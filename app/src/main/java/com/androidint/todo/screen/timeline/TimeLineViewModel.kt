package com.androidint.todo.screen.timeline

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidint.todo.repository.CategoryRepositoryImpl
import com.androidint.todo.repository.TaskRepositoryImpl
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

/*
   TODO:
   month: State<Int>,--------
   previousMonth: () -> Unit,-------
   nextMonth: () -> Unit,--------
   year: State<Int>,---------
   day: State<Int>,---------
   setDay: (Int) -> Unit,-------
   monthName: State<String>,
   tasks: SnapshotStateList<Task>

    */

@HiltViewModel
class TimeLineViewModel @Inject constructor(
    private val taskRepositoryImpl: TaskRepositoryImpl,
    private val categoryRepositoryImpl: CategoryRepositoryImpl
) : ViewModel() {

    private val calendar = PersianDate()
    private var _month = mutableStateOf(calendar.grgMonth)
    val month: State<Int> = _month
    private var _year = mutableStateOf(calendar.grgYear)
    val year: State<Int> = _year
    private var _day = mutableStateOf(calendar.grgDay)
    val day: State<Int> = _day
    private var _monthName = mutableStateOf(calendar.grgMonthName)
    val monthName: State<String> = _monthName
    private var _tasks = mutableStateListOf<Task>()
    val tasks: SnapshotStateList<Task> = _tasks
    private var _categories = mutableStateListOf<Category>()
    val categories: SnapshotStateList<Category> = _categories

    init {
        viewModelScope.launch() {

                categoryRepositoryImpl.getAll().collect{
                    _categories.addAll(it)
                }

        }
        setTasksToday()
    }



    fun previousMonth() {
        calendar
            .setGrgMonth(_month.value)
            .setGrgYear(_year.value)
            .setGrgDay(_day.value)
            .subMonth()

        _year.value = calendar.grgYear
        _month.value = calendar.grgMonth
        _day.value = calendar.grgDay
        _monthName.value = calendar.grgMonthName

        setTasksToday()
    }
    fun nextMonth() {
        calendar
            .setGrgMonth(_month.value)
            .setGrgYear(_year.value)
            .setGrgDay(_day.value)
            .addMonth()

        _year.value = calendar.grgYear
        _month.value = calendar.grgMonth
        _day.value = calendar.grgDay
        _monthName.value = calendar.grgMonthName

        setTasksToday()

    }
    fun setDay(dayOfMonth:Int) {
        calendar
            .setGrgMonth(_month.value)
            .setGrgYear(_year.value).grgDay = dayOfMonth
        _year.value = calendar.grgYear
        _month.value = calendar.grgMonth
        _day.value = calendar.grgDay

        setTasksToday()
    }

    private fun setTasksToday(){
        viewModelScope.launch {
            _tasks.clear()
            taskRepositoryImpl.getTaskByDate(day = day.value,month = month.value,year = year.value).collect{
                _tasks.addAll(it)
            }
        }
    }


}