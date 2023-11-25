package com.androidint.todo.screen.addtask

import android.util.Log
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
import com.androidint.todo.repository.model.Day
import com.androidint.todo.repository.model.Tag
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TimeTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class ErrorValidation {
    object TimeDate : ErrorValidation()
    object Category : ErrorValidation()

}

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

    private var _stateInputValueValidation: SnapshotStateList<ErrorValidation> =
        mutableStateListOf()
    val stateErrorValidation: SnapshotStateList<ErrorValidation>
        get() = _stateInputValueValidation

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

    fun validateInput(timeTask: TimeTask, day: Day, category: Category) {
        _stateInputValueValidation.clear()

        viewModelScope.launch(Dispatchers.IO) {
            timeDateValidation(timeTask, day)
            categoryValidation(category)

        }
    }

    private suspend fun categoryValidation(category: Category) {
       val categories = categoryRepository.getAll().firstOrNull()
        var conflict = false
        conflict = categories?.let { list ->
            list.any {
                it.name == category.name && it.color != category.color
            }
        } == true
        withContext(Dispatchers.Main) {
            if (conflict) _stateInputValueValidation.add(ErrorValidation.Category)
        }
    }


    private suspend fun timeDateValidation(timeTask: TimeTask, day: Day){
        val tasks =
            taskRepository.getTaskByDate(day.year, day.month, day.dayOfMonth).firstOrNull()
        var conflict = false
        conflict = tasks?.let { list ->
            list.any {
                timeTask.isConflicted(it.timeDuration)
            }
        } == true
        withContext(Dispatchers.Main){
            if (conflict) _stateInputValueValidation.add(ErrorValidation.TimeDate)
        }

    }

    // when client wants to update task, it should be call from TaskElementScreen()
    fun initTask(taskId: Long) {
        _submitDataState.value = TaskState.Loading
        viewModelScope.launch {
            val taskTags =  taskRepository.getTaskWithTags(taskId)
            _task.value = taskTags.task
            task.value?.let {
                categoryRepository.findById(it.ownerCategoryId)?.let { category ->
                    _category.value = category
                }
            }
            _tags.clear()
            _tags.addAll(taskTags.tags)
            Log.d("tags size", " ${tags.size}")
            _submitDataState.value = TaskState.Success

        }

    }

    fun addTask(task: Task, tags: List<Tag>, category: Category) {
        _submitDataState.value = TaskState.Loading
        _category.value = category
        viewModelScope.launch {

//           val fetchedTaskByDate = taskRepository
//               .getTaskByDate(task.day.year,task.day.month,task.day.dayOfMonth).firstOrNull()
//            val conflict = fetchedTaskByDate?.any {
//                task.timeDuration.isConflicted(it.timeDuration)
//            }
//            if (conflict) this.cancel("there is conflict in time")
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


