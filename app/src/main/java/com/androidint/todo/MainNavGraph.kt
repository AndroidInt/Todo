package com.androidint.todo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.androidint.todo.screen.addtask.AddTaskScreen
import com.androidint.todo.screen.addtask.AddTaskViewModel
import com.androidint.todo.screen.mainPage.MainPageCompose
import com.androidint.todo.screen.mainPage.MainPageViewModel
import com.androidint.todo.screen.timeline.TimeLineScreen
import com.androidint.todo.screen.timeline.TimeLineViewModel
import com.androidint.todo.utils.Screens

@Composable
fun MainNavGraph (navHostController : NavHostController) {

    NavHost(navController = navHostController, startDestination = Screens.main_page ){

        composable(route = Screens.main_page){
            val viewModel = hiltViewModel<MainPageViewModel>()
            MainPageCompose(
                navHostController,
                viewModel
            )
        }

        composable(route = Screens.add_task){
            val viewModelAddTask = hiltViewModel<AddTaskViewModel>()
                    AddTaskScreen(
            conflictedDurationState = viewModelAddTask.conflictedDurationState,
            conflictedCategoryState = viewModelAddTask.conflictedCategoryState,
            categoryList = viewModelAddTask.categoryList,
            updateRequest = viewModelAddTask.updateRequest,
            addTask = viewModelAddTask::addTask,
            updateTask = viewModelAddTask::updateTask,
            successfullyDone = viewModelAddTask.successfullyDone,
            onSuccessfullyDone = viewModelAddTask::onSuccessfullyDone
        )

        }

        composable(route = Screens.timeline){
            val viewModelTimeLineTask = hiltViewModel<TimeLineViewModel>()
                    TimeLineScreen(
            month = viewModelTimeLineTask.month,
            previousMonth = viewModelTimeLineTask::previousMonth,
            nextMonth = viewModelTimeLineTask::nextMonth,
            year = viewModelTimeLineTask.year,
            day = viewModelTimeLineTask.day,
            setDay = viewModelTimeLineTask::setDay,
            monthName = viewModelTimeLineTask.monthName,
            tasks = viewModelTimeLineTask.tasks,
            categories = viewModelTimeLineTask.categories

        )

        }


    }
}