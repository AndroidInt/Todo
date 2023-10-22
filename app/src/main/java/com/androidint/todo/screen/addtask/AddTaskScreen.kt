package com.androidint.todo.screen.addtask

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Day
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TimeTask
import com.androidint.todo.utils.DataStore.Companion.categoryToColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate


fun MutableList<MutableState<Boolean>>.next() {


    val trueIndex = this.filter {
        it.value
    }.map {
        indexOf(it)
    }.first()
    this[trueIndex].value = false
    CoroutineScope(Dispatchers.Main).launch {
        delay(500)
        if (trueIndex + 1 < this@next.size)
            this@next[trueIndex + 1].value = true
    }

}

fun MutableList<MutableState<Boolean>>.set(index: Int) {
    val trueIndex = this.filter {
        it.value
    }.map {
        indexOf(it)
    }.firstOrNull()
    trueIndex?.let {
        this@set[trueIndex].value = false
    }
    CoroutineScope(Dispatchers.Main).launch {
        trueIndex?.let {
            delay(500)
        }
        if (index in 0 until this@set.size) {
            this@set[index].value = true
        } else {
            throw IndexOutOfBoundsException("Index is out of bound")
        }
    }


}

fun MutableList<MutableState<Boolean>>.back() {

    val trueIndex = this.filter {
        it.value
    }.map {
        indexOf(it)
    }.first()
    this[trueIndex].value = false

    CoroutineScope(Dispatchers.Main).launch {
        delay(500)
        if (trueIndex > 0)
            this@back[trueIndex - 1].value = true
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddTaskScreen(

    conflictedDurationState: State<Boolean>,

    conflictedCategoryState: State<Boolean>,

    categoryList: SnapshotStateList<Category>,

    updateRequest: State<Boolean>,

    addTask: (task: Task, category: Category) -> Unit,

    updateTask: (task: Task, category: Category) -> Unit,
    successfullyDone: State<Boolean>,
    onSuccessfullyDone: () -> Unit,
    showSnackbar: (message: String, scope: CoroutineScope) -> Unit


) {
    val scope = rememberCoroutineScope()

    val lock = remember{
        mutableStateOf(false)
    }

    val titleState = remember {
        mutableStateOf(true)
    }
    val stack = mutableListOf<MutableState<Boolean>>()
    stack.add(titleState)

    val descriptionState = remember {
        mutableStateOf(false)
    }
    stack.add(descriptionState)
    val calendarState = remember {
        mutableStateOf(false)
    }
    stack.add(calendarState)
    val clockState = remember {
        mutableStateOf(false)
    }
    stack.add(clockState)

    val colorState = remember {
        mutableStateOf(false)
    }
    stack.add(colorState)

    val saveTaskState = remember {
        mutableStateOf(false)
    }
    stack.add(saveTaskState)


    // input values, all these states should be substituted with viewModel
    val calendar = PersianDate()
    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }
    var category: Category? by remember {
        mutableStateOf(

            if (categoryList.toList().isNotEmpty()) {
                Category(categoryList.toList()[0].name, categoryList.toList()[0].color)

            } else {
                Category("Inbox", 0)
            }

        )
    }


    val minDuration = 10
    val timeTask by remember {
        if ("${calendar.hour}${calendar.minute + minDuration}".toInt() < 2360 && calendar.minute + minDuration < 60) {
            mutableStateOf(
                TimeTask(
                    calendar.hour,
                    calendar.minute,
                    calendar.hour,
                    calendar.minute + minDuration
                )
            )
        } else {
            mutableStateOf(
                TimeTask(calendar.hour, calendar.minute, 23, 59)
            )
        }

    }

    val yearP = remember {
        mutableStateOf(
            calendar.grgYear
        )
    }
    val monthP = remember {
        mutableStateOf(
            calendar.grgMonth
        )
    }
    val dayP = remember {
        mutableStateOf(
            calendar.grgDay
        )
    }


    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F), verticalArrangement = Arrangement.Top
        ) {

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Title :", modifier = Modifier
                        .weight(1F)
                        .clickable {
                            if (!lock.value){
                                stack.set(0)
                            }
                        })
                    Text(text = title, modifier = Modifier.weight(1F))

                }
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Description :", modifier = Modifier
                        .weight(1F)
                        .clickable {
                            if (!lock.value){
                                stack.set(1)
                            }
                        })
                    Text(text = description, modifier = Modifier.weight(1F))

                }
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Date : ", modifier = Modifier
                        .weight(1F)
                        .clickable {
                            if (!lock.value){
                                stack.set(2)
                            }
                        })
                    Text(
                        text = "${yearP.value} / ${
                            calendar.getGrgMonthName(monthP.value).take(3)
                        } (${monthP.value}) / ${dayP.value}", modifier = Modifier.weight(1F)
                    )

                }
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Time : ", modifier = Modifier
                        .weight(1F)
                        .clickable {
                            if (!lock.value){
                                stack.set(3)
                            }
                        })
                    Row(modifier = Modifier.weight(1F)) {
                        Text(
                            text = "${if (timeTask.startHour < 10) "0${timeTask.startHour}" else "${timeTask.startHour}"} : ${if (timeTask.startMinute < 10) "0${timeTask.startMinute}" else "${timeTask.startMinute}"}",

                            )
                        Text(text = "  -  ")
                        Text(
                            text = "${if (timeTask.endHour < 10) "0${timeTask.endHour}" else "${timeTask.endHour}"} : ${if (timeTask.endMinute < 10) "0${timeTask.endMinute}" else "${timeTask.endMinute}"}",

                            )
                    }


                }
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Category : ", modifier = Modifier
                        .weight(1F)
                        .clickable {
                            if (!lock.value){
                                stack.set(4)
                            }
                        })
                    category?.let {
                        Row(
                            modifier = Modifier.weight(1F),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Canvas(modifier = Modifier) {
                                drawCircle(categoryToColor(it.color), radius = 4.dp.toPx())
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "${it.name} ")
                        }
                    }


                }
                if (conflictedCategoryState.value || conflictedDurationState.value) {
//                    Row(
//                        Modifier
//                            .padding(8.dp)
//                            .fillMaxWidth()
//                    ) {
//                        Text(text = "Error :", modifier = Modifier.weight(1F))
//                        Text(
//                            text = "there is a conflict in time or date!",
//                            modifier = Modifier.weight(1F),
//                            color = Color.Red
//                        )
//                    }
                }
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier.weight(0.9F),
                        onClick = {
                            stack.back()
                        },
                        enabled = saveTaskState.value
                    ) {
                        Text(text = "Edit")
                    }
                    Spacer(modifier = Modifier.weight(0.01F))
                    Button(
                        modifier = Modifier.weight(0.9F),
                        onClick = {
                            val task = Task(
                                title, description,
                                Day(
                                    calendar.setGrgDay(dayP.value)
                                        .setGrgMonth(monthP.value)
                                        .setGrgYear(yearP.value).dayOfWeek(),
                                    dayP.value,
                                    month = monthP.value,
                                    year = yearP.value
                                ),
                                0,
                                timeDuration = timeTask,
                                1,
                                done = false
                            )

                            if (!updateRequest.value) {

                                category?.let { addTask(task, it) }

                            } else {

                                category?.let { updateTask(task, it) }

                            }
                            if (!conflictedCategoryState.value && !conflictedDurationState.value) {
                                scope.launch {
                                    saveTaskState.value = false
                                    val job = async {
                                        lock.value = true
                                        showSnackbar("Your task has successfully added.", scope)
                                        lock.value = false
                                    }
                                    job.await()
                                    stack.set(0)
                                }

                                title = ""
                                description = ""
                                dayP.value = calendar.grgDay
                                monthP.value = calendar.grgMonth
                                yearP.value = calendar.grgYear

                            } else {
                                lock.value = true
                                showSnackbar("there is a conflict in time or date!", scope)
                                lock.value = false

                            }


                        },
                        enabled = saveTaskState.value && title.length > 2
                    ) {
                        Text(
                            text =
                            if (updateRequest.value) "Update Task" else "Add Task"
                        )
                    }

                }

            }


        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .weight(1F), verticalArrangement = Arrangement.Bottom
        ) {


            //input composable values
            val density = LocalDensity.current
            AnimatedVisibility(
                visible = titleState.value,
                enter = slideInVertically {
                    // Slide in from 40 dp from the bottom.
                    with(density) { 40.dp.roundToPx() }
                },
                exit = slideOutVertically {
                    with(density) { 40.dp.roundToPx() }
                } + fadeOut()

            ) {
                TitleInput(
                    initTitle = title,
                    onSetTitle = {
                        title = it
                        stack.next()
                    })
            }

            AnimatedVisibility(
                visible = descriptionState.value,
                enter = slideInVertically {
                    // Slide in from 40 dp from the bottom.
                    with(density) { 40.dp.roundToPx() }
                },
                exit = slideOutVertically {
                    with(density) { 40.dp.roundToPx() }
                } + fadeOut()

            ) {
                DescriptionInput(
                    initDescription = description,
                    onSetDescription = {
                        description = it
                        stack.next()
                    }) {
                    stack.back()
                }

            }

            AnimatedVisibility(
                visible = calendarState.value,
                enter = slideInVertically {
                    // Slide in from 40 dp from the bottom.
                    with(density) { 40.dp.roundToPx() }
                },
                exit = slideOutVertically {
                    with(density) { 40.dp.roundToPx() }
                } + fadeOut()

            ) {
                CalendarTaskSet(
                    initYear = yearP.value,
                    initMonth = monthP.value,
                    initDay = dayP.value,
                    onSelectedDay = { year, month, day ->
                        yearP.value = year
                        monthP.value = month
                        dayP.value = day
                        stack.next()
                    }) {
                    stack.back()
                }

            }

            AnimatedVisibility(
                visible = clockState.value,
                enter = slideInVertically {
                    // Slide in from 40 dp from the bottom.
                    with(density) { 40.dp.roundToPx() }
                },
                exit = slideOutVertically {
                    with(density) { 40.dp.roundToPx() }
                } + fadeOut()

            ) {
                ClockTaskSet(
                    initStartHour = timeTask.startHour,
                    initStartMinute = timeTask.startMinute,
                    initEndHour = timeTask.endHour,
                    initEndMinute = timeTask.endMinute,
                    onBack = { stack.back() },
                    onSetDuration = { startHour, startMinute, endHour, endMinute ->
                        timeTask.startHour = startHour
                        timeTask.startMinute = startMinute
                        timeTask.endHour = endHour
                        timeTask.endMinute = endMinute
                        stack.next()
                    }
                )

            }

            AnimatedVisibility(
                visible = colorState.value,
                enter = slideInVertically {
                    // Slide in from 40 dp from the bottom.
                    with(density) { 40.dp.roundToPx() }
                },
                exit = slideOutVertically {
                    with(density) { 40.dp.roundToPx() }
                } + fadeOut()

            ) {

                ColorPicker(

                    categories = categoryList.toList(),
                    onBack = { stack.back() },

                    onSetCategory = {
                        category = it
                        stack.next()
                    }
                )
            }

            if (successfullyDone.value) {

                onSuccessfullyDone()
            }


        }
    }


}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun ShowAddTaskScreen() {
//    AddTaskScreen()
}