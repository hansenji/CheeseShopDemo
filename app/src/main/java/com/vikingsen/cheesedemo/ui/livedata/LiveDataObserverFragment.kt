package com.vikingsen.cheesedemo.ui.livedata

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class LiveDataObserverFragment : Fragment() {

    protected inline fun <T> LiveData<T>.observe(crossinline block: (T?) -> Unit) {
        observe(this@LiveDataObserverFragment, Observer { block(it) })
    }

    protected inline fun <T> LiveData<T>.observeNotNull(crossinline block: (T) -> Unit) {
        observe(this@LiveDataObserverFragment, Observer {
            if (it != null) {
                block(it)
            }
        })
    }
}