package com.stocksexchange.android.utils.exceptions

/**
 * An exception to throw whenever there is no internet.
 */
class NoInternetException(
    message: String? = "",
    throwable: Throwable? = null
) : Exception(message, throwable)