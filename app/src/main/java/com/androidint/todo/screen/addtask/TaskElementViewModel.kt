package com.androidint.todo.screen.addtask

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidint.todo.repository.CategoryRepositoryImpl
import com.androidint.todo.repository.TaskRepositoryImpl
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Task
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

    init {
        viewModelScope.launch {
            categoryRepository.getAll().collect {
                categories.addAll(
                    it
                )
            }
        }
    }


//    addTask: (task: Task) -> Unit
//    updateTask: (task: Task) -> Unit = {},
//    addTaskState: State<Boolean> = remember { mutableStateOf(true) },
//    onPushTaskState: State<TaskState>
//


//    private var _conflictedDurationState = mutableStateOf(false)
//    val conflictedDurationState: State<Boolean> = _conflictedDurationState
//    private var _conflictedCategoryState = mutableStateOf(false)
//    val conflictedCategoryState: State<Boolean> = _conflictedCategoryState
//    private var _categoryList = mutableStateListOf<Category>()
//    val categoryList: SnapshotStateList<Category> = _categoryList
//    private var _updateRequest = mutableStateOf(false)
//    val updateRequest: State<Boolean> = _updateRequest
//    private var _successfullyDone = mutableStateOf(false)
//    val successfullyDone: State<Boolean> = _successfullyDone


    suspend fun addTask(task: Task) {
        addOrUpdateCategory()
        viewModelScope.launch {
            kotlin.runCatching {
                val taskId = taskRepository.insert(task)
//                val tagId = taskRepository.insertTag()
            }.onFailure {

            }.onSuccess {

            }
        }
    }


    suspend fun updateTask(task: Task) {
        addOrUpdateCategory()
        viewModelScope.launch {
            kotlin.runCatching {
                taskRepository.updateTask(task)
            }.onFailure {

            }.onSuccess {

            }
        }

    }

    private suspend fun addCategory(category: Category): Boolean {

        val returnValue = viewModelScope.async {
            var result = false
            kotlin.runCatching {
                categoryRepository.insert(category)
            }.onFailure {
                result = false
            }.onSuccess {
                result = true
            }
            return@async result
        }
        return returnValue.await()
    }

    private suspend fun updateCategory(category: Category): Boolean {

        val returnValue = viewModelScope.async {
            var result = false
            kotlin.runCatching {
                categoryRepository.updateCategory(category)
            }.onFailure {
                result = false
            }.onSuccess {
                result = true
            }
            return@async result
        }
        return returnValue.await()
    }

    private fun addOrUpdateCategory() {
        val updateOperate = categories.any {
            category.value.color == it.color
        }
        viewModelScope.launch {
            if (updateOperate) {
                updateCategory(category.value)
            } else {
                addCategory(category.value)
            }
        }

    }
}


