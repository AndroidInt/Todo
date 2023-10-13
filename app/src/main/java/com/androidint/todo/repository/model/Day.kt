package com.androidint.todo.repository.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


/*
    one-to-many relationship between Task(child entity) and Category (parent entity)
*/


@Entity
 open class Task(
    open val title: String,
    open val description: String?,
    @Embedded
    open val day: Day,
    open val ownerCategoryId: Int,
    @Embedded
    open val timeDuration: TimeTask,
    open val priority: Int = 1,
    open var done: Boolean = false

) {
    @PrimaryKey(autoGenerate = true)
    var taskId: Int = 0
}

class Day(
    var dayOfWeek: Int,
    val dayOfMonth: Int,
    val month: Int,
    val year: Int
)


data class TimeTask(
    val startHour: Int = 0,
    val startMinute: Int = 0,
    val endHour: Int = 0,
    val endMinute: Int = 0
)

@Entity
data class Category(
    val name: String = "Inbox"
) {
    @PrimaryKey(autoGenerate = true)
    var categoryId: Int = 0
}

data class CategoryWithTasks(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId", //name of Id field that belongs to Category
        entityColumn = "ownerCategoryId" //name of Id field that was created for relationship
    )
    val tasks: List<Task>
)
