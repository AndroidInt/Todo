package com.androidint.todo.utils

import android.content.pm.ApplicationInfo
import androidx.core.os.BuildCompat
import com.androidint.todo.BuildConfig

class DataStore {

    companion object {
        private const val DATA_STORE_NAME = "${BuildConfig.APPLICATION_ID}_data_store"
    }
}