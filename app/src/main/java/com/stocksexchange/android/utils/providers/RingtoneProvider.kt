package com.stocksexchange.android.utils.providers

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri

/**
 * A helper class used for providing ringtone related functionality.
 */
class RingtoneProvider(context: Context) {


    private val context: Context = context.applicationContext




    /**
     * Fetches the name of the ringtone URI.
     *
     * @param ringtoneUri The ringtone URI to fetch the name from
     *
     * @return The name of the ringtone
     */
    fun getRingtoneName(ringtoneUri: String): String {
        val ringtone = RingtoneManager.getRingtone(context, Uri.parse(ringtoneUri))

        return if(ringtone != null) {
            ringtone.getTitle(context)
        } else {
            ""
        }
    }


}