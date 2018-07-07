package com.stocksexchange.android.model

import com.stocksexchange.android.api.model.TicketCategories

/**
 * Parameters storing ticket creation data.
 */
data class TicketCreationParameters(
    val category: TicketCategories,
    val currency: String?,
    val subject: String,
    val message: String
)