package com.androidint.todo.repository.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.repository.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: Task)

    @Query("SELECT * FROM Task")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM Task ts WHERE ts.done == 0 ")
    fun getAllNoDone(): Flow<List<Task>>

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM TASK T WHERE T.taskId ==:id")
    suspend fun findById(id:Int): Task?



    @Transaction
    @Query("SELECT * FROM Category")
    fun getCategoriesWithTasks(): Flow<List<CategoryWithTasks>>

    @Update
    suspend fun updateTask(task: Task): Int

    @Query("SELECT * FROM TASK WHERE day_dayOfMonth ==:day AND day_month==:month AND day_year==:year")
    fun getTaskByDate(year: Int, month: Int, day: Int): Flow<List<Task>>


}

@Dao
interface CategoryDao {

    @Insert
    suspend fun insert(category: Category)

    @Query("SELECT * FROM Category")
    fun getAll(): Flow<List<Category>>

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM Category T WHERE T.categoryId ==:id")
    suspend fun findById(id:Int): Category?

    @Query("SELECT * FROM Category T WHERE T.name == :name")
    suspend fun getCategoryByName(name:String): Category?

    @Query("SELECT * FROM Category T WHERE T.color == :color")
    suspend fun getCategoryByColor(color:Int): Category?






    @Update
    suspend fun updateCategory(category: Category)

    @Query("Select * From Category T WHERE T.name==:name")
    suspend fun contain(name: String): Category?



}