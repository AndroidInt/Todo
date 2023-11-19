package com.androidint.todo.screen.addtask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Day
import com.androidint.todo.repository.model.Tag
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TimeTask
import saman.zamani.persiandate.PersianDate

sealed class TaskState{
    class Success(val id :Int): TaskState()
    object Idle: TaskState()
    object Proccessing: TaskState()

    class  Error( val message:String?) : TaskState()
}


@Composable
fun TaskElementScreen(
    task: State<Task?>,
//    title: State<String> = remember { mutableStateOf("") },
//    description: State<String> = remember { mutableStateOf("") },
//    duration: State<TimeTask> = remember { mutableStateOf(TimeTask()) },
//    year: State<Int> = remember { mutableStateOf(PersianDate().grgYear) },
//    month: State<Int> = remember { mutableStateOf(PersianDate().grgMonth) },
//    day: State<Int> = remember { mutableStateOf(PersianDate().grgDay) },
    category: State<Category> = remember { mutableStateOf(Category()) },
    setCategory: (category: Category)-> Unit = {},
    categories: SnapshotStateList<Category> = remember { mutableStateListOf() },
    addTask: (task: Task, tags: List<Tag>,category: Category) -> Unit = {_,_,_ -> },
    updateTask: (task: Task, tags: List<Tag>,category: Category) -> Unit = {_,_,_ -> },
    addTaskState: State<Boolean> = remember { mutableStateOf(true) },
    tagList: SnapshotStateList<Tag> = remember { mutableStateListOf() },
    onPushTaskState: State<TaskState>
) {
    val date = PersianDate()
    val initTitle = remember {
        mutableStateOf(
            if (addTaskState.value) {
                ""
            } else {
                task.value!!.title
            }
        )
    }
    val initDescription = remember {
        mutableStateOf(
            if (addTaskState.value) {
                ""
            } else {
                task.value!!.description.toString()
            }
        )
    }
    val initDuration = remember {
        mutableStateOf(
            if (addTaskState.value) {
                TimeTask(0, 0, 0, 0)
            } else {
                task.value!!.timeDuration
            }
        )
    }
    val initYear = remember {
        mutableStateOf(
            if (addTaskState.value) {
                date.grgYear
            } else {
                task.value!!.day.year
            }
        )
    }
    val initMonth = remember {
        mutableStateOf(
            if (addTaskState.value) {
                date.grgMonth
            } else {
                task.value!!.day.month
            }
        )
    }
    val initDay = remember {
        mutableStateOf(
            if (addTaskState.value) {
                date.grgDay
            } else {
                task.value!!.day.dayOfMonth
            }
        )
    }
    val initDayOfWeek = remember {
        mutableStateOf(
            if (addTaskState.value) {
                var dayOfWeek = date.dayOfWeek() - date.grgDay.mod(7) - 1
                if (dayOfWeek < 0) dayOfWeek += 7
                dayOfWeek
            } else {
                task.value!!.day.dayOfWeek
            }
        )
    }
    val initCategory = remember {
        mutableStateOf(
            if (addTaskState.value) {
                Category()
            } else {
                category.value
            }
        )
    }

    val initTagList =
        if (tagList.isNotEmpty()){
            remember {
                mutableStateListOf<Tag>()
            }.also {
                it.addAll(tagList.toList())
            }
        }else{
            remember {
                mutableStateListOf<Tag>()
            }
        }

    val buttonHeight = remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier

                .verticalScroll(rememberScrollState())
        ) {

            TitleInputV2(
                initTitle
            )
            Spacer(modifier = Modifier.height(8.dp))
            DescriptionInputV2(
                initDescription
            )
            Spacer(modifier = Modifier.height(8.dp))
            Clock(
                initDuration.value.startHour,
                initDuration.value.startMinute,
                initDuration.value.endHour,
                initDuration.value.endMinute,
                onSetDuration = { sh, sm, eh, em ->
                    val timeTask = TimeTask(sh,sm,eh,em)
                    initDuration.value = timeTask
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            CalendarV2(
                initYear, initMonth, initDay,
                onSelectDate = { selectDayOfWeek, selectDay, selectMonth, selectYear ->
                    initDayOfWeek.value = selectDayOfWeek
                    initDay.value = selectDay
                    initMonth.value = selectMonth
                    initYear.value = selectYear
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            ColorV2(
                categories = categories.toList(),
                onSetCategory = {
                    initCategory.value = it
                    setCategory(it)
                },
                category = category.value
            )
            Spacer(modifier = Modifier.height(8.dp))
            AddTag(initTagList, setTag = {
                initTagList.add(it)
            })

            Spacer(modifier = Modifier.height(8.dp))
            Notify()

            Spacer(modifier = Modifier.height(buttonHeight.value.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(onClick = {
                val task = Task(
                    initTitle.value, initDescription.value,
                    Day(
                        dayOfWeek = initDayOfWeek.value,
                        dayOfMonth = initDay.value,
                        month = initMonth.value,
                        year = initYear.value
                    ),
                    if (initCategory.value.categoryId == null) 0 else initCategory.value.categoryId!!,
                    timeDuration = initDuration.value,
                    1,
                    done = false
                )
                if (addTaskState.value) {
                    addTask(task,initTagList.toList(),initCategory.value)
                } else {
                    updateTask(task,initTagList.toList(),initCategory.value)
                }
            },
                modifier = Modifier
                    .onGloballyPositioned {
                        buttonHeight.value = it.size.height / 2
                    }
                    .fillMaxWidth(),
                enabled = initTitle.value.isNotEmpty() && initDuration.value.eventDuration() > 9 && initCategory.value.name.isNotEmpty()

            ) {
                Text(text =
                if (addTaskState.value){
                    "Add task"
                }else{
                    "Update Task"
                }
                )
            }
        }
    }


}

@Preview
@Composable
fun ShowTaskElementScreen() {
    var list = remember {
        mutableStateListOf<Category>()
    }
    val cat = Category("AOSP",3)
    list.add(cat)
    TaskElementScreen(
        task = remember {
            mutableStateOf(null)
        },
        addTaskState = remember { mutableStateOf(true) },
        categories = list,
//        category = remember{ mutableStateOf(cat)}
        onPushTaskState =  remember{ mutableStateOf(TaskState.Error(""))}
    )
}