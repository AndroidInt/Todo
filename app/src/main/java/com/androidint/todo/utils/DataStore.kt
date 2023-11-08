package com.androidint.todo.utils

import android.content.pm.ApplicationInfo
import androidx.compose.ui.graphics.Color
import androidx.core.os.BuildCompat
import com.androidint.todo.BuildConfig

class DataStore {

    companion object {
        private const val DATA_STORE_NAME = "${BuildConfig.APPLICATION_ID}_data_store"

        val colors = listOf(
            Color.Yellow,
            Color.Cyan,
            Color.Magenta,
            Color.Blue,
            Color.Red,
            Color.Gray
        )

        fun categoryToColor(categoryId: Int): Color {
            return when (categoryId) {
                0 -> Color.Yellow
                1 -> Color.Cyan
                2 -> Color.Magenta
                3 -> Color.Blue
                4 -> Color.Red
                5 -> Color.Gray
                else -> {
                    Color.Yellow
                }
            }
        }

        fun categoryToColorV2(categoryId: Int): Color {
            if (categoryId >= colorsV2.size)
                throw IndexOutOfBoundsException()
            return colorsV2[categoryId]
        }


        fun colorToCategoryGroup(color: Color): Int {
            return when (color) {
                Color.Yellow -> 0
                Color.Cyan -> 1
                Color.Magenta -> 2
                Color.Blue -> 3
                Color.Red -> 4
                Color.Gray -> 5
                else -> {
                    0
                }
            }
        }

        fun getDayOfWeek(index: Int): String {
            val dayOfWeek = listOf(
                "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
            )
            return dayOfWeek[index]
        }

        val Red = Color(0xFFEF5350)
        val Pink = Color(0xFFEC407A)
        val Purple = Color(0xFFAB47BC)
        val DeepPurple = Color(0xFF7E57C2)
        val Indigo = Color(0xFF5C6BC0)
        val Blue = Color(0xFF42A5F5)
        val LightBlue = Color(0xFF29B6F6)
        val Cyan = Color(0xFF26C6DA)
        val Teal = Color(0xFF26A69A)
        val Green = Color(0xFF66BB6A)
        val LightGreen = Color(0xFF9CCC65)
        val Lime = Color(0xFFD4E157)


        val colorsV2 = listOf(
            Lime,
            Red,
            Pink,
            Purple,
            DeepPurple,
            Indigo,
            Blue,
            LightBlue,
            Cyan,
            Teal,
            Green,
            LightGreen,
        )


    }


}
