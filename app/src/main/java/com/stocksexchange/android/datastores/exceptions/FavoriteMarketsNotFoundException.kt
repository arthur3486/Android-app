package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever favorite markets
 * have not been found.
 */
class FavoriteMarketsNotFoundException(
    message: String = "",
    throwable: Throwable? = null
) : NotFoundException(message, throwable)