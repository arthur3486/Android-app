package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName

/**
 * A model class holding information regarding ticket reply.
 */
data class TicketReplyResponse(
    @SerializedName("ticket_message_id") val id: Long,
    @SerializedName("msg") val message: String
)