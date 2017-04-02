package com.vikingsen.cheesedemo.util;


import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Menu;
import android.view.MenuItem;

public class DrawableUtil {
    public static void tintAllMenuIcons(@NonNull Menu menu, @ColorInt final int colorInt) {
        for (int i = 0; i < menu.size(); ++i) {
            final MenuItem item = menu.getItem(i);
            tintMenuItemIcon(item, colorInt);
        }
    }

    public static void tintMenuItemIcon(@NonNull MenuItem menuItem, @ColorInt int colorInt) {
        final Drawable drawable = menuItem.getIcon();
        if (drawable != null) {
            final Drawable wrapped = DrawableCompat.wrap(drawable);
            drawable.mutate();
            DrawableCompat.setTint(wrapped, colorInt);
            menuItem.setIcon(drawable);
        }
    }
}
