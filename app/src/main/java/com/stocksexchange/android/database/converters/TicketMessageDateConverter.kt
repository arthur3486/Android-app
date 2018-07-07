package com.stocksexchange.android.database.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.stocksexchange.android.api.model.TicketMessageDate
import com.stocksexchange.android.api.utils.fromJson

/**
 * A converter used for transforming [TicketMessageDate]
 * object(-s) to a JSON string and vice versa.
 */
object TicketMessageDateConverter {


    /**
     * Converts a [TicketMessageDate] object to a JSON string.
     *
     * @param ticketMessageDate The [TicketMessageDate] object
     * to convert to JSON
     *
     * @return The JSON string of the [TicketMessageDate] object
     */
    @TypeConverter
    @JvmStatic
    fun fromTicketMessageDate(ticketMessageDate: TicketMessageDate?): String {
        return Gson().toJson(ticketMessageDate)
    }


    /**
     * Converts a JSON string into an [TicketMessageDate] object.
     *
     * @param json The JSON string of the [TicketMessageDate] object
     *
     * @return The [TicketMessageDate] object constructed from
     * the JSON string
     */
    @TypeConverter
    @JvmStatic
    fun toTicketMessageDate(json: String?): TicketMessageDate? {
        return json?.let { Gson().fromJson(it) }
    }


}