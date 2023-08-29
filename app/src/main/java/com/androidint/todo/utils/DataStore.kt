package com.androidint.todo.utils

import android.content.pm.ApplicationInfo
import androidx.compose.ui.graphics.Color
import androidx.core.os.BuildCompat
import com.androidint.todo.BuildConfig

class DataStore {

    companion object {
        private const val DATA_STORE_NAME = "${BuildConfig.APPLICATION_ID}_data_store"

        fun categoryToColor(categoryId :Int):Color{
            return when (categoryId){
                1 -> Color.Blue
                2 -> Color.DarkGray
                3 -> Color.Yellow
                4 -> Color.Green
                else -> {Color.Blue}
            }
        }
    }
}
