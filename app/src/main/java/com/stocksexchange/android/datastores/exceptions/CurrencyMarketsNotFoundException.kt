package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever currency markets
 * have not been found.
 */
class CurrencyMarketsNotFoundException(
    message: String = "",
    throwable: Throwable? = null
) : NotFoundException(message, throwable)