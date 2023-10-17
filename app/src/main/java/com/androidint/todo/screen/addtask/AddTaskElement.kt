package com.androidint.todo.screen.addtask


import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.DayOfWeek

import com.androidint.todo.ui.theme.TodoTheme
import com.androidint.todo.utils.DataStore
import com.androidint.todo.utils.DataStore.Companion.categoryToColor
import com.androidint.todo.utils.DataStore.Companion.colorToCategoryGroup
import saman.zamani.persiandate.PersianDate
import kotlin.math.abs
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleInput(
    initTitle:String,
    onSetTitle: (String) -> Unit,
) {


    var title by remember { mutableStateOf(initTitle) }
    var confirmToNext by remember {
        if (initTitle.length > 4){
            mutableStateOf(true)
        }else{
            mutableStateOf(false)
        }
    }
    val maxCharacter = 25
    val focusManager = LocalFocusManager.current


    Card(modifier = Modifier
        .padding(8.dp)
        .imePadding()) {

        Column() {

            Row(modifier = Modifier.padding(8.dp)) {
                OutlinedTextField(

                    value = title,
                    placeholder = { Text(text = "write somethings ...") },
                    label = { Text(text = "Title") }, onValueChange = {
                        if (it.length <= maxCharacter) title = it
                        confirmToNext = title.length > 2

                    }, keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ), keyboardActions = KeyboardActions(onDone = {

                        focusManager.clearFocus()
                    }), singleLine = true, modifier = Modifier
                        .fillMaxWidth()
                )


            }

            Row(modifier = Modifier.padding(8.dp)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSetTitle(title) },
                    enabled = confirmToNext
                ) {
                    Text(text = "Next")
                }


            }
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionInput(
    initDescription:String,
    onSetDescription: (String) -> Unit,
    onBack: () -> Unit,

    ) {


    var description by remember { mutableStateOf(initDescription) }
    var confirmToNext by remember { mutableStateOf(true) }
    val maxCharacter = 40
    val focusManager = LocalFocusManager.current

    Card(modifier = Modifier.padding(8.dp)) {

        Column {

            Row(modifier = Modifier.padding(8.dp)) {

                OutlinedTextField(

                    value = description,
                    placeholder = { Text(text = "optional") },
                    label = { Text(text = "description") },
                    onValueChange = {
                        if (it.length <= maxCharacter) description = it

                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {

                        focusManager.clearFocus()

                    }),
                    singleLine = true,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()

                )


            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Button(modifier = Modifier.weight(0.9F),
                    onClick = { onBack() }) {
                    Text(text = "Back")

                }
                Spacer(modifier = Modifier.weight(0.05F))
                Button(
                    modifier = Modifier.weight(0.9F),
                    onClick = { onSetDescription(description) },
                    enabled = confirmToNext
                ) {
                    Text(text = "Next")

                }

            }
        }
    }


}

@Composable
fun CalendarTaskSet(
    modifier: Modifier = Modifier,
    initYear:Int,
    initMonth:Int,
    initDay:Int,
    startDayOfWeek: DayOfWeek = DayOfWeek.Monday,
    onSelectedDay: (year: Int, month: Int, day: Int) -> Unit,
    onBack: () -> Unit,
) {


    val calendar = PersianDate()

    var year by remember {
        mutableStateOf(initYear)
    }

    var month by remember {
        mutableStateOf(initMonth)

    }

    var day by remember {
        mutableStateOf(initDay)
    }
    val confirmToNext by remember {
        derivedStateOf {
            mutableStateOf(
                day != 0
            )
        }

    }

    val firstDayOfMonth by remember {
        derivedStateOf {
            mutableStateOf(
                calendar.setGrgYear(year).setGrgMonth(month)
                    .dayOfWeek() - calendar.setGrgMonth(month).grgDay.mod(7) - 1
            )
        }
    }

    val daysList = mutableListOf<Int?>()

    daysList.clear()
    if (firstDayOfMonth.value < 0) firstDayOfMonth.value += 7
    repeat(firstDayOfMonth.value) {
        daysList.add(null)
    }

    repeat(calendar.getGrgMonthLength(year, month)) {
        daysList.add(it + 1)
    }

    Card(modifier = modifier
        .padding(8.dp)
        .wrapContentHeight(unbounded = true)
        ) {
        Column(verticalArrangement = Arrangement.Top) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
//                    .weight(7F)

            ) {
                Column(
                    modifier = Modifier.weight(7F)
                ) {
                    Row {
                        //make static name header of week based on starting day of week
                        val daysWeek = mutableListOf(startDayOfWeek.toString().take(3))
                        var day = startDayOfWeek
                        repeat(6) {
                            day = day.next()
                            daysWeek.add(day.toString().take(3))
                        }
                        Row(horizontalArrangement = Arrangement.Start) {
                            daysWeek.forEach {
                                Box(
                                    modifier = Modifier.sizeIn(40.dp, 40.dp),
                                    contentAlignment = Alignment.Center,
                                ) {

                                    Text(text = it)


                                }
                            }
                        }

                    }

                    Column {
                        daysList.chunked(7).forEach { week ->

                            Row {
                                week.forEach {

                                    Box(
                                        modifier = Modifier
                                            .defaultMinSize(40.dp, 40.dp)
                                            .clickable {
                                                it?.let {
                                                    day = it
                                                }

                                            }
                                            .background(
                                                color = Color.Unspecified,
                                                shape = CircleShape
                                            )
                                            .border(
                                                2.dp,
                                                if (it != null && day == it) MaterialTheme.colorScheme.onBackground else Color.Unspecified,
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center,

                                        ) {
                                        it?.let {
                                            Text(text = it.toString())
                                        }

                                    }

                                }

                            }





                        }
                    }


                }



                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(2F)

                ) {

                    IconButton(onClick = {
                        val cal = PersianDate()
                            .setGrgMonth(month)
                            .setGrgYear(year)
                            .setGrgDay(day)
                            .addMonth()
                        year = cal.grgYear
                        month = cal.grgMonth
                        day = cal.grgDay
                    }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
                    }
                    Text(text = year.toString())
                    Text(text = calendar.getGrgMonthName(month).take(3))
                    IconButton(onClick = {
                        val cal = PersianDate()
                            .setGrgMonth(month)
                            .setGrgYear(year)
                            .setGrgDay(day)
                            .subMonth()

                        year = cal.grgYear
                        month = cal.grgMonth
                        day = cal.grgDay
                    }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
//                    .weight(2F)
            ) {
                Button(modifier = Modifier
                    .weight(0.7F)
                    .wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    border = BorderStroke(2.dp,MaterialTheme.colorScheme.onBackground)
                    ,
                    onClick = {
                        year = calendar.grgYear
                        month = calendar.grgMonth
                        day = calendar.grgDay
                    }) {
                    Text(text = "Today")

                }


                Button(modifier = Modifier
                    .weight(0.9F)
                    .wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    border = BorderStroke(2.dp,MaterialTheme.colorScheme.onBackground),
                    onClick = {
                        val cal = PersianDate()
                            .setGrgMonth(month)
                            .setGrgYear(year)
                            .setGrgDay(day)
                            .addWeek()
                        year = cal.grgYear
                        month = cal.grgMonth
                        day = cal.grgDay
                    }) {
                    Text(text = "Next week")

                }



                Button(
                    modifier = Modifier
                        .weight(0.9F)
                        .wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    border = BorderStroke(2.dp,MaterialTheme.colorScheme.onBackground),
                    onClick = {
                        val cal = PersianDate()
                            .setGrgMonth(month)
                            .setGrgYear(year)
                            .setGrgDay(day)
                            .addMonth()
                        year = cal.grgYear
                        month = cal.grgMonth
                        day = cal.grgDay
                    }
                ) {
                    Text(text = "Next month")
                }

            }
            Row(
                modifier = Modifier

                    .padding(8.dp)
                    .fillMaxWidth()
//                    .weight(2F)
            ) {
                Button(modifier = Modifier.weight(0.9F),
                    onClick = { onBack() }) {
                    Text(text = "Back")

                }
                Spacer(modifier = Modifier.weight(0.05F))
                Button(
                    modifier = Modifier.weight(0.9F),
                    onClick = { onSelectedDay(year, month, day) },
                    enabled = confirmToNext.value
                ) {
                    Text(text = "Next")
                }

            }

        }


    }


}

private fun numberToDigit(number : Int) = if (number<10) "0$number" else number.toString()
@ExperimentalFoundationApi
@Composable
fun ClockTaskSet(
    modifier: Modifier = Modifier,
    initStartHour: Int = 0,
    initStartMinute: Int = 0,
    initEndHour: Int = 0,
    initEndMinute: Int = 0,
    onBack: () -> Unit,
    onSetDuration: (
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int

    ) -> Unit

) {
    var minDuration = 10

    var startHour by remember {
        mutableStateOf(initStartHour)
    }
    var startMinute by remember {
        mutableStateOf(initStartMinute)
    }
    var endHour by remember {
        mutableStateOf(initEndHour)
    }
    var endMinute by remember {
        mutableStateOf(initEndMinute)
    }

    val confirmToNext by remember {
        derivedStateOf {
            (endHour.hours+endMinute.minutes).inWholeMinutes - (startHour.hours+startMinute.minutes).inWholeMinutes >= minDuration
        }

    }

    Card(
        modifier = modifier.padding(8.dp)

    ) {

        Row {
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
                    ClockComponent(startHour,startMinute) { hour, minute ->
                        startHour = hour
                        startMinute = minute



                    }
                    Text(text = "to")
                    ClockComponent(endHour,endMinute) { hour, minute ->
                        endHour = hour
                        endMinute = minute
                    }
                }

            }
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Button(modifier = Modifier.weight(0.9F),
                onClick = { onBack() }) {
                Text(text = "Back")

            }
            Spacer(modifier = Modifier.weight(0.05F))
            Button(
                modifier = Modifier.weight(0.9F),
                onClick = {
                    onSetDuration(startHour,startMinute,endHour,endMinute)
                          },
                enabled = confirmToNext
            ) {
                Text(text = "Next")
            }

        }


    }

}


@Composable
fun ClockComponent(initHour: Int,initMinute:Int,onSetClock: (hour: Int, minute: Int) -> Unit) {


    var hour by remember {
        mutableStateOf(initHour)
    }
    var minute by remember {
        mutableStateOf(initMinute)
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
                    count = hour, direction = direction,
                    onSetCounter = {
                        onSetClock(hour, minute)
                    }
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
                                        if (minute < 59) minute++
                                    }
                                }


                            }
                        )
                    }, contentAlignment = Alignment.Center
            ) {

                AnimatedCounter(
                    modifier = Modifier,
                    count = minute, direction = direction,
                    onSetCounter = {
                        onSetClock(hour, minute)
                    }
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
    onSetCounter: () -> Unit,
    direction: Boolean,
    modifier: Modifier = Modifier,
) {


    Row(modifier = modifier) {
        val countString = count.toString()

        AnimatedContent(
            targetState = countString,
            transitionSpec = {
                onSetCounter()
                if (direction) {
                    slideInVertically { it } with slideOutVertically { -it }
                } else {
                    slideInVertically { -it } with slideOutVertically { it }
                }

            },
            label = "",

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
            }
            .clearAndSetSemantics { contentDescription = "choose $color" }
        ,
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
fun ColorPicker(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onBack: () -> Unit,
    onSetCategory: (category: Category) -> Unit
) {
    val confirmToNext by remember{ mutableStateOf(true)}
    val colorState: MutableState<Color> = remember { mutableStateOf(Color.Yellow) }
    val categoryName by remember {
        derivedStateOf {
            var name  = ""
            categories.forEach {
                if (categoryToColor(it.color) == colorState.value)
                    name =  it.name
            }

            if (!categories.any { categoryToColor( it.color) == colorState.value})
                  name =   ""
            mutableStateOf(name)
        }
    }
    val name by remember {
        derivedStateOf {
            mutableStateOf(categoryName.value)
        }
    }


    Card(
        modifier = modifier
            .padding(8.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(4.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                for (color in DataStore.colors)
                    ColorCircle(
                        modifier = Modifier.weight(1F), color = color, colorState = colorState
                    )


            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), verticalAlignment = Alignment.Top
            ) {

                val maxCharacter = 25
                val focusManager = LocalFocusManager.current

                OutlinedTextField(
                    value = name.value,
                    placeholder = { Text(text = "write category's name") },
                    label = { Text(text = "Category") },
                    onValueChange = {if (it.length <= maxCharacter) name.value = it},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ), keyboardActions = KeyboardActions(onDone = {
//                        if (confirmToNext)
//                            onSetCategory(
//                                Category(
//                                    name = name.value,
//                                    color = colorToCategoryGroup(colorState.value)
//                                )
//                            )
                        focusManager.clearFocus()
                    }), singleLine = true, modifier = Modifier
                        .fillMaxWidth().semantics {
                                                  contentDescription = "input category name"
                        },
                    supportingText = {
                        Text(text = "Have just one category for each color ")
                    }
                )


            }


            Row {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Button(modifier = Modifier.weight(0.9F),
                        onClick = { onBack() }) {
                        Text(text = "Back")

                    }
                    Spacer(modifier = Modifier.weight(0.05F))
                    Button(
                        modifier = Modifier.weight(0.9F),
                        onClick = {
                            onSetCategory(
                                Category(
                                    name = name.value,
                                    color = colorToCategoryGroup(colorState.value)
                                )
                            )
                        },
                        enabled = confirmToNext
                    ) {
                        Text(text = "Next")
                    }

                }
            }


        }


    }


//    Card (
//        modifier = modifier
////            .requiredHeight(130.dp)
//            .padding(8.dp)
//    ){
//        //make data test for preview
//            val list = mutableListOf<Category>()
//            repeat(6){
//                list.add(Category("cat ${it+1}"))
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            Column(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//
//
//
//
//
//
//            ){
//                repeat(6){
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ){
//                        Text(text = list[it].name)
//                        IconButton(onClick = { /*TODO*/ }) {
//                            Icons.Default.Delete
//                        }
//                    }
//                }
//
//
//
//            }
//    }

}


@OptIn(ExperimentalFoundationApi::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun AddTaskPreview() {

    TodoTheme {
        Column(modifier = Modifier.fillMaxHeight()) {


//            TitleInput {
//
//            }
//
//            DescriptionInput({}) {
//
//            }


//            CalendarTaskSet(
//                initYear = 2023,
//                initMonth = 9,
//                initDay = 27,
//                onSelectedDay = { year, month, day ->
//
//                }) {
//
//            }
            val list = mutableListOf<Category>()
            repeat(5) {
                list.add(
                    Category("cat $it", it)
                )
            }

            ColorPicker(
                modifier = Modifier.wrapContentHeight(),
                categories = list, onBack = {},
                onSetCategory = {
                }
            )
//            ClockTaskSet(modifier = Modifier,
//                onBack = {},
//                onSetStartDuration = { _,_ ->},
//                onSetEndDuration = {_,_ ->},
//                confirmToSave = remember {mutableStateOf(false)},
//                saveData = {}
//
//
//
//
//                )


        }
    }
}





