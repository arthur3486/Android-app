package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * A model class holding the date information for ticket messages.
 */
data class TicketMessageDate(
    @SerializedName("date") val date: String,
    @SerializedName("timezone") val timezone: String
) : Serializable