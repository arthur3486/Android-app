package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever something has not
 * been found. Primarily used for extending.
 */
open class NotFoundException(
    message: String = "",
    throwable: Throwable? = null
) : Exception(message, throwable)