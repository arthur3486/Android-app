package com.stocksexchange.android.ui.utils.listeners

import android.text.Editable
import android.text.TextWatcher

/**
 * An adapter for [TextWatcher] listener with a helper callback.
 */
class QueryListener(private val callback: Callback) : TextWatcher {


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Stub
    }


    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        if(text.isNotEmpty()) {
            callback.onQueryEntered(text.toString())
        } else {
            callback.onQueryRemoved()
        }
    }


    override fun afterTextChanged(s: Editable?) {
        // Stub
    }


    /**
     * A helper interface to get notified whenever a query is entered
     * or is removed.
     */
    interface Callback {

        /**
         * Gets called whenever a character is entered.
         *
         * @param query The whole query entered
         */
        fun onQueryEntered(query: String)

        /**
         * Gets called whenever a query is removed.
         */
        fun onQueryRemoved()

    }


}