package me.gndev.water_battle.util

import android.content.Context

object DimensionUtils {
    fun dpFromPx(context: Context, px: Int): Int {
        return (px / context.resources.displayMetrics.density).toInt()
    }

    fun pxFromDp(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}