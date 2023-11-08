package com.androidint.todo.repository.model

import android.os.Parcelable
import androidx.compose.ui.graphics.vector.RootGroupName
import androidx.navigation.NavGraph
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


/*
    one-to-many relationship between Task(child entity) and Category (parent entity)
*/
@Parcelize
@Entity
data class Task(
    val title: String,
    val description: String?,
    @Embedded(prefix = "day_")
    val day: Day,
    var ownerCategoryId: Int,
    @Embedded
    val timeDuration: TimeTask,
    val priority: Int = 1,
    var done: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    var taskId: Int? = null,

//    var tags:List<Tag>? = null
) :Parcelable

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Tag(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
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
        if (nextOrdinal > size - 1)
            nextOrdinal = 0
        return values[nextOrdinal]
    }
}

@Parcelize
class Day(
    var dayOfWeek: Int,
    val dayOfMonth: Int,
    val month: Int,
    val year: Int
) :Parcelable{
    fun equal(other: Day): Boolean {
        return other.dayOfWeek == this.dayOfWeek &&
                other.dayOfMonth == this.dayOfMonth &&
                other.month == this.month &&
                other.year == this.year
    }


}

@Parcelize
data class TimeTask(
    var startHour: Int = 0,
    var startMinute: Int = 0,
    var endHour: Int = 0,
    var endMinute: Int = 0
) : Comparable<TimeTask>,Parcelable {
    override fun compareTo(other: TimeTask): Int {
        return "${other.startHour.toString()}${other.startMinute.toString()}".toInt()
    }


    fun isConflicted(other: TimeTask): Boolean {
        val start = (this.startHour.hours + this.startMinute.minutes).inWholeMinutes
        val end = (this.endHour.hours + this.endMinute.minutes).inWholeMinutes
        val secondStart = (other.startHour.hours + other.startMinute.minutes).inWholeMinutes
        val secondEnd = (other.endHour.hours + other.endMinute.minutes).inWholeMinutes

        if (secondStart in start  until end+1)
            return true
        if (secondEnd in start  until end+1)
            return true
        if (end in secondStart until secondEnd+1)
            return true
        if (start in secondStart until secondEnd + 1)
            return true

        return false
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
@Parcelize
@Entity(indices = [Index(value = ["name", "color"], unique = true)])
data class Category(

    @ColumnInfo(name = "name") var name: String = "Inbox",
    @ColumnInfo(name = "color") var color: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var categoryId: Int? = null,
):Parcelable

data class CategoryWithTasks(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId", //name of Id field that belongs to Category
        entityColumn = "ownerCategoryId" //name of Id field that was created for relationship
    )
    val tasks: List<Task>
)
