package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever orders have not
 * been found.
 */
class OrdersNotFoundException(
    message: String = "",
    throwable: Throwable? = null
) : NotFoundException(message, throwable)