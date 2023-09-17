package com.androidint.todo.repository.model

import androidx.room.Embedded

data class TaskWithCategoryName(
    val title: String,
    val description: String?,
    @Embedded
    val day: Day,
    val ownerCategoryId: Int,
    @Embedded
    val timeDuration: TimeTask,
    val priority: Int = 1,
    var done: Boolean = false,
    val name: String = "Inbox"
)
