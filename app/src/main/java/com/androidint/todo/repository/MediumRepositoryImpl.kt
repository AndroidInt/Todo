package com.androidint.todo.repository

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.room.withTransaction
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Tag
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TaskTagCrossRef
import com.androidint.todo.repository.room.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class MediumRepositoryImpl @Inject constructor(
    private val categoryRepository: CategoryRepositoryImpl,
    private val taskRepository: TaskRepositoryImpl,
    private val todoDatabase: TodoDatabase
) {
    suspend fun addTask(task: Task, tags: List<Tag>, category: Category) {

        withContext(Dispatchers.IO) {
            todoDatabase.withTransaction {
                task.ownerCategoryId = addOrUpdateCategory(category)
                val taskId = taskRepository.insert(task)
                insertTagsByTask(taskId, tags)
            }
        }
    }

    suspend fun updateTask(task: Task, tags: List<Tag>, category: Category) {
        todoDatabase.withTransaction {
            addOrUpdateCategory(category)
            taskRepository.updateTask(task)
            changeTagsTask(task, tags)
        }
    }

    /** TODO(set taskId to task before call this method)
     *  Four states exist
     * [1]  Tags and taskTags.tags is the same ----------> it doesn't need to do anythings
     * [2]  Tags and taskTags.tags isn't the same -------> it needs to remove tag
     *                                          [3] |----> it needs to add tag
     *                                          [4] |----> it needs both add and remove
     */
    private suspend fun changeTagsTask(task: Task, tags: List<Tag>) {
        val taskTags = taskRepository.getTaskWithTags(task.taskId!!)
        val excessive = taskTags.tags.distinctBy {
            !tags.contains(it)
        }
        val surplus = tags.distinctBy {
            !taskTags.tags.contains(it)
        }
        //make concise excessive-Tags-Task-CrossRef to excessiveTTCR
        val excessiveTTCR = excessive.map {
            TaskTagCrossRef(task.taskId!!, it.tagId!!)
        }
        taskRepository.deleteTaskTagCrossRef(excessiveTTCR)
        insertTagsByTask(task.taskId!!, surplus)
    }

    private suspend fun insertTagsByTask(taskId: Long, tags: List<Tag>) {
        val tagsId = taskRepository.insertTags(tags)
        val taskTagsList = mutableListOf<TaskTagCrossRef>()
        tagsId.forEach {
            taskTagsList.add(TaskTagCrossRef(taskId, it))
        }
        taskRepository.insertTaskTagCrossRef(taskTagsList)
    }

    private suspend fun addOrUpdateCategory(category: Category): Long {

        val categories = categoryRepository.getAll().firstOrNull()
        val updateOperate = categories.any {
            category.color == it.color
        }
        return if (updateOperate) {
            updateCategory(category)
            -1
        } else {
            addCategory(category)
        }
    }

    private suspend fun addCategory(category: Category): Long {
        return categoryRepository.insert(category)
    }

    private suspend fun updateCategory(category: Category) {
        return categoryRepository.updateCategory(category)
    }
}