package com.stocksexchange.android.database.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.stocksexchange.android.api.model.OrderAmount
import com.stocksexchange.android.api.utils.fromJson

/**
 * A converter used for transforming [OrderAmount]
 * object(-s) to a JSON string and vice versa.
 */
object OrderAmountConverter {


    /**
     * Converts a map of [OrderAmount] objects to a JSON string.
     *
     * @param map The map of [OrderAmount] objects
     * to convert to JSON
     *
     * @return The JSON string of the map of [OrderAmount] objects
     */
    @TypeConverter
    @JvmStatic
    fun fromMap(map: Map<Double, OrderAmount>?): String {
        return Gson().toJson(map)
    }


    /**
     * Converts a JSON string into a map of [OrderAmount] objects.
     *
     * @param json The JSON string of the map of [OrderAmount] objects
     *
     * @return The map of [OrderAmount] objects constructed from
     * the JSON string
     */
    @TypeConverter
    @JvmStatic
    fun toMap(json: String?): Map<Double, OrderAmount>? {
        return json?.let { Gson().fromJson(json) }
    }


}