package com.stocksexchange.android.database.mappings

import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.database.model.DatabaseCurrencyMarketSummary

fun CurrencyMarketSummary.mapToDatabaseCurrencyMarketSummary(): DatabaseCurrencyMarketSummary {
    return DatabaseCurrencyMarketSummary(
        name = name,
        currency = currency,
        currencyName = currencyName,
        market = market,
        marketName = marketName,
        minOrderAmount = minOrderAmount,
        minBuyPrice = minBuyPrice,
        minSellPrice = minSellPrice,
        buyFeePercent = buyFeePercent,
        sellFeePercent = sellFeePercent,
        isActive = isActive
    )
}


fun DatabaseCurrencyMarketSummary.mapToCurrencyMarketSummary(): CurrencyMarketSummary {
    return CurrencyMarketSummary(
        name = name,
        currency = currency,
        currencyName = currencyName,
        market = market,
        marketName = marketName,
        minOrderAmount = minOrderAmount,
        minBuyPrice = minBuyPrice,
        minSellPrice = minSellPrice,
        buyFeePercent = buyFeePercent,
        sellFeePercent = sellFeePercent,
        isActive = isActive
    )
}