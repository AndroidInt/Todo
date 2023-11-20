package com.androidint.todo.screen.addtask

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidint.todo.repository.CategoryRepositoryImpl
import com.androidint.todo.repository.MediumRepositoryImpl
import com.androidint.todo.repository.TaskRepositoryImpl
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Tag
import com.androidint.todo.repository.model.Task
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskElementViewModel @Inject constructor(
    private val taskRepository: TaskRepositoryImpl,
    private val categoryRepository: CategoryRepositoryImpl,
    private val mediumRepository: MediumRepositoryImpl
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
        viewModelScope.launch {
            mediumRepository.addTask(task,tags,category)
        }
    }

    fun updateTask(task: Task, tags: List<Tag>, category: Category) {
        _category.value = category
        viewModelScope.launch {
            mediumRepository.updateTask(task,tags,category)
        }
    }

}


