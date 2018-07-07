package com.stocksexchange.android.api.exceptions

/**
 * Gets thrown whenever the credentials supplied for a request are invalid.
 */
class InvalidCredentialsException(
    message: String = "",
    cause: Throwable? = null
) : IllegalStateException(message, cause)