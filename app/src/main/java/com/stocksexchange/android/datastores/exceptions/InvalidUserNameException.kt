package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever the user's name
 * is invalid.
 */
class InvalidUserNameException(
    message: String? = "",
    throwable: Throwable? = null
) : IllegalStateException(message, throwable)