package com.androidint.todo.screen


import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
//import androidx.compose.ui.layout.RootMeasurePolicy.measure
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.androidint.todo.repository.model.Day
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TimeTask
import com.androidint.todo.ui.theme.TodoTheme
import com.androidint.todo.utils.DataStore
import java.io.Serializable
import java.util.Calendar
import kotlin.math.roundToInt




@Composable
fun EventLayout(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    timeBar: @Composable (hour: Int, minute: Int) -> Unit = { hour, minute ->
        val hourHeight = 120.dp
        Box(modifier = Modifier
            .height(hourHeight)
            .taskData(null, false)) {
            BasicSidebarLabel(hour, minute)
        }
    },
    content: @Composable (task: Task) -> Unit = {
        BasicEvent(task = it)

    }
) {
    val hourHeight = 120.dp
    val time = remember {
        mutableStateOf(Calendar.getInstance().get(Calendar.MINUTE)
        +Calendar.getInstance().get(Calendar.HOUR) * 60
        )
    }
    Layout(
        modifier = modifier,
        content = {


            (0..23).forEach {
                timeBar(it, 0)
            }

            tasks.sortedBy(Task::timeDuration).forEach { task ->
                Box(
                    modifier = Modifier
                        .taskData(task, false)
                ) {
                    content(task)
                }
            }
            //current time line
            // TODO("if you are in the current day then current time should be showed")

            Row(
                modifier = Modifier
                    .taskData(null, true)
            ) {
                Canvas(modifier = Modifier) {

                    drawCircle(Color.Blue, radius = 4.dp.toPx())

                }


                Box(modifier = Modifier) {
                    Divider(
                        Modifier
                            .height(1.dp)
                            .fillMaxWidth(), color = Color.Blue
                    )
                }
            }


        }
    )
    { measurables, constraints ->
        val height = hourHeight.roundToPx() * 24
        val placeableWithTasks = measurables.map { measurable ->

            val task: Task? = (measurable.parentData as TaskDataModifier).task
            val dividerExist = (measurable.parentData as TaskDataModifier).timeBarExist


            val taskHeight =
                ((task?.timeDuration?.eventDuration()
                    ?.div(60f))?.times(hourHeight.toPx()))?.roundToInt()

            val placeable = if (task == null) {
                measurable.measure(constraints)
            } else {
                taskHeight?.let { constraints.copy(minHeight = it, maxHeight = it) }
                    ?.let { measurable.measure(it) }!!
            }

            if (task == null) {
                Pair(placeable, null, dividerExist)
            } else {
                Pair(placeable, task, dividerExist)
            }

        }


        layout(constraints.maxWidth, height) {


            var heightTimeBar = 0
            val paddingHour = 10
            val taskPadding = constraints.maxWidth / 5
            placeableWithTasks.forEach { (placeable, task, dividerExist) ->
                if (task == null) {

                    if (dividerExist) {
                        val offsetTime = ((time.value / 60f) * hourHeight.toPx()).roundToInt()
                        placeable.place(paddingHour+10, offsetTime)
                    } else {
                        placeable.place(paddingHour, heightTimeBar)
                        heightTimeBar += hourHeight.toPx().toInt()
                    }


                } else {
                    val taskOffsetMinutes = task.timeDuration.offsetTimeToMinutes()
                    val taskY = ((taskOffsetMinutes.div(60f)).times(hourHeight.toPx())).roundToInt()
                    placeable.place(taskPadding, taskY)
                }

            }


        }
    }
}

public data class Pair<out A, out B, out C>(
    public val first: A,
    public val second: B,
    public val third: C
) : Serializable {

    /**
     * Returns string representation of the [Pair] including its [first] and [second] values.
     */
    public override fun toString(): String = "($first, $second , $third)"
}

private class TaskDataModifier(
    val task: Task?,
    val timeBarExist: Boolean
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = TaskDataModifier(task, timeBarExist)


}

fun Modifier.taskData(task: Task?, timeBarExist: Boolean) =
    this.then(TaskDataModifier(task, timeBarExist))


@Composable
fun BasicEvent(task: Task, modifier: Modifier = Modifier) {

    Row(
        modifier = modifier.background(
            MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(0.03F)
                .fillMaxHeight(1F)
                .background(
                    DataStore.categoryToColor(task.ownerCategoryId)
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(Modifier.fillMaxWidth(0.97F)) {
            Spacer(modifier = Modifier.height(16.dp))
            //TODO("pass category name to Text from ViewModel which has a fun that convert Id to category's name")
            Text(text = "meeting")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = task.title)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text =
                "${task.timeDuration.startHour}:${if (task.timeDuration.startMinute < 10) "0" + task.timeDuration.startMinute else task.timeDuration.startMinute}"
                        + " - " + "${task.timeDuration.endHour}:${if (task.timeDuration.endMinute < 10) "0" + task.timeDuration.endMinute else task.timeDuration.endMinute}"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

    }

}


@Composable
fun BasicSidebarLabel(
    hour: Int, minutes: Int,
    modifier: Modifier = Modifier,
) {

    Text(
        text = "$hour:${if (minutes < 10) 0 else ""}$minutes",
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
    )
}




@Preview(showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun BasicEventPreview() {

    val tasks = mutableListOf<Task>()
    tasks.add(
        Task(
            "Amanda Ruth's 1",
            "",
            Day(3, 2, 3, 1990),
            1,
            TimeTask(1, 0, 2, 50)
        )
    )
    tasks.add(
        Task(
            "Amanda Ruth's 2",
            "",
            Day(3, 2, 3, 1990),
            2,
            TimeTask(3, 0, 4, 0)
        )
    )
    tasks.add(
        Task(
            "Amanda Ruth's 3",
            "",
            Day(3, 2, 3, 1990),
            3,
            TimeTask(5, 30, 6, 40)
        )
    )

    TodoTheme {
        val rememberScrollState = rememberScrollState()
        EventLayout(
            modifier = Modifier
                .verticalScroll(rememberScrollState), tasks = tasks
        )

    }


}

