package com.androidint.todo.di

import android.content.Context
import androidx.room.Room
import com.androidint.todo.repository.room.CategoryDao
import com.androidint.todo.repository.room.TaskDao
import com.androidint.todo.repository.room.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodoDatabaseModule {

    @Singleton
    @Provides
    fun provideTaskDao(todoDatabase: TodoDatabase): TaskDao {
        return todoDatabase.TaskDao()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(todoDatabase: TodoDatabase): CategoryDao {
        return todoDatabase.CategoryDao()
    }

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext appContext: Context): TodoDatabase {

        return Room.databaseBuilder(
            appContext,
            TodoDatabase::class.java,
            "Todo"
        ).build()
    }


}