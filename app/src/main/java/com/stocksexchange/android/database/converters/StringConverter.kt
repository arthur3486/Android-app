package com.stocksexchange.android.database.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.stocksexchange.android.api.utils.fromJson

/**
 * A converter used for transforming [String]
 * object(-s) to a JSON string and vice versa.
 */
object StringConverter {


    /**
     * Converts a map of [String] objects to a JSON string.
     *
     * @param map The map of [String] objects
     * to convert to JSON
     *
     * @return The JSON string of the map of [String] objects
     */
    @TypeConverter
    @JvmStatic
    fun fromMap(map: Map<String, String>?): String {
        return Gson().toJson(map)
    }


    /**
     * Converts a JSON string into a map of [String] objects.
     *
     * @param json The JSON string of the map of [String] objects
     *
     * @return The map of [String] objects constructed from
     * the JSON string
     */
    @TypeConverter
    @JvmStatic
    fun toMap(json: String?): Map<String, String>? {
        return json?.let { Gson().fromJson(json) }
    }


}