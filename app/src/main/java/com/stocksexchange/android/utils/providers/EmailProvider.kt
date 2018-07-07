package com.stocksexchange.android.utils.providers

import android.content.Context
import android.content.Intent

/**
 * A helper class used for providing email related functionality.
 */
class EmailProvider(context: Context) {


    private val context: Context = context.applicationContext




    /**
     * Checks whether the mail client is present on the device
     * or not.
     *
     * @return true if is present; false otherwise
     */
    fun isMailClientPresenter(): Boolean {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"

        return context.packageManager.queryIntentActivities(intent, 0).isNotEmpty()
    }


}