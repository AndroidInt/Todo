package com.androidint.todo.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Task

@Database(entities = [Task::class,
    Category::class], version = 1)
abstract class TodoDatabase : RoomDatabase(){

    abstract fun TaskDao(): TaskDao
    abstract fun CategoryDao(): CategoryDao

}