package com.androidint.todo.repository

import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.repository.model.Tag
import com.androidint.todo.repository.model.TagWithTasks
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TaskTagCrossRef
import com.androidint.todo.repository.model.TaskWithTags
import com.androidint.todo.repository.room.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject


class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) {

    suspend fun insert(task: Task): Long {
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

    suspend fun findById(id: Long): Task? {
        return taskDao.findById(id)
    }

    fun getCategoriesWithTasks(): Flow<List<CategoryWithTasks>> {
        return taskDao.getCategoriesWithTasks()
    }

    suspend fun updateTask(task: Task) {
        return taskDao.updateTask(task)
    }

    suspend fun getTaskByDate(year: Int, month: Int, day: Int): Flow<List<Task>> {
        return taskDao.getTaskByDate(year, month, day)
    }

    suspend fun insertTags(tags:List<Tag>):List<Long>{
        return taskDao.insertTags(tags)
    }


    suspend fun insertTag(tag:Tag): Long {
        return taskDao.insertTag(tag)
    }

    suspend fun insertTaskTagCrossRef(taskTags: List<TaskTagCrossRef>) {
        return taskDao.insertTaskTagCrossRef(taskTags)
    }

    suspend fun getTaskWithTags(): Flow<List<TaskWithTags>> {
        return taskDao.getTaskWithTags()
    }
    suspend fun getTaskWithTags(id: Long): TaskWithTags {
        return withContext(Dispatchers.IO){
             taskDao.getTaskWithTags(id)
        }
    }

    suspend fun getTagWithTasks(): Flow<List<TagWithTasks>> {
        return taskDao.getTagWithTasks()
    }

    suspend fun deleteTaskTagCrossRef(taskTags : List<TaskTagCrossRef>){
        taskDao.deleteTaskTagCrossRef(taskTags)
    }

}