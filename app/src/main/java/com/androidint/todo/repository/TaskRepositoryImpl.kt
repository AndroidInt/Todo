package com.androidint.todo.repository

import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.room.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) {

    suspend fun insert(task: Task){
        return taskDao.insert(task)
    }

    suspend fun getAll(): Flow<List<Task>> {
        return taskDao.getAll()
    }

    suspend fun getAllNoDone(): Flow<List<Task>> {
        return taskDao.getAllNoDone()
    }

    suspend fun delete(task: Task){
        taskDao.delete(task)
    }

    suspend fun findById(id: Int): Task? {
        return taskDao.findById(id)
    }

    suspend fun getCategoriesWithTasks():Flow<List<CategoryWithTasks>>{
        return taskDao.getCategoriesWithTasks()
    }

    suspend fun updateTask(task : Task){
        return taskDao.updateTask(task)
    }


}