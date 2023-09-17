package com.androidint.todo.utils

sealed  class Resource<T>(
    val data: T? = null,
    val error : String = ":"
) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Loading<T> : Resource<T>()
    class Error<T>(messages: String):Resource<T>(error =  messages)
}