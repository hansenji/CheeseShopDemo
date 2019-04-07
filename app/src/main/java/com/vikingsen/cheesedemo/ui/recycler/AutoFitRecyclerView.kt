package com.vikingsen.cheesedemo.ui.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vikingsen.cheesedemo.R


@Suppress("MemberVisibilityCanBePrivate")
open class AutoFitRecyclerView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {

    var minColumnWidth = -1
        set(value) {
            field = value
            invalidate()
        }

    protected val gridLayoutManager: GridLayoutManager

    init {
        attrs?.let {
            val array = context.obtainStyledAttributes(attrs, R.styleable.AutoFitRecyclerView, defStyle, 0)
            minColumnWidth = array.getDimensionPixelSize(R.styleable.AutoFitRecyclerView_minColumnWidth, -1)
            array.recycle()
        }
        gridLayoutManager = GridLayoutManager(context, 1)
        super.setLayoutManager(gridLayoutManager)
    }

    override fun setLayoutManager(layout: LayoutManager?) = throw IllegalStateException("AutoFitRecyclerView doesn't allow users to set layout manager")

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        when {
            minColumnWidth > 0 -> gridLayoutManager.spanCount = Math.max(1, measuredWidth / minColumnWidth)
            else -> gridLayoutManager.spanCount = 1
        }
    }
}