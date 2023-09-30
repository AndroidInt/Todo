package com.androidint.todo.screen.timeline


import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.androidint.todo.repository.model.Day
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TimeTask
import com.androidint.todo.ui.theme.TodoTheme
import com.androidint.todo.utils.DataStore
import saman.zamani.persiandate.PersianDate
import java.io.Serializable
import java.util.Calendar
import kotlin.math.roundToInt

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

@Composable
fun EventLayout(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    timeBar: @Composable (hour: Int, minute: Int) -> Unit = { hour, minute ->
        val hourHeight = 120.dp
        Box(
            modifier = Modifier
                .height(hourHeight)
                .bottomBorder(1.dp, MaterialTheme.colorScheme.onPrimary)
                .taskData(null, false)
        ) {
            BasicSidebarLabel(hour, minute)
        }
    },
    content: @Composable (task: Task) -> Unit = {
        BasicEvent(task = it)

    }
) {
    val width = LocalConfiguration.current.screenWidthDp
    val hourHeight = 120.dp
    val time = remember {
        mutableStateOf(
            Calendar.getInstance().get(Calendar.MINUTE)
                    + Calendar.getInstance().get(Calendar.HOUR) * 60
        )
    }

    Layout(
        modifier = modifier
            .fillMaxWidth(),
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

            val placeable = if (task == null && !dividerExist) {
                measurable.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth / 5,
                        minWidth = constraints.maxWidth / 5
                    )
                )
            } else if (task == null && dividerExist) {
                measurable.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth,
                        minWidth = constraints.maxWidth
                    )
                )
            } else {
                taskHeight?.let {
                    constraints.copy(
                        minHeight = it,
                        maxHeight = it,
                        maxWidth = 10 * constraints.maxWidth / 15,
                        minWidth = 10 * constraints.maxWidth / 15
                    )
                }
                    ?.let { measurable.measure(it) }!!
            }

            if (task == null) {
                Pair(placeable, null, dividerExist)
            } else {
                Pair(placeable, task, dividerExist)
            }

        }

        layout(
            constraints.maxWidth
//            constraints.maxWidth
//            placeableWithTasks.maxOfOrNull {
//            it.first.width
//        } ?: 0
            , height
        ) {


            var heightTimeBar = 0
            val paddingHour = 10
            val taskPadding = constraints.maxWidth / 5
            placeableWithTasks.forEach { (placeable, task, dividerExist) ->
                if (task == null) {

                    if (dividerExist) {
                        val offsetTime = ((time.value / 60f) * hourHeight.toPx()).roundToInt()
                        placeable.place(paddingHour + 10, offsetTime)
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
fun BasicEvent(
    modifier: Modifier = Modifier,
    task: Task,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
//            .padding(8.dp)
            .background(
                MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(0.03F)
                .fillMaxHeight(1F)
                .background(
                    //TODO("it must pass the task's color not ownerCategoryId")
                    DataStore.categoryToColor(task.ownerCategoryId)
                )
        )

        Spacer(modifier = Modifier.fillMaxWidth(0.05F))

        Column(Modifier.fillMaxWidth(0.92F)) {
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
//    Divider(modifier = Modifier)
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
fun ShowEventItem() {
    BasicEvent(Modifier,
    Task(
            "Amanda Ruth's 1",
            "",
            Day(3, 2, 3, 1990),
            1,
            TimeTask(1, 0, 2, 50)
        )
    )
}
//@Composable
//fun BasicEventPreview() {
//
//    val tasks = mutableListOf<Task>()
//    tasks.add(
//        Task(
//            "Amanda Ruth's 1",
//            "",
//            Day(3, 2, 3, 1990),
//            1,
//            TimeTask(1, 0, 2, 50)
//        )
//    )
//    tasks.add(
//        Task(
//            "Amanda Ruth's 2",
//            "",
//            Day(3, 2, 3, 1990),
//            2,
//            TimeTask(3, 0, 4, 0)
//        )
//    )
//    tasks.add(
//        Task(
//            "Amanda Ruth's 3",
//            "",
//            Day(3, 2, 3, 1990),
//            3,
//            TimeTask(5, 30, 6, 40)
//        )
//    )
//
//    val dayOfMonths = (1..31).toList()
////    val dayOfWeeks = listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")
//    val dayOfWeeks = dayOfMonths.map {
//        when (it.rem(7)) {
//            0 -> {
//                "Sat"
//            }
//
//            1 -> {
//                "Sun"
//            }
//
//            2 -> {
//                "Mon"
//            }
//
//            3 -> {
//                "Tue"
//            }
//
//            4 -> {
//                "Wed"
//            }
//
//            5 -> {
//                "Thu"
//            }
//
//            6 -> {
//                "Fri"
//            }
//
//
//            else -> {
//                Throwable("bad expression for dayOfWeek")
//            }
//        }
//
//    }.toList()
//
//
//    TodoTheme {
//        Column(Modifier.fillMaxWidth()) {
//
//            val calendar = PersianDate()
//
//            var selectDayState by remember {
//                //TODO("fetch current day from viewmodel")
//                mutableStateOf(calendar.grgDay)
//            }
//            var monthState by remember {
//                //TODO("fetch current day from viewmodel")
//                mutableStateOf(calendar.grgMonth)
//            }
//            var yearState by remember {
//                //TODO("fetch current day from viewmodel")
//                mutableStateOf(calendar.grgYear)
//            }
//            val dayOfWeek = listOf(
//                "Mon", "Tue", "Wed", "Thu", "Fri","Sat", "Sun"
//            )
//            val weeksOfMonth = mutableListOf<String>()
//            repeat(calendar
//                .setGrgYear(yearState)
//                .setGrgMonth(monthState)
//                .setGrgDay(1)
//                .grgMonthLength){
//                weeksOfMonth.add(dayOfWeek[calendar.dayOfWeek()])
//                calendar.addDay()
//            }
//
//
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                IconButton(onClick = {
//                    val cal = PersianDate()
//                        .setGrgMonth(monthState)
//                        .setGrgYear(yearState)
//                        .setGrgDay(selectDayState)
//                        .subMonth()
//                    yearState = cal.grgYear
//                    monthState = cal.grgMonth
//                    selectDayState = cal.grgDay
//
////                    selectDayState = cal.grgDay
//
//                }
//
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.KeyboardArrowLeft,
//                        contentDescription = "previous month"
//                    )
//
//                }
//                Text(
//                    text = "${
//                        calendar
//                            .setGrgYear(yearState)
//                            .setGrgMonth(monthState)
//                            .grgMonthName
//                    }  $yearState"
//                )
//
//                IconButton(onClick = {
//
//                    val cal = PersianDate()
//                        .setGrgMonth(monthState)
//                        .setGrgYear(yearState)
//                        .setGrgDay(selectDayState)
//                        .addMonth()
//                    yearState = cal.grgYear
//                    monthState = cal.grgMonth
//                    selectDayState = cal.grgDay
//
//                }) {
//                    Icon(
//                        imageVector = Icons.Default.KeyboardArrowRight,
//                        contentDescription = "next month"
//                    )
//                }
//            }
//
//            val lazyListState = rememberLazyListState()
//            LazyRow(state = lazyListState, modifier = Modifier.fillMaxWidth()) {
//                items(weeksOfMonth.size) { it ->
//
//
//                    if (lazyListState.isScrollingForward()) {
//
//                        LaunchedEffect(key1 = !lazyListState.isScrollInProgress) {
//
//                            lazyListState.animateScrollToItem(lazyListState.firstVisibleItemIndex)
//
//                        }
//                    }
////                    LaunchedEffect(key1 = monthState) {
////
////                        lazyListState.animateScrollToItem(selectDayState-1)
////
////                    }
//
//
//
////TODO(" it needs to scroll smoothly when scrolling to next item ")
////                    if (!lazyListState.isScrollingForward()) {
////                        LaunchedEffect(key1 = !lazyListState.isScrollInProgress) {
////                            lazyListState.animateScrollToItem(
////
////                            )
////
////                        }
////                    }
//
//
//                    DayComponent(
//                        dayOfMonth = it+1,
//                        dayOfWeek = weeksOfMonth[it],
//                        selected = selectDayState == it+1
//                    ) {
//                        selectDayState = it
//                    }
//                }
//
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//
//            val rememberScrollState = rememberScrollState()
//            EventLayout(
//                modifier = Modifier
//                    .verticalScroll(rememberScrollState), tasks = tasks
//            )
//        }
//
//
//    }
//
//
//}






@Composable
private fun LazyListState.isScrollingForward(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}


@Composable
fun DayComponent(
    modifier: Modifier = Modifier,
    dayOfMonth: Int,
    dayOfWeek: String,
    selected: Boolean,
    onClick: (Int) -> Unit
) {

    var interactionSource = remember { MutableInteractionSource() }



    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onPrimary
        )


    ) {
        Column(
            modifier
                .padding(8.dp)
                .clickable {
                    onClick(dayOfMonth)
                    interactionSource = interactionSource
                },
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(text = dayOfMonth.toString())
            Box(
                modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(text = dayOfWeek, modifier)
                Text(text = "Wed", modifier = modifier.alpha(0F))
            }

        }
    }


}
