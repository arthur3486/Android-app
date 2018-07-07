package com.stocksexchange.android.api.exceptions

/**
 * Gets thrown whenever a currency is disabled.
 */
class CurrencyDisabledException(
    message: String = "",
    throwable: Throwable? = null
) : ApiException(message, throwable)