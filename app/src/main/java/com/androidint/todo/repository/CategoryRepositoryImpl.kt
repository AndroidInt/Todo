package com.androidint.todo.repository

import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.room.CategoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryDao: CategoryDao) {

    suspend fun insert(category: Category): Int {
        return categoryDao.insert(category)
    }

    fun getAll(): Flow<List<Category>> {
        return categoryDao.getAll()
    }


    suspend fun delete(category: Category) {
        categoryDao.delete(category)
    }

    suspend fun findById(id: Int): Category? {
        return categoryDao.findById(id)
    }

    suspend fun getCategoryByName(name: String): Category? {
        return categoryDao.getCategoryByName(name)
    }

    suspend fun getCategoryByColor(color: Int): Category? {
        return categoryDao.getCategoryByColor(color)
    }

    suspend fun updateCategory(category: Category):Int {
        return categoryDao.updateCategory(category)
    }

}