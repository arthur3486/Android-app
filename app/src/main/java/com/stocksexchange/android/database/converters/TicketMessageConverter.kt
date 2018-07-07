package com.stocksexchange.android.database.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.stocksexchange.android.api.model.TicketMessage
import com.stocksexchange.android.api.utils.fromJson

/**
 * A converter used for transforming [TicketMessage]
 * object(-s) to a JSON string and vice versa.
 */
object TicketMessageConverter {


    /**
     * Converts a map of [TicketMessage] objects to a JSON string.
     *
     * @param map The map of [TicketMessage] objects
     * to convert to JSON
     *
     * @return The JSON string of the map of [TicketMessage] objects
     */
    @TypeConverter
    @JvmStatic
    fun fromMap(map: Map<Long, TicketMessage>?): String {
        return Gson().toJson(map)
    }


    /**
     * Converts a JSON string into a map of [TicketMessage] objects.
     *
     * @param json The JSON string of the map of [TicketMessage] objects
     *
     * @return The map of [TicketMessage] objects constructed from
     * the JSON string
     */
    @TypeConverter
    @JvmStatic
    fun toMap(json: String?): Map<Long, TicketMessage>? {
        return json?.let { Gson().fromJson(it) }
    }


}