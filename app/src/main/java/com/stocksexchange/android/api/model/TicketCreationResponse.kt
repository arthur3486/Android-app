package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName

/**
 * A model class holding the information regarding ticket creation.
 */
data class TicketCreationResponse(
    @SerializedName("ticket_id") val id: Long,
    @SerializedName("ticket_status_id") val statusId: Int,
    @SerializedName("ticket_category_id") val categoryId: Int,
    @SerializedName("subject") val subject: String,
    @SerializedName("ticket_message_text") val text: String,
    @SerializedName("msg") val message: String
)