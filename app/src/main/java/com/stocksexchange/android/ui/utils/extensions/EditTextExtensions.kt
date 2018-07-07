package com.stocksexchange.android.ui.utils.extensions

import android.graphics.drawable.Drawable
import android.widget.EditText
import android.widget.TextView

/**
 * Sets a cursor drawable for the EditText.
 *
 * Note: Solution based on reflection since there is
 * no a viable option to set drawable programmatically
 * as of now. Based on the answer: https://stackoverflow.com/a/26543290
 *
 * @param drawable The drawable to set
 */
fun EditText.setCursorDrawable(drawable: Drawable?) {
    if (drawable != null) {
        try {
            val cursorDrawableResourceField = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            cursorDrawableResourceField.isAccessible = true

            val editorField = TextView::class.java.getDeclaredField("mEditor")
            editorField.isAccessible = true

            val cursorDrawableFieldOwner = editorField.get(this)
            val cursorDrawableFieldClass = cursorDrawableFieldOwner.javaClass

            val cursorDrawableField = cursorDrawableFieldClass.getDeclaredField("mCursorDrawable")
            cursorDrawableField.isAccessible = true
            cursorDrawableField.set(
                cursorDrawableFieldOwner,
                arrayOf(drawable, drawable)
            )
        } catch (exception: Exception) {
            // Ignore
        }
    }
}


/**
 * Sets a specified text for the EditText and
 * adjusts the selection for it.
 *
 * @param text The text to set
 */
fun EditText.setContent(text: String) {
    setText(text)
    setSelection(text.length)
}


/**
 * Gets a text from the EditText in a String class.
 *
 * @return The EditText's text returned in a String class
 */
fun EditText.getContent(): String {
    return text.toString()
}


/**
 * Checks whether the EditText is empty or not.
 *
 * @return true if empty; false otherwise
 */
fun EditText.isEmpty(): Boolean {
    return getContent().isEmpty()
}