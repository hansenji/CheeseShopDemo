package com.vikingsen.cheesedemo.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

/**
 * A LiveData class that has `null` value.
 */
@Suppress("unused")
class AbsentLiveData<R>
private constructor() : LiveData<R>() {
    init {
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> = AbsentLiveData()

        /**
         * null only absentValue
         */
        inline fun <R, T> nullSwitchMap(trigger: LiveData<T>, crossinline func: (T) -> LiveData<R>): LiveData<R> {
            return Transformations.switchMap(trigger) {
                when (it) {
                    null -> AbsentLiveData.create()
                    else -> func(it)
                }
            }
        }

        /**
         * Single absentValues
         */
        inline fun <R, T> switchMap(trigger: LiveData<T>, crossinline isAbsent: (T?) -> Boolean, crossinline func: (T?) -> LiveData<R>): LiveData<R> {
            return Transformations.switchMap(trigger) {
                when {
                    isAbsent(it) -> AbsentLiveData.create()
                    else -> func(it)
                }
            }
        }
    }
}