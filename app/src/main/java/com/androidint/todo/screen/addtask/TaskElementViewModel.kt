package com.androidint.todo.screen.addtask

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidint.todo.repository.CategoryRepositoryImpl
import com.androidint.todo.repository.TaskRepositoryImpl
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Tag
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TaskTagCrossRef
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskElementViewModel @Inject constructor(
    private val taskRepository: TaskRepositoryImpl,
    private val categoryRepository: CategoryRepositoryImpl,
) : ViewModel() {


    private lateinit var _task: MutableState<Task>
    val task: State<Task>
        get() = _task

    private lateinit var _category: MutableState<Category>
    val category: State<Category>
        get() = _category

    private lateinit var _categories: SnapshotStateList<Category>
    val categories: SnapshotStateList<Category>
        get() = _categories

    private lateinit var _submitDataState: MutableState<TaskState>
    val submitDataState: State<TaskState>
        get() = _submitDataState

    init {
        viewModelScope.launch {
            categoryRepository.getAll().collect {
                categories.addAll(
                    it
                )
            }
        }
    }

    fun addTask(task: Task, tags: List<Tag>, category: Category) {
        _category.value = category
        var taskId = 0
        viewModelScope.launch {
            addOrUpdateCategory()
            if (submitDataState.value::class == TaskState.Success::class) {
                task.ownerCategoryId = (submitDataState.value as TaskState.Success).id
                kotlin.runCatching {
                    taskId = taskRepository.insert(task)
                }.onFailure {
                    _submitDataState.value = TaskState.Error(it.message)
                }.onSuccess {
                    insertTagsByTask(taskId, tags)
                    _submitDataState.value = TaskState.Success(0)
                }
            }
        }
    }

    private suspend fun insertTagsByTask(taskId: Int, tags: List<Tag>) {
        val tagsId = taskRepository.insertTags(tags)
        val taskTagsList = mutableListOf<TaskTagCrossRef>()
        tagsId.forEach {
            taskTagsList.add(TaskTagCrossRef(taskId, it))
        }
        taskRepository.insertTaskTagCrossRef(taskTagsList)
    }


    fun updateTask(task: Task, tags: List<Tag>, category: Category) {
        _category.value = category
        viewModelScope.launch {
            addOrUpdateCategory()
            if (submitDataState.value::class == TaskState.Success::class) {
                task.ownerCategoryId = (submitDataState.value as TaskState.Success).id
                kotlin.runCatching {
                    val taskId = taskRepository.updateTask(task)
                }.onFailure {
                    _submitDataState.value = TaskState.Error(it.message)
                }.onSuccess {
                    changeTagsTask(task, tags)
                    _submitDataState.value = TaskState.Success(0)
                }
            }
        }
    }

    private suspend fun changeTagsTask(task: Task, tags: List<Tag>) {
        val taskTags = taskRepository.getTaskWithTags(task.taskId!!)
        /**
         *  Four states exist
         * [1]  Tags and taskTags.tags is the same ----------> it doesn't need to do anythings
         * [2]  Tags and taskTags.tags isn't the same -------> it needs to remove tag
         *                                          [3] |----> it needs to add tag
         *                                          [4] |----> it needs both add and remove
         */
        val excessive = taskTags.tags.distinctBy {
            !tags.contains(it)
        }
        val surplus  = tags.distinctBy {
            !taskTags.tags.contains(it)
        }
        val excessiveTTCR =excessive.map {
            TaskTagCrossRef(task.taskId!!,it.tagId!!)
        }
//        val surplusIDs = taskRepository.insertTags(surplus)
//        val surplusTTCR = surplusIDs.map {
//            TaskTagCrossRef(task.taskId!!,it)
//        }
        kotlin.runCatching {
            taskRepository.deleteTaskTagCrossRef(excessiveTTCR)
        }.onSuccess {  }.onFailure {  }
        kotlin.runCatching {
            insertTagsByTask(task.taskId!!,surplus)

        }

    }

    private suspend fun addOrUpdateCategory() {
        val updateOperate = categories.any {
            category.value.color == it.color
        }
        if (updateOperate) {
            updateCategory()
        } else {
            addCategory()
        }
    }

    private suspend fun addCategory() {
        var id = 0
        kotlin.runCatching {
            id = categoryRepository.insert(category.value)
        }.onFailure {
            _submitDataState.value = TaskState.Error(it.message)
        }.onSuccess {
            _submitDataState.value = TaskState.Success(id)
        }
    }

    private suspend fun updateCategory() {
        var id = 0
        kotlin.runCatching {
            id = categoryRepository.updateCategory(category.value)
        }.onFailure {
            _submitDataState.value = TaskState.Error(it.message)
        }.onSuccess {
            _submitDataState.value = TaskState.Success(id)
        }

    }


}


