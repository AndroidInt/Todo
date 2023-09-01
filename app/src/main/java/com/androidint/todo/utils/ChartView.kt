package com.androidint.todo.utils

import com.androidint.todo.repository.model.CategoryWithTasks
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp


@Composable
fun ChartView(
) {
    //TODO("create graph base input")
    val listArc = mutableListOf<Arc>()

//    for ((index, item) in cats.take(3).withIndex()) {
//        val done = item.tasks.filter { it.done }.size
//        val percentage = done * 100F / item.tasks.size
//        listArc.add(Arc(Color.Blue, percentage, (index+1).toFloat()))
//    }
    listArc.add(Arc(Color.Blue, 10.toFloat(), 15.toFloat()))
    listArc.add(Arc(Color.Green, 35.toFloat(), 15.toFloat()))
    listArc.add(Arc(Color.Red, 60.toFloat(), 15.toFloat()))
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
                color = Color.LightGray,
                radius = 0.5F * item.level * size.minDimension / 4F,
                center = Offset(
                    size.minDimension / 2F,
                    size.minDimension / 2F
                ),
                style = Stroke(10F)
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