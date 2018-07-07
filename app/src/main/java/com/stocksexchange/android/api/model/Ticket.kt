package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * A model class holding information about user tickets.
 */
data class Ticket(
    @SerializedName("ticket_id") val id: Long,
    @SerializedName("ticket_status_id") val statusId: Int,
    @SerializedName("ticket_category_id") val categoryId: Int,
    @SerializedName("subject") val subject: String,
    @SerializedName("ticket_messages") val messages: Map<Long, TicketMessage>,
    @SerializedName("updated_at") val updatedAt: Long,
    @SerializedName("created_at") val createdAt: Long
) : Serializable