package com.vikingsen.cheesedemo.model

import androidx.annotation.StringRes


sealed class Resource<out T>(val data: T?) {
    class Loading<out T>(data: T? = null) : Resource<T>(data)
    class Success<out T>(data: T?): Resource<T>(data)
    @Suppress("unused") // messageId can be used.
    class Error<out T>(@StringRes val messageId: Int = 0, data: T? = null): Resource<T>(data)
}