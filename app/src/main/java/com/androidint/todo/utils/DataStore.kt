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
        fun categoryToColor(categoryId :Int):Color{
            return when (categoryId){
                0 -> Color.Yellow
                1 -> Color.Cyan
                2 -> Color.Magenta
                3 -> Color.Blue
                4 -> Color.Red
                5 -> Color.Gray
                else -> {Color.Yellow}
            }
        }
        fun colorToCategoryGroup(color :Color):Int{
            return when (color){
                  Color.Yellow -> 0
                  Color.Cyan -> 1
                  Color.Magenta -> 2
                  Color.Blue -> 3
                  Color.Red -> 4
                  Color.Gray -> 5
                else -> {0}
            }
        }

    }
}
