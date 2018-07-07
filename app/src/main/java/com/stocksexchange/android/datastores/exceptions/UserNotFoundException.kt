package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever a user has not been found.
 */
class UserNotFoundException(
    message: String = "",
    throwable: Throwable? = null
) : NotFoundException(message, throwable)