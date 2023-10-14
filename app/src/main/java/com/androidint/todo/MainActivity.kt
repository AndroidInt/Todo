package com.androidint.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph
import com.androidint.todo.screen.addtask.AddTaskScreen
import com.androidint.todo.screen.addtask.AddTaskViewModel
import com.androidint.todo.screen.timeline.TimeLineScreen
import com.androidint.todo.screen.timeline.TimeLineViewModel

import com.androidint.todo.ui.theme.TodoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TodoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var changeScreen by remember {
                        mutableStateOf(true)
                    }

                    val viewModelAddTask by viewModels<AddTaskViewModel>()
                    val viewModelTimeLineTask by viewModels<TimeLineViewModel>()


                    Column {
                        Button(onClick = { changeScreen = !changeScreen }, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "changeScreen")
                        }

                        if (changeScreen){
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
                        }else{
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
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoTheme {
        Greeting("Android")
    }
}
