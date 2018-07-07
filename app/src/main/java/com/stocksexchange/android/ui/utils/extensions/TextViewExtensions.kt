package com.stocksexchange.android.ui.utils.extensions

import android.graphics.drawable.Drawable
import android.support.annotation.StyleRes
import android.widget.TextView
import com.stocksexchange.android.AT_LEAST_MARSHMALLOW

/**
 * Sets a left drawable for the TextView.
 *
 * @param drawable The drawable to set
 */
fun TextView.setLeftDrawable(drawable: Drawable?) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}


/**
 * Gets a left drawable of the TextView.
 *
 * @return The TextView's left drawable
 */
fun TextView.getLeftDrawable(): Drawable? {
    return compoundDrawables[0]
}


/**
 * Sets a top drawable for the TextView.
 *
 * @param drawable The drawable to set
 */
fun TextView.setTopDrawable(drawable: Drawable?) {
    this.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
}


/**
 * Gets a top drawable of the TextView.
 *
 * @return The TextView's top drawable
 */
fun TextView.getTopDrawable(): Drawable? {
    return compoundDrawables[1]
}


/**
 * Sets a right drawable for the TextView.
 *
 * @param drawable The drawable to set
 */
fun TextView.setRightDrawable(drawable: Drawable?) {
    this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
}


/**
 * Gets a right drawable of the TextView.
 *
 * @return The TextView's right drawable
 */
fun TextView.getRightDrawable(): Drawable? {
    return compoundDrawables[2]
}


/**
 * Sets a bottom drawable for the TextView.
 *
 * @param drawable The drawable to set
 */
fun TextView.setBottomDrawable(drawable: Drawable?) {
    this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable)
}


/**
 * Gets a bottom drawable of the TextView.
 *
 * @return The TextView's bottom drawable
 */
fun TextView.getBottomDrawable(): Drawable? {
    return compoundDrawables[3]
}


/**
 * Clears the TextView from any compound drawables.
 */
fun TextView.clearDrawable() {
    this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
    this.compoundDrawablePadding = 0
}


/**
 * Sets a style for the typeface.
 *
 * @param style The style to set
 */
fun TextView.setTypefaceStyle(style: Int) {
    setTypeface(null, style)
}


/**
 * Sets a text compatibility appearance.
 *
 * @param resId The resource id of the text appearance
 */
@SuppressWarnings("NewApi")
fun TextView.setTextAppearanceCompat(@StyleRes resId: Int) {
    if(AT_LEAST_MARSHMALLOW) {
        setTextAppearance(resId)
    } else {
        setTextAppearance(context, resId)
    }
}