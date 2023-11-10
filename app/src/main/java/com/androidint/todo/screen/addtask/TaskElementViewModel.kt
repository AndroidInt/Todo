package com.androidint.todo.screen.addtask

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.androidint.todo.repository.CategoryRepositoryImpl
import com.androidint.todo.repository.TaskRepositoryImpl
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.TimeTask
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

class TaskElementViewModel @Inject constructor(
    private val taskRepository: TaskRepositoryImpl,
    private val categoryRepository: CategoryRepositoryImpl,
):ViewModel() {

    private var _conflictedDurationState = mutableStateOf(false)
    val conflictedDurationState: State<Boolean> = _conflictedDurationState
    private var _conflictedCategoryState = mutableStateOf(false)
    val conflictedCategoryState: State<Boolean> = _conflictedCategoryState
    private var _categoryList = mutableStateListOf<Category>()
    val categoryList: SnapshotStateList<Category> = _categoryList
    private var _updateRequest = mutableStateOf(false)
    val updateRequest: State<Boolean> = _updateRequest
    private var _successfullyDone = mutableStateOf(false)
    val successfullyDone: State<Boolean> = _successfullyDone











}