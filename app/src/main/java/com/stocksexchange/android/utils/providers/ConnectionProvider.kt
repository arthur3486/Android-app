package com.stocksexchange.android.utils.providers

import android.content.Context
import com.stocksexchange.android.ui.utils.extensions.isNetworkAvailable

/**
 * A help class used for providing connectivity related
 * functionality.
 */
class ConnectionProvider(context: Context) {


    private val context: Context = context.applicationContext




    /**
     * Checks whether the network is available or not.
     *
     * @return true if available; false otherwise
     */
    fun isNetworkAvailable() : Boolean {
        return context.isNetworkAvailable()
    }


}