@file:Suppress("UsePropertyAccessSyntax")

package com.stocksexchange.android.ui.utils.extensions

import android.view.View


/**
 * Checks whether the view is visible, that is, the visibility flag
 * is set to [View.VISIBLE].
 *
 * @return true if the view is visible; false otherwise
 */
fun View.isVisible(): Boolean = visibility == View.VISIBLE


/**
 * Sets the view's visibility flag to [View.VISIBLE].
 */
fun View.makeVisible() = setVisibility(View.VISIBLE)


/**
 * Checks whether the view is invisible, that is, the visibility flag
 * is set to [View.INVISIBLE].
 *
 * @return true if the view is invisible; false otherwise
 */
fun View.isInvisible(): Boolean = visibility == View.INVISIBLE


/**
 * Sets the view's visibility flag to [View.INVISIBLE].
 */
fun View.makeInvisible() = setVisibility(View.INVISIBLE)


/**
 * Checks whether the view in gone, that is, the visibility flag
 * is set to [View.GONE].
 *
 * @return true if the view is gone; false otherwise
 */
fun View.isGone(): Boolean = visibility == View.GONE


/**
 * Sets the view's visibility flag to [View.GONE].
 */
fun View.makeGone() = setVisibility(View.GONE)


/**
 * Enables the view by setting its [View.isEnabled] property
 * to true and, optionally, changing its alpha.
 *
 * @param changeAlpha Whether to change the alpha of the view.
 * Default is false.
 * @param alpha The new alpha value for the view if [changeAlpha]
 * parameter is true. Default is 0.5f.
 */
fun View.enable(changeAlpha: Boolean = false, alpha: Float = 1f) {
    if(!isEnabled) {
        isEnabled = true

        if(changeAlpha) {
            setAlpha(alpha)
        }
    }
}


/**
 * Disables the view by setting its [View.isEnabled] property
 * to false and, optionally, changing its alpha.
 *
 * @param changeAlpha Whether to change the alpha of the view.
 * Default is false.
 * @param alpha The new alpha value for the view if [changeAlpha]
 * parameter is true. Default is 0.5f.
 */
fun View.disable(changeAlpha: Boolean = false, alpha: Float = 0.5f) {
    if(isEnabled) {
        isEnabled = false

        if(changeAlpha) {
            setAlpha(alpha)
        }
    }
}


/**
 * Sets the view's horizontal and vertical scale.
 *
 * @param scale The new scale to assign
 */
fun View.setScale(scale: Float) {
    scaleX = scale
    scaleY = scale
}


/**
 * Sets the view's horizontal padding (left and right).
 *
 * @param padding The new horizontal padding
 */
fun View.setHorizontalPadding(padding: Int) {
    setPadding(
        padding,
        paddingTop,
        padding,
        paddingBottom
    )
}


/**
 * Sets the view's vertical padding (top and bottom).
 *
 * @param padding The new vertical padding
 */
fun View.setVerticalPadding(padding: Int) {
    setPadding(
        paddingLeft,
        padding,
        paddingRight,
        padding
    )
}


/**
 * Sets the view's top padding.
 *
 * @param padding The new top padding
 */
fun View.setPaddingTop(padding: Int) {
    setPadding(paddingLeft, padding, paddingRight, paddingBottom)
}


/**
 * Sets the view's bottom padding.
 *
 * @param padding The new bottom padding
 */
fun View.setPaddingBottom(padding: Int) {
    setPadding(
        paddingLeft,
        paddingTop,
        paddingRight,
        padding
    )
}