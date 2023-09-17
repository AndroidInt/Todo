package com.androidint.todo.repository

import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.room.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) {


    fun exceptionHandling(){

    }


    suspend fun insert(task: Task) {
        return taskDao.insert(task)
    }

    fun getAll() =
        flow {
            try {
            emit(taskDao.getAll())

            }catch (e: NullPointerException){
                print("fucking nulllllllllll")
            }
        }


    suspend fun getAllNoDone(): Flow<List<Task>> {
        return taskDao.getAllNoDone()
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    suspend fun findById(id: Int): Task? {
        return taskDao.findById(id)
    }

    fun getCategoriesWithTasks() =
        flow {
            try {
            emit(taskDao.getCategoriesWithTasks())
            }catch (e: NullPointerException){
                print("nulllll")
            }

        }


    suspend fun updateTask(task: Task) {
        return taskDao.updateTask(task)
    }


}