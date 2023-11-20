package com.androidint.todo

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.androidint.todo.screen.addtask.AddTaskScreen
import com.androidint.todo.screen.addtask.AddTaskViewModel
import com.androidint.todo.screen.mainPage.MainPageCompose
import com.androidint.todo.screen.mainPage.MainPageViewModel
import com.androidint.todo.screen.timeline.TimeLineScreen
import com.androidint.todo.screen.timeline.TimeLineViewModel
import com.androidint.todo.utils.Screens
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainNavGraph(
    navHostController: NavHostController,
    showSnackBar: (message: String, scope: CoroutineScope) -> Unit
) {

    NavHost(navController = navHostController, startDestination = Screens.main_page) {

        composable(route = Screens.main_page) {
            val mainPageViewModel = hiltViewModel<MainPageViewModel>()
            MainPageCompose(
                navHostController,
                mainPageViewModel.tasks,
                mainPageViewModel.tasksWithCategory,
                mainPageViewModel::deleteTask,
                mainPageViewModel::doneCurrentTask
            )
        }

        composable(route = Screens.add_task+"/{taskId}",arguments = listOf(navArgument("taskId"){
            type= NavType.IntType
        })) {
            val viewModelAddTask = hiltViewModel<AddTaskViewModel>()
            AddTaskScreen(
                taskId = it.arguments?.getInt("taskId"),
                conflictedDurationState = viewModelAddTask.conflictedDurationState,
                conflictedCategoryState = viewModelAddTask.conflictedCategoryState,
                categoryList = viewModelAddTask.categoryList,
                updateRequest = viewModelAddTask.updateRequest,
                addTask = viewModelAddTask::addTask,
                updateTask = viewModelAddTask::updateTask,
                successfullyDone = viewModelAddTask.successfullyDone,
                onSuccessfullyDone = viewModelAddTask::onSuccessfullyDone,
                showSnackbar = showSnackBar
            )

        }

        composable(route = Screens.timeline) {
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