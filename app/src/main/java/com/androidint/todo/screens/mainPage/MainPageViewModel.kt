package com.androidint.todo.screens.mainPage

import androidx.compose.runtime.collectAsState
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

    fun getTasksWithCategory(): List<CategoryWithTasks>? {
        return repository.getCategoriesWithTasks().flowOn(Dispatchers.IO)
            .asLiveData(viewModelScope.coroutineContext).value

    }

    fun getAllTasks(): List<Task>? {
        return repository.getAll().flowOn(Dispatchers.IO)
            .asLiveData(viewModelScope.coroutineContext).value
    }

    fun deleteTask(task: Task){
        repository.delete(task = task)
    }
    fun doneCurrentTask(task: Task){
        repository.updateTask(task)
    }

}



