package com.androidint.todo.repository

import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.room.TaskDao
import com.androidint.todo.repository.room.TodoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) {



    suspend fun insert(task: Task) {
        return taskDao.insert(task)
    }

    fun getAll() =
        flow {
            emit(taskDao.getAll())
        }



    suspend fun getAllNoDone(): Flow<List<Task>> {
        return taskDao.getAllNoDone()
    }

    fun delete(task: Task) {
        CoroutineScope(SupervisorJob()).launch{
            taskDao.delete(task)
        }
    }

    suspend fun findById(id: Int): Task? {
        return taskDao.findById(id)
    }



    fun getCategoriesWithTasks() =
        flow {
            emit(taskDao.getCategoriesWithTasks())
        }


     fun updateTask(task: Task): Flow<Int> {
        return flow {
            emit(taskDao.updateTask(task))
        }
    }


}