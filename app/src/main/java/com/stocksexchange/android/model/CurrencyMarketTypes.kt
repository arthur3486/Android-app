package com.stocksexchange.android.model

/**
 * An enumeration of all possible market types.
 */
enum class CurrencyMarketTypes(
    val marketName: String = ""
) {

    BTC("Bitcoin"),
    USDT("Tether"),
    NXT("NXT"),
    LTC("Litecoin"),
    ETH("Ethereum"),
    TUSD("TrueUSD"),

    FAVORITES,
    SEARCH

}