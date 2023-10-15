package com.androidint.todo.screen.timeline

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import saman.zamani.persiandate.PersianDate

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue

import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Day

import com.androidint.todo.repository.model.Task

@Composable
fun TimeLineScreen(
    month: State<Int>,
    previousMonth: () -> Unit,
    nextMonth: () -> Unit,
    year: State<Int>,
    day: State<Int>,
    setDay: (Int) -> Unit,
    monthName: State<String>,
    tasks: SnapshotStateList<Task>,
    categories : SnapshotStateList<Category>

) {
    Column(Modifier.fillMaxWidth()) {

        val calendar = PersianDate()


        val dayOfWeek = listOf(
            "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
        )
        val weeksOfMonth = mutableListOf<String>()
        calendar.grgMonthName

        repeat(
            calendar
                .setGrgYear(year.value)
                .setGrgMonth(month.value)
                .setGrgDay(1)
                .grgMonthLength
        ) {
            val weekDay = if(calendar.dayOfWeek()-2 < 0) calendar.dayOfWeek()-2+7 else calendar.dayOfWeek()-2
            weeksOfMonth.add(dayOfWeek[weekDay])
            calendar.addDay()
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
                previousMonth()


            }

            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "previous month"
                )

            }
            Text(
                text = "${
                    monthName.value
                }  ${year.value}"
            )

            IconButton(onClick = {
                nextMonth()


            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "next month"
                )
            }
        }
        val lazyListState = rememberLazyListState()
        LazyRow(state = lazyListState, modifier = Modifier.fillMaxWidth()) {
            items(weeksOfMonth.size) { it ->


                if (lazyListState.isScrollingForward()) {

                    LaunchedEffect(key1 = !lazyListState.isScrollInProgress) {

                        lazyListState.animateScrollToItem(lazyListState.firstVisibleItemIndex)

                    }
                }



//TODO(" it needs to scroll smoothly when scrolling to next item ")
//                    if (!lazyListState.isScrollingForward()) {
//                        LaunchedEffect(key1 = !lazyListState.isScrollInProgress) {
//                            lazyListState.animateScrollToItem(
//
//                            )
//
//                        }
//                    }


                DayComponent(
                    dayOfMonth = it + 1,
                    dayOfWeek = weeksOfMonth[it],
                    selected = day.value == it + 1
                ) {
                    setDay(it)
                }
            }

        }
        Spacer(modifier = Modifier.height(16.dp))

        val rememberScrollState = rememberScrollState()
        EventLayout(
            modifier = Modifier
                .verticalScroll(rememberScrollState),
            tasks = tasks.toList(),
            categories = categories,
            day = Day(
                calendar.setGrgYear(year.value).setGrgMonth(month.value).setGrgDay(day.value).dayOfWeek(),
                day.value,month.value,year.value
            )
        )
    }


}

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