package com.stocksexchange.android.api.model

/**
 * An enumeration of all possible history order statuses.
 */
enum class OrderStatuses(val id: Int) {

    PENDING(1),
    PROCESSING(2),
    FINISHED(3),
    CANCELLED(4)

}