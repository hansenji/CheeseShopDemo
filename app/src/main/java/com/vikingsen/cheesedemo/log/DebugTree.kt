package com.vikingsen.cheesedemo.log


import timber.log.Timber

class DebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String =
            super.createStackElementTag(element) + ":" + element.lineNumber // add line number
}
