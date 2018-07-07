package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever the currency market
 * summary has not been found.
 */
class CurrencyMarketSummaryNotFoundException(
    message: String = "",
    throwable: Throwable? = null
) : NotFoundException(message, throwable)