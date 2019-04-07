package com.vikingsen.cheesedemo.ui.recycler

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(context: Context, @DimenRes spaceRes: Int) : RecyclerView.ItemDecoration() {

    private val space = context.resources.getDimensionPixelSize(spaceRes)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(space, space, space, space)
    }
}