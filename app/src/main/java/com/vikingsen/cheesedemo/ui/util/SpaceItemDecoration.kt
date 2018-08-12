package com.vikingsen.cheesedemo.ui.util

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.view.View

class SpaceItemDecoration(context: Context, @DimenRes spaceRes: Int) : RecyclerView.ItemDecoration() {

    private val space = context.resources.getDimensionPixelSize(spaceRes)

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect?.set(space, space, space, space)
    }
}