package com.stocksexchange.android.database.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.stocksexchange.android.api.model.UserSession
import com.stocksexchange.android.api.utils.fromJson

/**
 * A converter used for transforming [UserSession]
 * object(-s) to a JSON string and vice versa.
 */
object UserSessionConverter {


    /**
     * Converts a list of [UserSession] objects to a JSON string.
     *
     * @param list The list of [UserSession] objects
     * to convert to JSON
     *
     * @return The JSON string of the list of [UserSession] objects
     */
    @TypeConverter
    @JvmStatic
    fun fromList(list: List<UserSession>?): String {
        return Gson().toJson(list)
    }


    /**
     * Converts a JSON string into a list of [UserSession] objects.
     *
     * @param json The JSON string of the list of [UserSession] objects
     *
     * @return The list of [UserSession] objects constructed from
     * the JSON string
     */
    @TypeConverter
    @JvmStatic
    fun toList(json: String?): List<UserSession>? {
        return json?.let { Gson().fromJson(it) }
    }


}