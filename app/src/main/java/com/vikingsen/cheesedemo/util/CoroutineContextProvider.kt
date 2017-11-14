package com.vikingsen.cheesedemo.util

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlin.coroutines.experimental.CoroutineContext


@Suppress("unused")
interface CoroutineContextProvider {
    val ui: CoroutineContext
    val commonPool: CoroutineContext

    object MainCoroutineContextProvider: CoroutineContextProvider {
        override val ui = UI
        override val commonPool = CommonPool
    }

    object TestCoroutineContextProvider: CoroutineContextProvider {
        override val ui = CommonPool
        override val commonPool = CommonPool
    }

    object TestJdbcCoroutineContextProvider: CoroutineContextProvider {
        private val coroutineContext = newSingleThreadContext("TestJdbcContext")
        override val ui = coroutineContext
        override val commonPool = coroutineContext
    }
}