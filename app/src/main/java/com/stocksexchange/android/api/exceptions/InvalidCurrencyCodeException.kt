package com.stocksexchange.android.api.exceptions

/**
 * Gets thrown whenever the currency code is invalid.
 */
class InvalidCurrencyCodeException(
    message: String = "",
    throwable: Throwable? = null
) : ApiException(message, throwable)