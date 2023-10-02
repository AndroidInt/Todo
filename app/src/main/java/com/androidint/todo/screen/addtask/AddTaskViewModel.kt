package com.androidint.todo.screen.addtask

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidint.todo.repository.CategoryRepositoryImpl
import com.androidint.todo.repository.TaskRepositoryImpl
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
TODO :
 Insert task
 Insert category
 get tasks for showing conflict error
 update task if it needs
 get categories from Room
 update category if it needs

 */
@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepositoryImpl,
    private val categoryRepository: CategoryRepositoryImpl,
) : ViewModel() {

    private var _conflictedDurationState = mutableStateOf(false)
    val conflictedDurationState: State<Boolean> = _conflictedDurationState
    private var _conflictedCategoryState = mutableStateOf(false)
    val conflictedCategoryState: State<Boolean> = _conflictedCategoryState
    private var _categoryList = mutableStateListOf<Category>()
    val categoryList: SnapshotStateList<Category> = _categoryList
    private var _updateRequest = mutableStateOf(false)
    val updateRequest: State<Boolean> = _updateRequest

    init {
        viewModelScope.launch {
            categoryRepository.getAll().collect {
                _categoryList.clear()
                _categoryList.addAll(it)
            }
        }
    }

    fun resetState() {
        _conflictedCategoryState.value = false
        _conflictedDurationState.value = false
        _updateRequest.value = false

    }

    fun addTask(task: Task, category: Category) {

        viewModelScope
            .launch(context = Dispatchers.IO)
            {
                preprocessInsertTask(task, category)
                if (!conflictedDurationState.value && !conflictedCategoryState.value) {

                    // change category name if there are a changes in category
                    val categoryFromRoom = categoryRepository.getCategoryByColor(category.color)
                    categoryFromRoom?.let {
                        if (category.name != it.name) {
                            it.name = category.name
                            updateCategory(it)

                        }
                    }
                    if (categoryFromRoom == null)
                        insertCategory(category)

                    task.ownerCategoryId =
                        categoryRepository.getCategoryByName(category.name)!!.categoryId
                    taskRepository.insert(task)

                    resetState()
                }


            }
    }

    private suspend fun updateCategory(category: Category) {
        categoryRepository.updateCategory(category)
    }

    private suspend fun insertCategory(category: Category) {
        categoryRepository.insert(category)
    }


    private suspend fun preprocessInsertTask(task: Task, category: Category) {
        viewModelScope.launch(context = Dispatchers.IO) {
            _conflictedDurationState.value =
                taskRepository.getTaskByDate(task.day.year, task.day.month, task.day.dayOfMonth)
                    .firstOrNull()?.any { item ->
                        item.timeDuration.isConflicted(task.timeDuration)
                    } == true

            _conflictedCategoryState.value = categoryRepository.getAll()
                .firstOrNull()?.any { item ->
                    item.name == category.name && item.color != category.color
                } == true

        }
    }

    private suspend fun preprocessUpdateTask(task: Task, category: Category) {

        _conflictedDurationState.value = taskRepository
            .getTaskByDate(task.day.year, task.day.month, task.day.dayOfMonth)
            .firstOrNull()?.any { item ->
                if (item.timeDuration != task.timeDuration) {
                    item.timeDuration.isConflicted(task.timeDuration)
                } else {
                    false
                }
            } == true


        categoryRepository.getAll().collect {
            _conflictedCategoryState.value = it.any { item ->
                item.name == category.name && item.color != category.color
            }
        }
    }


    fun updateTask(task: Task, category: Category) {

        viewModelScope.launch(context = Dispatchers.IO) {

            preprocessUpdateTask(task, category)



            if (!conflictedDurationState.value && !conflictedCategoryState.value) {

                // change category name if there are a changes in category
                val categoryFromRoom = categoryRepository.getCategoryByColor(category.color)

                categoryFromRoom?.let {
                    if (category.name != it.name) {
                        it.name = category.name
                        updateCategory(it)
                    }
                }
                if (categoryFromRoom == null)
                    insertCategory(category)

                task.ownerCategoryId =
                    categoryRepository.getCategoryByName(category.name)!!.categoryId
                taskRepository.updateTask(task)
                resetState()
            }


        }


    }

}