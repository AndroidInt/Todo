package com.androidint.todo.screen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Canvas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable

import androidx.compose.foundation.gestures.detectVerticalDragGestures

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row


import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.layout.sizeIn

import androidx.compose.foundation.layout.wrapContentHeight

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp

import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp

import com.androidint.todo.ui.theme.TodoTheme
import com.androidint.todo.utils.DataStore
import saman.zamani.persiandate.PersianDate
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleInput(
    onTitleChange: (String) -> Unit, onNextLevelClick: () -> Unit
) {


    val title = remember { mutableStateOf("") }
    val iconVisibility = remember { mutableStateOf(false) }
    val maxCharacter = 25
    val focusManager = LocalFocusManager.current
    Row(modifier = Modifier.padding(8.dp)) {
        OutlinedTextField(trailingIcon = {
            if (iconVisibility.value) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "")
            }

        },

            value = title.value, label = { Text(text = "Title") }, onValueChange = {
                if (it.length <= maxCharacter) title.value = it
                onTitleChange(it)
                iconVisibility.value = title.value.length > 4

            }, keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ), keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onNextLevelClick()
            }), singleLine = true, modifier = Modifier.fillMaxWidth()
        )


    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionInput(
    onDescriptionChange: (String) -> Unit, onNextLevelClick: () -> Unit
) {


    val description = remember { mutableStateOf("") }
    val iconVisibility = remember { mutableStateOf(false) }
    val maxCharacter = 150
    val focusManager = LocalFocusManager.current


    Row(modifier = Modifier.padding(8.dp)) {

        OutlinedTextField(
            trailingIcon = {
                if (iconVisibility.value) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "")
                }


            },

            value = description.value,
            label = { Text(text = "description") },
            onValueChange = {
                if (it.length <= maxCharacter) description.value = it
                onDescriptionChange(it)
                iconVisibility.value = description.value.length > 4

            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onNextLevelClick()
            }),
            singleLine = true,
            maxLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )


    }


}

@Composable
fun CalendarTaskSet(modifier: Modifier = Modifier) {


    val calendar by remember {
        mutableStateOf(PersianDate())
    }

    var year by remember {
        mutableStateOf(calendar.grgYear)
    }

    var month by remember {
        mutableStateOf(calendar.grgMonth)

    }.also {
        if (it.value > 12) {
            it.value = 1
            year += 1
        }


        if (it.value < 1) {
            it.value = 12
            year -= 1

        }
    }

    val daysList = remember{
        mutableStateListOf<Int?>()
    }
    daysList.clear()
    var nullDay =calendar.setGrgMonth(month).dayOfWeek() - calendar.setGrgMonth(month).grgDay.mod(7)-1
    if (nullDay<0) nullDay +=7
    repeat(nullDay){
        daysList.add(null)
    }
    repeat(calendar.getGrgMonthLength(year,month)){
        daysList.add(it+1)
    }

    Card(modifier = modifier.padding(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)

        ) {
            LazyColumn(
                modifier = Modifier.weight(7F)
            ) {
                item {
                    val week = listOf( "Mon", "Tue", "Wed", "Thu", "Fri","Sat", "Sun")
                    LazyRow(horizontalArrangement = Arrangement.Start) {
                        items(week) {
                            Box(
                                modifier = Modifier.sizeIn(40.dp, 40.dp),
                                contentAlignment = Alignment.Center,
                            ) {

                                    Text(text = it)


                            }
                        }
                    }

                }

                items(


                      daysList.chunked(7)
                ) { week ->
                    LazyRow(horizontalArrangement = Arrangement.Start) {
                        items(week) {

                            Box(
                                modifier = Modifier.sizeIn(40.dp, 40.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                it?.let{
                                    Text(text = it.toString())
                                }

                            }
                        }
                    }
                }

            }



            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(2F)

            ) {

                IconButton(onClick = { month += 1 }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
                }
                Text(text = year.toString())
                Text(text = calendar.getGrgMonthName(month).take(3))
                IconButton(onClick = { month -= 1 }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                }
            }
        }

    }


}


@ExperimentalFoundationApi
@Composable
fun ClockTaskSet(modifier: Modifier = Modifier) {


    Card(
        modifier = modifier.padding(8.dp)

    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(minHeight = 80.dp), contentAlignment = Alignment.Center
        ) {

            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly

            ) {
                Text(text = "From")
                ClockComponent()
                Text(text = "to")
                ClockComponent()
            }

        }
    }

}


@Composable
fun ClockComponent() {


    var hour by remember {
        mutableStateOf(0)
    }
    var minute by remember {
        mutableStateOf(0)
    }
    var direction by remember {
        mutableStateOf(true)
    }

    val longSwipeChangeCount = 5
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {


        var columnSizeHour by remember {
            mutableStateOf(0F)
        }
        var columnSizeMinute by remember {
            mutableStateOf(0F)
        }


        Column(
            modifier = Modifier.onGloballyPositioned {
                columnSizeHour = it.size.height.toFloat()
            },

            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            var basicDelta = 0F


            Box(
                modifier = Modifier
                    .defaultMinSize(40.dp, 40.dp)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { _, delta ->

                                if (abs(delta) > columnSizeHour / 2) {
                                    if (delta < 0) {
                                        direction = true
                                        repeat(longSwipeChangeCount) {
                                            if (hour > 0) hour--
                                        }


                                    } else if (delta > 0) {
                                        direction = false
                                        repeat(longSwipeChangeCount) {
                                            if (hour < 23) hour++
                                        }

                                    }
                                }
                                basicDelta = delta


                            },
                            onDragEnd = {

                                if (abs(basicDelta) < columnSizeHour / 2) {
                                    if (basicDelta < 0) {
                                        direction = true
                                        if (hour > 0) hour--
                                    } else if (basicDelta > 0) {
                                        direction = false
                                        if (hour < 23) hour++
                                    }
                                }


                            }
                        )
                    }, contentAlignment = Alignment.Center
            ) {

                AnimatedCounter(
                    modifier = Modifier,
                    count = hour, direction = direction
                )


            }


        }

        Text(text = ":")
        Column(
            modifier = Modifier.onGloballyPositioned {
                columnSizeMinute = it.size.height.toFloat()
            },

            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            var basicDelta = 0F


            Box(
                modifier = Modifier
                    .defaultMinSize(40.dp, 40.dp)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { _, delta ->

                                if (abs(delta) > columnSizeHour / 2) {
                                    if (delta < 0) {
                                        direction = true
                                        repeat(longSwipeChangeCount) {
                                            if (minute > 0) minute--
                                        }


                                    } else if (delta > 0) {
                                        direction = false
                                        repeat(longSwipeChangeCount) {
                                            if (minute < 59) minute++
                                        }

                                    }
                                }
                                basicDelta = delta


                            },
                            onDragEnd = {

                                if (abs(basicDelta) < columnSizeHour / 2) {
                                    if (basicDelta < 0) {
                                        direction = true
                                        if (minute > 0) minute--
                                    } else if (basicDelta > 0) {
                                        direction = false
                                        if (minute < 23) minute++
                                    }
                                }


                            }
                        )
                    }, contentAlignment = Alignment.Center
            ) {

                AnimatedCounter(
                    modifier = Modifier,
                    count = minute, direction = direction
                )


            }


        }


    }

}


/*

This composable might be review and alternate with AnimatedCounter in future.

@Composable
fun AnimateCounter(
    modifier: Modifier,
    list: List<Int>
) {

    var minSizeOfLazyColumn by remember {
        mutableStateOf(0)

    }
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally, state = listState,
        verticalArrangement = Arrangement.Center


    ) {


        items(
            list

        ) {


            LaunchedEffect(key1 = !listState.isScrollInProgress) {


                listState.animateScrollToItem(listState.firstVisibleItemIndex)


            }



            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                if (it == remember { derivedStateOf { listState.firstVisibleItemIndex } }.value) {
                    Text(text = it.toString())
                } else {
                    Text(text = it.toString(), modifier.height(0.dp))
                }


            }

        }


    }
}
*/

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCounter(
    count: Int,
    direction: Boolean,
    modifier: Modifier = Modifier,
) {


    Row(modifier = modifier) {
        val countString = count.toString()

        AnimatedContent(
            targetState = countString,
            transitionSpec = {
                if (direction) {
                    slideInVertically { it } with slideOutVertically { -it }
                } else {
                    slideInVertically { -it } with slideOutVertically { it }
                }

            }, label = ""
        ) { char ->
            Text(
                text = if (char.length == 1) "0$char" else char,
                softWrap = false,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}


@Composable
fun ColorCircle(
    modifier: Modifier = Modifier,
    color: Color,
    colorState: MutableState<Color>
) {

    Box(
        modifier = modifier
            .clickable {
                colorState.value = color
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .padding(2.dp)
                .size(34.dp, 34.dp)
        ) {
            drawCircle(color)
        }
        if (colorState.value == color)
            Icon(imageVector = Icons.Default.Done, contentDescription = "")

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPicker(modifier: Modifier = Modifier) {

    val colorState: MutableState<Color> =
        remember { mutableStateOf(Color.Yellow) }
    Card(
        modifier = modifier
            .requiredHeight(130.dp)
            .padding(8.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(2F)
                .padding(4.dp), verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Row(
                modifier = Modifier.weight(1F),
                verticalAlignment = Alignment.CenterVertically
            ) {

                for (color in DataStore.colors)
                    ColorCircle(
                        modifier = Modifier.weight(1F), color = color, colorState = colorState
                    )


            }
            Row(
                modifier = Modifier
                    .weight(2F)
                    .padding(8.dp), verticalAlignment = Alignment.Top
            ) {
                val category = remember { mutableStateOf("") }
                val iconVisibility = remember { mutableStateOf(false) }
                val maxCharacter = 25
                val focusManager = LocalFocusManager.current

                OutlinedTextField(
                    value = category.value, label = { Text(text = "Category") },
                    onValueChange = {
                        if (it.length <= maxCharacter) category.value = it
                        iconVisibility.value = category.value.length > 4

                    }, keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ), keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()

                    }), singleLine = true, modifier = Modifier.fillMaxWidth()
                )

            }


        }


    }

}


@OptIn(ExperimentalFoundationApi::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun AddTaskPreview() {

    TodoTheme {
        Column(modifier = Modifier.fillMaxHeight()) {


            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentHeight()
            ) {
                TitleInput({ }) {

                }

                DescriptionInput({}) {

                }


            }
            CalendarTaskSet(modifier = Modifier.wrapContentHeight())
            ColorPicker(modifier = Modifier.wrapContentHeight())
            ClockTaskSet(modifier = Modifier)


        }
    }
}





