package com.vikingsen.cheesedemo.log


import timber.log.Timber

class DebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        // add line number
        return super.createStackElementTag(element) + ":" + element.lineNumber
    }
}
