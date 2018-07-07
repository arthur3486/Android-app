package com.stocksexchange.android.api.exceptions

/**
 * Gets thrown whenever a wallet address does not exist.
 */
class NoWalletAddressException(
    message: String = "",
    throwable: Throwable? = null
) : ApiException(message, throwable)