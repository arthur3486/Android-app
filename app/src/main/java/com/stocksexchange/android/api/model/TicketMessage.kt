package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * A model class holding the message information for tickets.
 */
data class TicketMessage(
    @SerializedName("text") val text: String = "",
    @SerializedName("date") val date: TicketMessageDate? = null
) : Serializable