package com.stocksexchange.android.ui.utils.extensions

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable

/**
 * Sets the color for the drawable to the specified color.
 *
 * @param color The color to set
 */
fun Drawable.setColor(color: Int) {
    mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}