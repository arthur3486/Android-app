package com.stocksexchange.android.model

/**
 * Parameters storing ticket reply related data.
 */
data class TicketReplyParameters(
    val ticketId: Long,
    val message: String
)