package com.stocksexchange.android.api.exceptions

/**
 * Gets thrown whenever an API error occurred.
 */
open class ApiException(
    message: String = "",
    throwable: Throwable? = null
) : Exception(message, throwable)