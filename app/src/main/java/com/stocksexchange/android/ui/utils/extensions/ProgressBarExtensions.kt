package com.stocksexchange.android.ui.utils.extensions

import android.support.annotation.ColorRes
import android.widget.ProgressBar

/**
 * Sets the progress bar color by mutating its drawable.
 *
 * @color The color to set
 */
fun ProgressBar.setColor(@ColorRes color: Int) {
    indeterminateDrawable = context.getColoredCompatDrawable(indeterminateDrawable, color)
}