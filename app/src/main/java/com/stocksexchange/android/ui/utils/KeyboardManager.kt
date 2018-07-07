package com.stocksexchange.android.ui.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Responsible for showing and hiding the keyboard on the screen.
 */
class KeyboardManager private constructor(context: Context) {


    companion object {

        fun newInstance(context: Context): KeyboardManager = KeyboardManager(context)

    }


    /**
     * A flag indicating whether this manager is recycled or not.
     */
    private var isRecycled: Boolean = false

    private var inputMethodManager: InputMethodManager? =
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)




    /**
     * Requests that the keyboard be show to the user.
     *
     * @param view The currently focused view which would
     * like to receiver soft keyboard input.
     */
    fun showKeyboard(view: View?) {
        if(view != null) {
            inputMethodManager?.showSoftInput(view, 0)
        }
    }


    /**
     * Requests that the keyboard be hidden from the user.
     *
     * @param view The currently focused view
     */
    fun hideKeyboard(view: View?) {
        if(view != null) {
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    /**
     * Recycles the manager resources, etc.
     */
    fun recycle() {
        if(isRecycled) {
            return
        }

        inputMethodManager = null
        isRecycled = true
    }


}