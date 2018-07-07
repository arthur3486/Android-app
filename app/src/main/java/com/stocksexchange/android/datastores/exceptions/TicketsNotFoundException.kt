package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever tickets have
 * not been found.
 */
class TicketsNotFoundException(
    message: String = "",
    throwable: Throwable? = null
) : NotFoundException(message, throwable)