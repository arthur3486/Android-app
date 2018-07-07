package com.stocksexchange.android.database.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.stocksexchange.android.api.model.CandleStick
import com.stocksexchange.android.api.utils.fromJson

/**
 * A converter used for transforming [CandleStick]
 * object(-s) to a JSON string and vice versa.
 */
object CandleStickConverter {


    /**
     * Converts a list of [CandleStick] objects to a JSON string.
     *
     * @param list The list of [CandleStick] objects
     * to convert to JSON
     *
     * @return The JSON string of the list of [CandleStick] objects
     */
    @TypeConverter
    @JvmStatic
    fun fromList(list: List<CandleStick>): String {
        return Gson().toJson(list)
    }


    /**
     * Converts a JSON string into a list of [CandleStick] objects.
     *
     * @param json The JSON string of the list of [CandleStick] objects
     *
     * @return The list of [CandleStick] objects constructed from
     * the JSON string
     */
    @TypeConverter
    @JvmStatic
    fun toList(json: String?): List<CandleStick>? {
        return json?.let { Gson().fromJson(it) }
    }


}