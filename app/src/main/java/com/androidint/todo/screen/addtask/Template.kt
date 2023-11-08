package com.androidint.todo.screen.addtask


import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.androidint.todo.R
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.DayOfWeek
import com.androidint.todo.repository.model.TimeTask
import com.androidint.todo.ui.theme.TodoTheme
import com.androidint.todo.utils.DataStore
import saman.zamani.persiandate.PersianDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleInputV2(
    title: MutableState<String>,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 24.dp
        )
    ) {
        Box(
            modifier = Modifier.wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.baseline_short_text_24),
                            contentDescription = "",
                        )
                    }

                }


                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(text = "Task title *")
                    },
                    value = title.value,
                    onValueChange = {
                        if (it.length <= 25) title.value = it
                    },
                    modifier = Modifier.background(Color.Transparent),
                    singleLine = true
                )


            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionInputV2(
    description: MutableState<String>
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 24.dp
        )
    ) {
        Box(
            modifier = Modifier.wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.baseline_edit_note_24),
                            contentDescription = "",
                        )
                    }

                }


                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(text = "Add description")
                    },
                    value = description.value,
                    onValueChange = {
                        if (it.length <= 25) description.value = it
                    },
                    modifier = Modifier.background(Color.Transparent),
                    singleLine = true
                )


            }
        }

    }
}


@Composable
fun CalendarV2(
    year: MutableState<Int>,
    month: MutableState<Int>,
    day: MutableState<Int>,
    startDayOfWeek: DayOfWeek = DayOfWeek.Monday,
) {
    val showCalendar = remember {
        mutableStateOf(false)
    }
    val date = PersianDate()
    var monthName = remember {
        mutableStateOf(date.grgMonthName)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showCalendar.value = !showCalendar.value
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 24.dp
        )
    ) {
        Column {
            Row(
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.baseline_calendar_month_24),
                        contentDescription = ""
                    )
                }
                Text(
                    text = "${date.dayEnglishName()} " +
                            "${day.value} " +
                            "${monthName.value.take(3)} " +
                            "${date.grgYear} ", modifier = Modifier.padding(16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showCalendar.value = !showCalendar.value }) {
                        Icon(
                            imageVector = if (showCalendar.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = showCalendar.value,
            enter = expandVertically(),
            exit = shrinkVertically(
                animationSpec = tween(1000)
            )
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )
            Column {
                CalendarTaskSetV2(
                    initYear = date.grgYear,
                    initMonth = date.grgMonth,
                    initDay = date.grgDay,
                    onSelectedDay = { selectYear, selectMonth, selectDay ->
                        date.setGrgYear(selectYear)
                            .setGrgMonth(selectMonth)
                            .grgDay = selectDay
                        year.value = date.grgYear
                        month.value = date.grgMonth
                        monthName.value = date.grgMonthName
                        day.value = date.grgDay
//                        showCalendar.value = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Clock(
    initStartHour: Int = 0,
    initStartMinute: Int = 0,
    initEndHour: Int = 0,
    initEndMinute: Int = 0,
    onSetDuration: (
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int
    ) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 24.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_access_time_24),
                    contentDescription = ""
                )
            }
            ClockTaskSetV2(
                Modifier,
                initStartHour,
                initStartMinute,
                initEndHour,
                initEndMinute,
                onSetDuration = { sh, sm, eh, em ->
                    onSetDuration(sh, sm, eh, em)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorV2(
    categories: List<Category>,
    onSetCategory: (category: Category) -> Unit
) {
    val showColorPallet = remember {
        mutableStateOf(false)
    }
    val colorState = remember {
        mutableStateOf(DataStore.colorsV2[0])
    }
    val categoryName by remember {
        derivedStateOf {
            var name = ""
            categories.forEach {
                if (DataStore.colorsV2[it.color] == colorState.value)
                    name = it.name
            }
            if (!categories.any { DataStore.colorsV2[it.color] == colorState.value })
                name = ""
            mutableStateOf(name)
        }
    }
    val name by remember {
        derivedStateOf {
            mutableStateOf(categoryName.value)
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 24.dp
        )
    ) {
        Column {
            Row(
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
//                        modifier = Modifier.padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.baseline_color_lens_24),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(
                            colorState.value
                        ),
                    )
                }


                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(text = "Category name *")
                    },
                    value = name.value,
                    onValueChange = {
                        if (it.length <= 25) name.value = it
                    },
                    modifier = Modifier
                        .background(Color.Transparent)
                        .widthIn(1.dp, Dp.Infinity),
                    singleLine = true
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showColorPallet.value = !showColorPallet.value }) {
                        Icon(
                            imageVector = if (showColorPallet.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = ""
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = showColorPallet.value,
                enter = expandVertically(),
                exit = shrinkVertically(
                    animationSpec = tween(1000)
                )
            ) {
                Column {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                    Column(
                        modifier = Modifier
                            .padding(4.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DataStore.colorsV2.chunked(6).forEach {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                for (color in it)
                                    ColorCircleV2(
                                        modifier = Modifier.weight(1F),
                                        color = color,
                                        colorState = colorState
                                    )
                            }
                        }
                    }
                }
            }
            onSetCategory(Category(name.value, DataStore.colorsV2.indexOf(colorState.value)))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTag(
    tagList: SnapshotStateList<String>,
) {
    var tag = remember {
        mutableStateOf("")
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 24.dp
        )
    ) {
        Box(
            modifier = Modifier.wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.baseline_tag_24),
                                contentDescription = "",
                            )
                        }

                    }
                    TextField(
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        placeholder = {
                            Text(text = "Tags")
                        },
                        value = tag.value,
                        onValueChange = {
                            if (
                                Regex("^[a-zA-Z0-9_]*\$").containsMatchIn(it)
                                &&
                                it.length <= 15
                            ) {
                                tag.value = it
                            }
                        },
                        modifier = Modifier
                            .widthIn(1.dp, Dp.Infinity)
                            .background(Color.Transparent),
                        singleLine = true
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (tag.value.length in 1..15 && !tagList.contains(tag.value)) {
                                    tagList.add(tag.value)
                                }
                                tag.value = ""
                            }, enabled = tagList.size < 5
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = ""
                            )
                        }
                    }
                }
                if (tagList.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.padding(start = 16.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Divider(Modifier.height(1.dp))
                        tagList.forEach {
                            InputChip(selected = true, onClick = {
                                tagList.remove(it)
                            },
                                label = {
                                    Text(text = it, style = MaterialTheme.typography.bodySmall)
                                },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Localized description",
                                        Modifier.size(InputChipDefaults.IconSize)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Notify() {

    val check = remember {
        mutableStateOf(false)
    }
    val hourBefore = remember {
        mutableStateOf(0)
    }
    val minuteBefore = remember {
        mutableStateOf(0)
    }


    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 24.dp
        )
    ) {
        Column {
            Row(
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.baseline_notifications_none_24),
                        contentDescription = ""
                    )
                }
                Text(
                    text = "Notify me", modifier = Modifier.padding(16.dp)
                )
                if (check.value){
                    ClockComponentV2(initHour = 0, initMinute = 0, onSetClock = { hour, minute ->
                        minuteBefore.value = minute
                        hourBefore.value = hour
                    })
                }else{
                    Text(text = "00 : 00")
                }


                Text(
                    text = "before", modifier = Modifier.padding(16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(checked = check.value, onCheckedChange = {
                        check.value = !check.value
                    },
                        thumbContent = {
                            if (check.value) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            } else {
                                null
                            }
                        }

                    )
                }
            }

        }
    }
}

enum class ERepeat {
    None,
    Daily,
    Weekly,
    Monthly
}

@Composable
fun RepeatTask() {
    val repeat = remember {
        mutableStateOf(false)
    }
    val repeatType = remember {
        mutableStateOf(ERepeat.None)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 24.dp
        )
    ) {
        Column {
            Row(
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.baseline_event_repeat_24),
                        contentDescription = ""
                    )
                }
                Text(

                    text =if(repeatType.value == ERepeat.None){
                        "Don't repeat"
                    }else{
                        "Repeat the task ${repeatType.value}"
                    },

                    modifier = Modifier.padding(16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(checked = repeat.value, onCheckedChange = {
                        repeat.value = !repeat.value
                        if (!repeat.value){
                            repeatType.value = ERepeat.None
                        }else{
                            repeatType.value = ERepeat.Monthly
                        }
                    },
                        thumbContent = {
                            if (repeat.value) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            } else {
                                null
                            }
                        }

                    )
                }
            }

            AnimatedVisibility(
                visible = repeat.value,
                enter = expandVertically(),
                exit = shrinkVertically(
                    animationSpec = tween(1000)
                )
            ) {
                Divider(
                    modifier = Modifier.height(1.dp)
                )
                Row(
                    modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                repeatType.value = ERepeat.None
                                repeat.value = false
                            }
                    ) {
                        Text(text = "None")
                    }
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                repeatType.value = ERepeat.Daily
                            }
                    ) {
                        Text(text = "Daily")
                    }
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                repeatType.value = ERepeat.Weekly
                            }
                    ) {
                        Text(text = "Weekly")
                    }
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                repeatType.value = ERepeat.Monthly
                            }
                    ) {
                        Text(text = "Monthly")
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun ShowEditText() {

    val date = PersianDate()
    var title = remember { mutableStateOf("") }
    var description = remember { mutableStateOf("") }
    var duration = remember { mutableStateOf(TimeTask()) }
    var year = remember { mutableStateOf(date.grgYear) }
    var month = remember { mutableStateOf(date.grgMonth) }
    var day = remember { mutableStateOf(date.grgDay) }
    var category = remember { mutableStateOf(Category()) }
    var tagList = remember {
        mutableStateListOf<String>()
    }

    var buttonHeight = remember {
        mutableStateOf(0)
    }
    TodoTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier

                    .verticalScroll(rememberScrollState())
            ) {

                TitleInputV2(
                    title
                )
                Spacer(modifier = Modifier.height(8.dp))
                DescriptionInputV2(
                    description
                )
                Spacer(modifier = Modifier.height(8.dp))
                Clock(
                    duration.value.startHour,
                    duration.value.startMinute,
                    duration.value.endHour,
                    duration.value.endMinute,
                    onSetDuration = { sh, sm, eh, em ->
                        duration.value.startHour = sh
                        duration.value.startMinute = sm
                        duration.value.endHour = eh
                        duration.value.endMinute = em
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                CalendarV2(
                    year, month, day
                )
                Spacer(modifier = Modifier.height(8.dp))
                val list = mutableListOf<Category>()

                ColorV2(
                    categories = list,
                    onSetCategory = {
                        category.value = it
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                AddTag(tagList)

                Spacer(modifier = Modifier.height(8.dp))
                Notify()

                Spacer(modifier = Modifier.height(8.dp))
                RepeatTask()

                Spacer(modifier = Modifier.height(buttonHeight.value.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(onClick = { /*TODO*/ }, modifier = Modifier
                    .onGloballyPositioned { it ->
                        buttonHeight.value = it.size.height / 2
                    }
                    .fillMaxWidth()) {
                    Text(text = "Add task")
                }
            }
        }

    }
}