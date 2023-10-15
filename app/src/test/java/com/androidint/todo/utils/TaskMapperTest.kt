package com.androidint.todo.utils

import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.repository.model.Day
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TaskWithCategoryName
import com.androidint.todo.repository.model.TimeTask
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class TaskMapperTest {
    lateinit var categoryWithTasks: MutableList<CategoryWithTasks>
    @Test
    fun taskMapperTest() {
        var mapToTaskWitCategoryName = TaskMapper.generateData(categoryWithTasks)
           val obj = categoryWithTasks.get(0).category.name
        assertThat(mapToTaskWitCategoryName).contains(obj)

    }

    @Before
    fun setup() {
        categoryWithTasks = mutableListOf<CategoryWithTasks>()
        val numberOfCategories = 10
        val tasksPerCategory = 7

        for (i in 1..numberOfCategories) {
            val category = Category(name = "Category $i")
            val tasks = mutableListOf<Task>()

            for (j in 1..tasksPerCategory) {
                val task = Task(
                    title = "Task $j",
                    description = "Description for Task $j in Category $i",
                    day = Day(dayOfWeek = 1, dayOfMonth = 1, month = 9, year = 2023),
                    ownerCategoryId = category.categoryId,
                    timeDuration = TimeTask(
                        startHour = 9,
                        startMinute = 0,
                        endHour = 10,
                        endMinute = 30
                    ),
                    priority = 2,
                    done = false
                )
                tasks.add(task)
            }
            val categoryWithTask = CategoryWithTasks(category, tasks)
            categoryWithTasks.add(categoryWithTask)
        }

    }
}