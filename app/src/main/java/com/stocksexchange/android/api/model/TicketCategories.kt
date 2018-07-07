package com.stocksexchange.android.api.model

/**
 * An enumeration of all possible ticket categories.
 */
enum class TicketCategories(val id: Int) {

    ORDER_PROBLEM(1),
    DEPOSIT_PROBLEM(2),
    WITHDRAWAL_PROBLEM(3),
    DIVIDEND_PROBLEM(4),
    OTHER(5)

}