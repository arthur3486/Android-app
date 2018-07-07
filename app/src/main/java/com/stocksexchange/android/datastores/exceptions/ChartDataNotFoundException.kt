package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever the data for a chart
 * has not been found.
 */
class ChartDataNotFoundException(
    message: String = "",
    throwable: Throwable? = null
) : NotFoundException(message, throwable)