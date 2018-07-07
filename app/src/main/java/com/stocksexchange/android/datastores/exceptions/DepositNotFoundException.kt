package com.stocksexchange.android.datastores.exceptions

/**
 * An exception to throw whenever the deposit
 * has not been found.
 */
class DepositNotFoundException(
    message: String = "",
    throwable: Throwable? = null
) : NotFoundException(message, throwable)