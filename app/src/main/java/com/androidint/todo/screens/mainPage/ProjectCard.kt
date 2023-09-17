package com.androidint.todo.screens.mainPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.utils.TaskMapper

@Composable
fun ProjectCard(
    categoryWithTasks: List<CategoryWithTasks>
    ) {
    val list = TaskMapper.mapToTaskList(categoryWithTasks)
    Column(

    ) {
        Text(text = "Projects" ,
            Modifier
                .size(25.dp)
                .padding(25.dp) , Color.Black)
        LazyRow(contentPadding = PaddingValues(horizontal = 10.dp, vertical = 20.dp)
        ) {
            items(list.size) { index ->
                Card (
                    colors = CardDefaults.cardColors(
                        containerColor = Color.DarkGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(15.dp),
                    shape = MaterialTheme.shapes.small

                    ) {

                    Column (
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ){
                        PieChart(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(2.dp),
                            colors = listOf(
                                Color.Gray,
                                MaterialTheme.colorScheme.primary,
                            ) ,
                            inputValues = listOf(40f , 60f),
                        )
                        Text(
                            text = list.get(index).name,
                            modifier = Modifier.padding(top = 10.dp),
                            color = Color.Gray,
                            fontSize = 10.sp,
                        )
                        Text(
                            text = list.get(index).title,
                            modifier = Modifier.padding(top = 10.dp),
                            color = Color.White
                        )
                        Text(
                            text = list.get(index).day.dayOfWeek.toString(),
                            modifier = Modifier.padding(top = 10.dp),
                            color = Color.Green
                        )
                    }
                }

            }
        }
    }

}
@Preview
@Composable
fun preview(){
//    ProjectCard(count = 6)
}