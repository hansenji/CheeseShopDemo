package com.vikingsen.cheesedemo.ui.recycler

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * RecyclerView ViewHolder which facilitates data binding.
 */
open class BindingViewHolder<out T : ViewDataBinding>
private constructor(val binding: T) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Main constructor
     */
    constructor(@LayoutRes layoutId: Int, parent: ViewGroup) : this(inflate<T>(layoutId, parent))

    companion object {
        /**
         * Inflate a data bound view using it's layoutId and the parent. This does not attach the view to the parent.
         */
        @Suppress("NOTHING_TO_INLINE")
        inline fun <T : ViewDataBinding> inflate(@LayoutRes layoutId: Int, parent: ViewGroup): T {
            return DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
        }
    }
}