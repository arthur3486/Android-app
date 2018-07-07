package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever the settings
 * have not been found.
 */
class SettingsNotFoundException(
    message: String = "",
    throwable: Throwable? = null
) : NotFoundException(message, throwable)