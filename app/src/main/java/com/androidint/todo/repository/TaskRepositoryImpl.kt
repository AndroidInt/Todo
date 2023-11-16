package com.androidint.todo.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.repository.model.Tag
import com.androidint.todo.repository.model.TagWithTasks
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TaskTagCrossRef
import com.androidint.todo.repository.model.TaskWithTags
import com.androidint.todo.repository.room.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) {

    suspend fun insert(task: Task): Int {
        return taskDao.insert(task)
    }

    fun getAll(): Flow<List<Task>> {
        return taskDao.getAll()
    }

    fun getAllNoDone(): Flow<List<Task>> {
        return taskDao.getAllNoDone()
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    suspend fun findById(id: Int): Task? {
        return taskDao.findById(id)
    }

    fun getCategoriesWithTasks(): Flow<List<CategoryWithTasks>> {
        return taskDao.getCategoriesWithTasks()
    }

    suspend fun updateTask(task: Task): Int {
        return taskDao.updateTask(task)
    }

    suspend fun getTaskByDate(year: Int, month: Int, day: Int): Flow<List<Task>> {
        return taskDao.getTaskByDate(year, month, day)
    }


    suspend fun insertTag(tag:Tag): Flow<List<Int>> {
        return taskDao.insertTag(tag)
    }

    suspend fun insertTaskTagCrossRef(taskTag: TaskTagCrossRef) {
        return taskDao.insertTaskTagCrossRef(taskTag)
    }

    suspend fun getTaskWithTags(): Flow<List<TaskWithTags>> {
        return taskDao.getTaskWithTags()
    }

    suspend fun getTagWithTasks(): Flow<List<TagWithTasks>> {
        return taskDao.getTagWithTasks()
    }


}