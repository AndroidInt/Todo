package com.androidint.todo.screens.mainPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.repository.model.Day
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TimeTask
import com.androidint.todo.utils.ChartView
import com.androidint.todo.utils.TopAppBarCompose
import com.androidint.todo.utils.TopBarClickListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random


@Composable
fun MainPageCompose(
    navHostController: NavHostController,
    viewModel: MainPageViewModel
) {
    val topBarClickListener: TopBarClickListener
    val sdf = SimpleDateFormat("EEEE")
    val d = Date()
    val current_day: String = sdf.format(d)
    val categoryWithTasks = viewModel.getTasksWithCategory()
    val tasks =  viewModel.getAllTasks()
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBarCompose(title = current_day)

        categoryWithTasks?.let {
            tasks?.let{

                MainCard(
                    categoryWithTasks
                )

                ProjectCard(categoryWithTasks)
                tasksList(
                    tasks
                )

            }
        }?: run{
            nullData()
        }
    }
}







@Composable
fun tasksList(
    tasks: List<Task>
) {
    Column {
        Text(
            text = "Tasks",
            Modifier
                .size(25.dp)
                .padding(25.dp), Color.Black
        )
        LazyColumn(contentPadding = PaddingValues(horizontal = 10.dp, vertical = 20.dp)) {
            items(tasks.size) { item ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.DarkGray,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(15.dp),

                    ) {

                    Text(
                        text = tasks.get(item).title,
                        modifier = Modifier.padding(15.dp),
                        color = Color.White
                    )
                }

            }
        }
    }

}
@Composable
fun nullData(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Nothing to show!\r\nPlease make your first task with +",
            modifier = Modifier.padding(15.dp),
            color = Color.Black
        )
    }
}

@Preview
@Composable
fun MainPageComposePreview() {
//    MainPageCompose()
}
