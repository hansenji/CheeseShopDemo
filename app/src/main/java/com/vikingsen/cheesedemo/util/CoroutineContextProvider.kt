package com.vikingsen.cheesedemo.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors


@Suppress("unused")
interface CoroutineContextProvider {
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher

    object MainCoroutineContextProvider: CoroutineContextProvider {
        override val main = Dispatchers.Main
        override val default = Dispatchers.Default
        override val io = Dispatchers.IO
    }

    object TestCoroutineContextProvider: CoroutineContextProvider {
        override val main = Dispatchers.Default
        override val default = Dispatchers.Default
        override val io = Dispatchers.IO
    }

    object TestJdbcCoroutineContextProvider: CoroutineContextProvider {
        private val coroutineContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        override val main = coroutineContext
        override val default = coroutineContext
        override val io = coroutineContext
    }
}