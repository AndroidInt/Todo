package com.androidint.todo.screen.addtask

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidint.todo.repository.CategoryRepositoryImpl
import com.androidint.todo.repository.TaskRepositoryImpl
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TimeTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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
class AddTaskScreenViewModel @Inject constructor(
    private val taskRepository: TaskRepositoryImpl,
    private val categoryRepository: CategoryRepositoryImpl,
) : ViewModel() {

    private var _conflictedDurationState = mutableStateOf(false)
    val conflictedDurationState: State<Boolean> = _conflictedDurationState
    private var _conflictedCategoryState = mutableStateOf(false)
    val conflictedCategoryState: State<Boolean> = _conflictedCategoryState
    private var _categoryList = mutableStateListOf<Category>()
    val categoryList : SnapshotStateList<Category> = _categoryList
    private var _updateRequest = mutableStateOf(false)
    val updateRequest :State<Boolean> = _updateRequest
    init {
        viewModelScope.launch(Dispatchers.IO) {
             categoryRepository.getAll().collect{
                 _categoryList.addAll(it)
            }
        }
    }
    fun resetState(){
        _conflictedCategoryState.value = false
        _conflictedDurationState.value = false
        _updateRequest.value = false

    }
    fun addTask(task: Task, category: Category) {

        viewModelScope.launch(context = Dispatchers.IO) {
            taskRepository.getAll().collect { it ->
                val subList = it.filter {
                    it.day.year == task.day.year &&
                            it.day.month == task.day.month &&
                            it.day.dayOfMonth == task.day.dayOfMonth

                }.toList()
                _conflictedDurationState.value = subList.any { item ->
                    item.timeDuration.isConflicted(task.timeDuration)
                }
            }
            categoryRepository.getAll().collect {
                _conflictedCategoryState.value = it.any { item ->
                    item.name == category.name && item.color != category.color
                }
            }

            if (!conflictedDurationState.value && !conflictedCategoryState.value) {

                categoryRepository.getCategoryByName(category.name)
                val categoryFromRoom = categoryRepository.getCategoryByColor(category.color)

                categoryFromRoom?.let {
                    if (category.name != it.name) {
                        it.name = category.name
                        categoryRepository.updateCategory(it)
                    }
                }
                if (categoryFromRoom == null)
                    categoryRepository.insert(category)

                task.ownerCategoryId = categoryRepository.getCategoryByName(category.name)!!.categoryId
                taskRepository.insert(task)
                resetState()
            }


        }
    }

    fun updateTask(task: Task, category: Category) {

        viewModelScope.launch(context = Dispatchers.IO) {
            taskRepository.getAll().collect { it ->
                val subList = it.filter {
                    it.day.year == task.day.year &&
                            it.day.month == task.day.month &&
                            it.day.dayOfMonth == task.day.dayOfMonth

                }.toList()
                _conflictedDurationState.value = subList.any { item ->
                    if (item.timeDuration != task.timeDuration )
                    {
                        item.timeDuration.isConflicted(task.timeDuration)
                    }else{
                        false
                    }
                }
            }
            categoryRepository.getAll().collect {
                _conflictedCategoryState.value = it.any { item ->
                    item.name == category.name && item.color != category.color
                }
            }

            if (!conflictedDurationState.value && !conflictedCategoryState.value) {

                categoryRepository.getCategoryByName(category.name)
                val categoryFromRoom = categoryRepository.getCategoryByColor(category.color)

                categoryFromRoom?.let {
                    if (category.name != it.name) {
                        it.name = category.name
                        categoryRepository.updateCategory(it)
                    }
                }
                if (categoryFromRoom == null)
                    categoryRepository.insert(category)

                task.ownerCategoryId = categoryRepository.getCategoryByName(category.name)!!.categoryId
                taskRepository.updateTask(task)
                resetState()
            }


        }





    }

}