package com.androidint.todo

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.androidint.todo.screen.addtask.TaskElementScreen
import com.androidint.todo.screen.addtask.TaskElementViewModel
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
        composable(
            route = Screens.add_task+"?taskId={taskId}",
            arguments = listOf(navArgument("taskId")
            {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
            )
        ) {
            val viewModelAddTask = hiltViewModel<TaskElementViewModel>()
            it.arguments?.getString("taskId")?.let { taskId ->
                viewModelAddTask.initTask(taskId.toLong())
            }
//            val waitToFetchTask = viewModelAddTask.submitDataState.value.
           TaskElementScreen(
               task = viewModelAddTask.task,
               category = viewModelAddTask.category,
               categories = viewModelAddTask.categories,
               addTask = viewModelAddTask::addTask,
               updateTask = viewModelAddTask::updateTask,
               addTaskState = it.arguments?.getString("taskId") == null,
               tagList = viewModelAddTask.tags,
               fetchDataState = viewModelAddTask.submitDataState,
               inputValidation = viewModelAddTask.stateErrorValidation,
               validateInput = viewModelAddTask::validateInput
           )
        }
        composable(route = Screens.timeline) {
            val viewModelTimeLineTask = hiltViewModel<TimeLineViewModel>()
            TimeLineScreen(
                navHostController::navigate,
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