package com.androidint.todo.screens.mainPage

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor
import com.androidint.todo.repository.model.Category
import com.androidint.todo.repository.model.CategoryWithTasks
import com.androidint.todo.repository.model.Day
import com.androidint.todo.repository.model.Task
import com.androidint.todo.repository.model.TimeTask
import com.androidint.todo.screens.theme.TodoTheme

import kotlin.random.Random

@Composable
fun MainCard(cats: List<CategoryWithTasks>) {

    val colors = getRandomColor(cats.size)
Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
    shape = MaterialTheme.shapes.small
) {
    Column(modifier = Modifier.padding(8.dp)) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1F)

                ) {
                Graph(cats , colors)
            }
            Column(Modifier.weight(1F)) {
                ListCategory(
                    cats , colors
                )
            }



        }


    }

}


}


fun getRandomColor(sizeOfList: Int ): MutableList<Color>{

    val  colorList : MutableList<Color> = mutableListOf()
    for (i in 0..sizeOfList){
        val randomColor = Random
        val color = android.graphics.Color.argb(255, randomColor.nextInt(256), randomColor.nextInt(256), randomColor.nextInt(256))
        val composeColor = Color(color)
        colorList.add(composeColor)
    }
    return colorList
}
@Composable
fun ListCategory(cats: List<CategoryWithTasks>,
                 colors: MutableList<Color>

) {

    Row {
        Column() {
            for (item in cats) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .size(8.dp)
                    ) {

                        drawCircle(colors.get(cats.indexOf(item)))
                    }

                    Text(text = item.category.name)
                }

                Spacer(modifier = Modifier.height(12.dp))

            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column() {
            for (item in cats) {

                val done = item.tasks.filter { it.done }.size
                Text(text = "${done * 100 / item.tasks.size}%")
                Spacer(modifier = Modifier.height(12.dp))
            }


        }
    }


}

@Composable
fun Graph(
    cats: List<CategoryWithTasks> ,
    colors : MutableList<Color>
) {
    //TODO("create graph base input")
    val listArc = mutableListOf<Arc>()

    for ((index, item) in cats.take(3).withIndex()) {
        val done = item.tasks.filter { it.done }.size
        val percentage = done * 100F / item.tasks.size
        listArc.add(Arc(colors.get(cats.indexOf(item)), percentage, (index+1).toFloat()))
    }
    CircleGraph(
        listArc
    )
}

data class Arc(val color: Color, val percentage: Float, val level: Float)


@Composable
fun CircleGraph(arcs: List<Arc>) {


    Canvas(
        modifier = Modifier
            .size(size = 200.dp)
    ) {

        for (item in arcs) {

            drawCircle(
                color = Color.DarkGray,
                radius = 0.5F * item.level * size.minDimension / 4F,
                center = Offset(
                    size.minDimension / 2F,
                    size.minDimension / 2F
                ),
                style = Stroke(7F)
            )
            drawArc(
                color = item.color,
                -90F, 360F * item.percentage / 100F, false,
                topLeft = Offset(
                    x = (size.minDimension / 2F) - 0.5F * item.level * size.minDimension / 4F,
                    y = (size.minDimension / 2F) - 0.5F * item.level * size.minDimension / 4F
                ),
                style = Stroke(30F, cap = StrokeCap.Round),
                size = Size(
                    item.level * size.minDimension / 4F,
                    item.level * size.minDimension / 4F
                )
            )
        }
    }


}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GPreview() {
    val cats = mutableListOf<Category>()
    val tasks = mutableListOf<Task>()
    for (c in 1..3) {
        val cat = Category(
            name = "Inbox$c"
        )
        cat.categoryId = c
        cats.add(
            cat
        )
    }
    for (t in 1..50) {
        tasks.add(
            Task(
                ownerCategoryId = Random.nextInt(4),
                title = "title$t",
                description = "Random$t",
                day = Day(
                    1 * t,
                    2, 11, 2022
                ),

                done = Random.nextBoolean(),
                timeDuration = TimeTask(11, 30, 12, 30)
            )
        )

    }
    val catTasks = mutableListOf<CategoryWithTasks>()
    for (i in 1..3) {
        val tasksWithGroup = mutableListOf<Task>()
        tasks.filter {
            it.ownerCategoryId == i
        }.forEach {
            tasksWithGroup.add(it)
        }

        catTasks.add(
            CategoryWithTasks(cats[i - 1], tasksWithGroup)
        )

    }

    TodoTheme {
        Column() {
            MainCard(catTasks)
        }
    }
}


