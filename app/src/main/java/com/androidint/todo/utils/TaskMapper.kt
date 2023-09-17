package com.androidint.todo.utils

import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TaskWithCategoryName
import kotlinx.coroutines.flow.merge
import javax.inject.Inject


class TaskMapper() {
    companion object{
        fun mapToTaskList(categoryWithTasksList: List<CategoryWithTasks>): MutableList<TaskWithCategoryName> {
            val tasksWithCategoryNameList: MutableList<TaskWithCategoryName> = ArrayList()
            for (item in categoryWithTasksList) {
                for (task in item.tasks) {
                    tasksWithCategoryNameList.add(
                        TaskWithCategoryName(
                            task.title,
                            task.description,
                            task.day,
                            task.ownerCategoryId,
                            task.timeDuration,
                            task.priority,
                            task.done,
                            item.category.name
                        )
                    )

                }
            }
            return tasksWithCategoryNameList
        }
        fun generateData(task: Task){

        }
    }


}