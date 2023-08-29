package com.androidint.todo.screen


import android.content.res.Configuration
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.androidint.todo.repository.model.Day
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TimeTask
import com.androidint.todo.ui.theme.TodoTheme
import com.androidint.todo.utils.DataStore
import kotlin.math.roundToInt


@Composable
fun EventLayout(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    content: @Composable (task: Task) -> Unit = { BasicEvent(task = it) }
) {
    val hourHeight = 120.dp
    Layout(
        modifier = modifier,
        content = {
            tasks.sortedBy(Task::timeDuration).forEach { task ->
                Box(modifier = Modifier.taskData(task)) {
                    content(task)
                }
            }
        }
    )
    { measurables, constraints ->
        val height = hourHeight.roundToPx() * 24
        val placeableWithTasks = measurables.map { measurable ->
            val task = measurable.parentData as Task
            val taskHeight =
                ((task.timeDuration.eventDuration() / 60f) * hourHeight.toPx()).roundToInt()
            val placeable =
                measurable.measure(constraints.copy(minHeight = taskHeight, maxHeight = taskHeight))
            Pair(placeable, task)
        }
        layout(constraints.maxWidth, height) {
            placeableWithTasks.forEach { (placeable, task) ->
                val taskOffsetMinutes = task.timeDuration.offsetTimeToMinutes()
                val taskY = ((taskOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()
                placeable.place(0, taskY)
            }
        }
    }
}

private class TaskDataModifier(
    val task: Task,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = task
}

fun Modifier.taskData(task: Task) = this.then(TaskDataModifier(task))


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


/*
@Preview(showBackground = true)
@Composable
fun BasicSidebarLabelPreview() {
    TodoTheme {
        BasicSidebarLabel(10, 0, Modifier.sizeIn(64.dp, 64.dp, 64.dp, 120.dp))
    }
}
*/

@Composable
fun ScheduleSidebar(
    modifier: Modifier = Modifier,
    hourHeight: Dp = 120.dp,
    label: @Composable (hour: Int, minutes: Int) -> Unit = { hour, minutes ->
        BasicSidebarLabel(hour, minutes)
    }
) {
    Column(modifier = modifier) {
        repeat(24) { i ->
            Box(modifier = Modifier.height(hourHeight)) {
                label(i, 0)
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun ScheduleSidebarPreview() {
    TodoTheme {
        ScheduleSidebar()
    }
}

*/
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
        Row {
            ScheduleSidebar(
                modifier = Modifier
                    .weight(1F)
                    .verticalScroll(rememberScrollState)
            )
            EventLayout(
                modifier = Modifier
                    .weight(5F)
                    .verticalScroll(rememberScrollState), tasks = tasks
            )
        }
    }


}

