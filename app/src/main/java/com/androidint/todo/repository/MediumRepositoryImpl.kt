package com.androidint.todo.repository

import androidx.room.withTransaction
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Tag
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TaskTagCrossRef
import com.androidint.todo.repository.room.TodoDatabase
import javax.inject.Inject

class MediumRepositoryImpl @Inject constructor(
   private val categoryRepository: CategoryRepositoryImpl,
   private val taskRepository: TaskRepositoryImpl,
   private val todoDatabase: TodoDatabase
) {
    suspend fun addTask(task: Task, tags: List<Tag>, category: Category) {
        todoDatabase.withTransaction {
            task.ownerCategoryId = addOrUpdateCategory(category)
            val taskId = taskRepository.insert(task)
            insertTagsByTask(taskId, tags)
        }
    }
    suspend fun updateTask(task: Task, tags: List<Tag>, category: Category) {
        todoDatabase.withTransaction {
            task.ownerCategoryId = addOrUpdateCategory(category)
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
        val surplus  = tags.distinctBy {
            !taskTags.tags.contains(it)
        }
        //make concise excessive-Tags-Task-CrossRef to excessiveTTCR
        val excessiveTTCR =excessive.map {
            TaskTagCrossRef(task.taskId!!,it.tagId!!)
        }
        taskRepository.deleteTaskTagCrossRef(excessiveTTCR)
        insertTagsByTask(task.taskId!!,surplus)
    }
    private suspend fun insertTagsByTask(taskId: Int, tags: List<Tag>) {
        val tagsId = taskRepository.insertTags(tags)
        val taskTagsList = mutableListOf<TaskTagCrossRef>()
        tagsId.forEach {
            taskTagsList.add(TaskTagCrossRef(taskId, it))
        }
        taskRepository.insertTaskTagCrossRef(taskTagsList)
    }
    private suspend fun addOrUpdateCategory(category: Category):Int {
        val categories = mutableListOf<Category>()
        categoryRepository.getAll().collect{
            categories.addAll(it)
        }
        val updateOperate = categories.any {
            category.color == it.color
        }
        return if (updateOperate) {
            updateCategory(category)
        } else {
            addCategory(category)
        }
    }
    private suspend fun addCategory(category: Category):Int {
        return categoryRepository.insert(category)
    }
    private suspend fun updateCategory(category: Category): Int {
      return categoryRepository.updateCategory(category)
    }
}