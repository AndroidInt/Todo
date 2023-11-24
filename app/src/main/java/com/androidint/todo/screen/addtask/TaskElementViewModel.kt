package com.androidint.todo.screen.addtask

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidint.todo.repository.CategoryRepositoryImpl
import com.androidint.todo.repository.MediumRepositoryImpl
import com.androidint.todo.repository.TaskRepositoryImpl
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Tag
import com.androidint.todo.repository.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskElementViewModel @Inject constructor(
    private val taskRepository: TaskRepositoryImpl,
    private val categoryRepository: CategoryRepositoryImpl,
    private val mediumRepository: MediumRepositoryImpl
) : ViewModel() {


    private var _task: MutableState<Task?> = mutableStateOf(null)
    val task: State<Task?>
        get() = _task

    private var _category: MutableState<Category> = mutableStateOf(Category())
    val category: State<Category>
        get() = _category

    private var _categories: SnapshotStateList<Category> = mutableStateListOf()
    val categories: SnapshotStateList<Category>
        get() = _categories


    private var _tags: SnapshotStateList<Tag> = mutableStateListOf()
    val tags: SnapshotStateList<Tag>
        get() = _tags

    private var _submitDataState: MutableState<TaskState> = mutableStateOf(TaskState.Idle)
    val submitDataState: State<TaskState>
        get() = _submitDataState

    init {
        viewModelScope.launch {
            categoryRepository.getAll().collect {
                _categories = mutableStateListOf()
                _categories.addAll(it)
            }
        }
    }

    // when client wants to update task, it should be call from TaskElementScreen()
    fun initTask(taskId: Long?) {
        viewModelScope.launch {
            val taskTags = taskId?.let { taskRepository.getTaskWithTags(it) }
            if (taskTags != null) {
                _task.value =taskTags.task
            }
            task.value?.let {
                categoryRepository.findById(it.ownerCategoryId)?.let {
                    _category.value = it
                }
            }
            if (taskTags != null) {
                _tags.addAll(taskTags.tags)
            }
        }
    }

    fun addTask(task: Task, tags: List<Tag>, category: Category) {
        _submitDataState.value = TaskState.Loading
        _category.value = category
        viewModelScope.launch {
            kotlin.runCatching {
                mediumRepository.addTask(task, tags, category)
            }.onFailure {
                _submitDataState.value = TaskState.Error(it.message)
            }.onSuccess {
                _submitDataState.value = TaskState.Success
            }
        }
    }

    fun updateTask(task: Task, tags: List<Tag>, category: Category) {
        _submitDataState.value = TaskState.Loading
        _category.value = category
        viewModelScope.launch {
            kotlin.runCatching {
                mediumRepository.updateTask(task, tags, category)
            }.onFailure {
                _submitDataState.value = TaskState.Error(it.message)
            }.onSuccess {
                _submitDataState.value = TaskState.Success
            }
        }
    }

}


