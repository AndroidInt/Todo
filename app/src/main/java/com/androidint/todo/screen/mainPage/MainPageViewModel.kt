package com.androidint.todo.screen.mainPage

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.RoomDatabase
import com.androidint.todo.repository.TaskRepositoryImpl
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.repository.model.Day
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TimeTask
import com.androidint.todo.repository.room.TodoDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainPageViewModel @Inject constructor(private val repository: TaskRepositoryImpl) :
    ViewModel() {




    private var _tasksWithCategory = mutableStateListOf<CategoryWithTasks>()
    val tasksWithCategory : SnapshotStateList<CategoryWithTasks> = _tasksWithCategory
    private var _tasks = mutableStateListOf<Task>()
    val tasks : SnapshotStateList<Task> = _tasks
    init {
        viewModelScope.launch(){
            repository.getCategoriesWithTasks().collect {
                _tasksWithCategory.addAll(it)
            }
            repository.getAll().collect{
                _tasks.addAll(it)
            }
        }
    }
    fun getTasksWithCategory(): List<CategoryWithTasks>? {
        return repository.getCategoriesWithTasks().flowOn(Dispatchers.IO)
            .asLiveData(viewModelScope.coroutineContext).value

    }

    fun getAllTasks(): List<Task>? {
        return repository.getAll().flowOn(Dispatchers.IO)
            .asLiveData(viewModelScope.coroutineContext).value
    }

    fun deleteTask(task: Task,scope: CoroutineScope){
        scope.launch {
            repository.delete(task = task)
        }

    }
    fun doneCurrentTask(task: Task,scope: CoroutineScope){
        scope.launch {
            repository.updateTask(task)
        }

    }

}



