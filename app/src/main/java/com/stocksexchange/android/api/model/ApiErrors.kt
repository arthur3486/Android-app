package com.stocksexchange.android.api.model

/**
 * An enumeration of all errors that API can return.
 */
enum class ApiErrors(val message: String) {

    NO_WALLET("No Wallet"),
    NO_WALLET_ADDRESS("No Wallet Address"),
    INVALID_CURRENCY_CODE("Invalid Currency Code"),
    CURRENCY_DISABLED("Selected Currency is disabled")

}