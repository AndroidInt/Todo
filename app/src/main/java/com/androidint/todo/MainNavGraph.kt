package com.androidint.todo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.androidint.todo.screen.mainPage.MainPageCompose
import com.androidint.todo.screen.mainPage.MainPageViewModel
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
    }
}


//
//var changeScreen by remember {
//    mutableStateOf(true)
//}
//
//val viewModelAddTask by viewModels<AddTaskViewModel>()
//val viewModelTimeLineTask by viewModels<TimeLineViewModel>()
//
//
//Column {
//    Button(onClick = { changeScreen = !changeScreen }, modifier = Modifier.fillMaxWidth()) {
//        Text(text = "changeScreen")
//    }
//
//    if (changeScreen){
//        AddTaskScreen(
//            conflictedDurationState = viewModelAddTask.conflictedDurationState,
//            conflictedCategoryState = viewModelAddTask.conflictedCategoryState,
//            categoryList = viewModelAddTask.categoryList,
//            updateRequest = viewModelAddTask.updateRequest,
//            addTask = viewModelAddTask::addTask,
//            updateTask = viewModelAddTask::updateTask,
//            successfullyDone = viewModelAddTask.successfullyDone,
//            onSuccessfullyDone = viewModelAddTask::onSuccessfullyDone
//        )
//    }else{
//        TimeLineScreen(
//            month = viewModelTimeLineTask.month,
//            previousMonth = viewModelTimeLineTask::previousMonth,
//            nextMonth = viewModelTimeLineTask::nextMonth,
//            year = viewModelTimeLineTask.year,
//            day = viewModelTimeLineTask.day,
//            setDay = viewModelTimeLineTask::setDay,
//            monthName = viewModelTimeLineTask.monthName,
//            tasks = viewModelTimeLineTask.tasks,
//            categories = viewModelTimeLineTask.categories
//
//        )
//    }
//
//
//
//
//
//
//}