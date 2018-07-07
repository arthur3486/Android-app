package com.stocksexchange.android.utils.providers

import android.content.Context
import android.support.annotation.StringRes
import com.stocksexchange.android.R

/**
 * A helper class used for providing string related functionality.
 */
class StringProvider(context: Context) {


    private val context: Context = context.applicationContext




    /**
     * Gets a string specified by the resource id.
     *
     * @param id The id to get the string for
     *
     * @return The resolved string
     */
    fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }


    /**
     * Gets a string specified by the resource id with an argument.
     *
     * @param id The id to get the string for
     * @param arg The argument to provide for formatting
     *
     * @return The resolved string
     */
    fun getString(@StringRes id: Int, arg: String): String {
        return context.getString(id, arg)
    }


    /**
     * Gets a network connection check string.
     */
    fun getNetworkCheckMessage(): String = getString(R.string.error_check_network_connection)


    /**
     * Gets a user not found string.
     */
    fun getUserNotFoundMessage(): String = getString(R.string.error_user_not_found)


    /**
     * Gets too many requests string.
     */
    fun getTooManyRequestsMessage(): String = getString(R.string.error_too_many_requests)


    /**
     * Gets server unresponsive string.
     */
    fun getServerUnresponsiveMessage(): String = getString(R.string.error_server_not_responding)


    /**
     * Gets something went wrong string.
     */
    fun getSomethingWentWrongMessage(): String = getString(R.string.error_something_went_wrong)


}