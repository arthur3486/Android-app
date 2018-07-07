package com.stocksexchange.android.api.model

/**
 * An enumeration of all possible ticket statuses.
 */
enum class TicketStatuses(val id: Int) {

    AWAITING_SUPPORT_RESPONSE(1),
    AWAITING_CUSTOMER_RESPONSE(2),
    CLOSED(3)

}