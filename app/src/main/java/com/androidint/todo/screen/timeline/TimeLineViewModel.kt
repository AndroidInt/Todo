package com.androidint.todo.screen.timeline

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidint.todo.repository.CategoryRepositoryImpl
import com.androidint.todo.repository.TaskRepositoryImpl
import com.androidint.todo.repository.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
    private var _monthName = mutableStateOf(calendar.monthName)
    val monthName: State<String> = _monthName
    private var _tasks = mutableStateListOf<Task>()
    val tasks: SnapshotStateList<Task> = _tasks




    fun previousMonth() {
        calendar
            .setGrgMonth(_month.value)
            .setGrgYear(_year.value)
            .setGrgDay(_day.value)
            .subMonth()

        _year.value = calendar.grgYear
        _month.value = calendar.grgMonth
        _day.value = calendar.grgDay
        _monthName.value = calendar.monthName

        viewModelScope.launch {

            taskRepositoryImpl.getAll().collect{ list ->
                _tasks.clear()
                _tasks.addAll( list.filter {
                    it.day.dayOfMonth == day.value && it.day.month == month.value && it.day.year == year.value
                }.toList())
            }

        }






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
        _monthName.value = calendar.monthName

        viewModelScope.launch {

            taskRepositoryImpl.getAll().collect{ list ->
                _tasks.clear()
                _tasks.addAll( list.filter {
                    it.day.dayOfMonth == day.value && it.day.month == month.value && it.day.year == year.value
                }.toList())
            }

        }

    }
    fun setDay(dayOfMonth:Int) {
        calendar
            .setGrgMonth(_month.value)
            .setGrgYear(_year.value).grgDay = dayOfMonth
        _year.value = calendar.grgYear
        _month.value = calendar.grgMonth
        _day.value = calendar.grgDay


        viewModelScope.launch {

            taskRepositoryImpl.getAll().collect{ list ->
                _tasks.clear()
                _tasks.addAll( list.filter {
                    it.day.dayOfMonth == day.value && it.day.month == month.value && it.day.year == year.value
                }.toList())
            }

        }

    }


}