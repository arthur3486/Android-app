package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever transactions have
 * not been found.
 */
class TransactionsNotFoundException(
    message: String = "",
    throwable: Throwable? = null
) : NotFoundException(message, throwable)