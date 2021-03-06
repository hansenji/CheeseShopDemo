package com.vikingsen.cheesedemo.util


import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

@Suppress("MemberVisibilityCanPrivate")
object DrawableUtil {

    @JvmStatic
    fun tintAllMenuIcons(menu: Menu, @ColorInt colorInt: Int) {
        (0 until menu.size()).map { menu.getItem(it) }
                .forEach { tintMenuItemIcon(it, colorInt) }
    }

    @JvmStatic
    fun tintMenuItemIcon(menuItem: MenuItem, @ColorInt colorInt: Int) {
        val drawable = menuItem.icon
        if (drawable != null) {
            val wrapped = DrawableCompat.wrap(drawable)
            drawable.mutate()
            DrawableCompat.setTint(wrapped, colorInt)
            menuItem.icon = drawable
        }
    }
}
