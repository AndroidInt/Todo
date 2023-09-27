package com.androidint.todo.repository.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation


/*
    one-to-many relationship between Task(child entity) and Category (parent entity)
*/


@Entity
data class Task(
    val title: String,
    val description: String?,
    @Embedded
    val day: Day,
    val ownerCategoryId: Int,
    @Embedded
    val timeDuration: TimeTask,
    val priority: Int = 1,
    var done: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var taskId: Int = 0
}

enum class DayOfWeek() {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;


    fun next(): DayOfWeek {
        val values = DayOfWeek.values()
        val size = values.size
        var nextOrdinal = (ordinal + 1)
        if(nextOrdinal > size-1)
            nextOrdinal = 0
        return values[nextOrdinal]
    }
}

class Day(
    var dayOfWeek: Int,
    val dayOfMonth: Int,
    val month: Int,
    val year: Int
)


data class TimeTask(
    var startHour: Int = 0,
    var startMinute: Int = 0,
    var endHour: Int = 0,
    var endMinute: Int = 0
) : Comparable<TimeTask> {
    override fun compareTo(other: TimeTask): Int {
        return "${other.startHour.toString()}${other.startMinute.toString()}".toInt()
    }

    fun eventDuration(): Int {
        val hour = endHour - startHour
        val min = endMinute - startMinute
        return (hour * 60) + min
    }

    fun offsetTimeToMinutes(): Int {
        return startHour * 60 + startMinute
    }
}

@Entity(indices = [Index(value = ["name","color"], unique = true)])
data class Category(
    @ColumnInfo(name = "name") val name: String = "Inbox",
    @ColumnInfo(name = "color") val color: Int = 0,
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
