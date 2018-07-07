package com.stocksexchange.android.utils.providers

import android.content.Context
import android.support.annotation.BoolRes

/**
 * A helper class used for providing boolean resources.
 */
class BooleanProvider(context: Context) {


    private val context: Context = context.applicationContext




    /**
     * Returns a boolean with the specified id.
     *
     * @param id The id to get the boolean for
     *
     * @return The boolean with the specified id
     */
    fun getBoolean(@BoolRes id: Int): Boolean {
        return context.resources.getBoolean(id)
    }


}