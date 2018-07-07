package com.stocksexchange.android.database.mappings

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.database.model.DatabaseCurrencyMarket

fun CurrencyMarket.mapToDatabaseCurrencyMarket(): DatabaseCurrencyMarket {
    return DatabaseCurrencyMarket(
        id = id,
        name = name,
        currency = currency,
        market = market,
        minOrderAmount = minOrderAmount,
        dailyBuyMaxPrice = dailyBuyMaxPrice,
        dailySellMinPrice = dailySellMinPrice,
        lastPrice = lastPrice,
        lastPriceDayAgo = lastPriceDayAgo,
        dailyVolume = dailyVolume,
        spread = spread,
        buyFeePercent = buyFeePercent,
        sellFeePercent = sellFeePercent
    )
}


fun List<CurrencyMarket>.mapToDatabaseCurrencyMarketList(): List<DatabaseCurrencyMarket> {
    return map { it.mapToDatabaseCurrencyMarket() }
}


fun List<CurrencyMarket>.mapToNameMarketMap(): Map<String, CurrencyMarket> {
    val map: MutableMap<String, CurrencyMarket> = mutableMapOf()

    for(currencyMarket in this) {
        map[currencyMarket.name] = currencyMarket
    }

    return map
}


fun DatabaseCurrencyMarket.mapToCurrencyMarket(): CurrencyMarket {
    return CurrencyMarket(
        id = id,
        name = name,
        minOrderAmount = minOrderAmount,
        dailyBuyMaxPrice = dailyBuyMaxPrice,
        dailySellMinPrice = dailySellMinPrice,
        lastPrice = lastPrice,
        lastPriceDayAgo = lastPriceDayAgo,
        dailyVolume = dailyVolume,
        spread = spread,
        buyFeePercent = buyFeePercent,
        sellFeePercent = sellFeePercent
    )
}


fun List<DatabaseCurrencyMarket>.mapToCurrencyMarketList(): List<CurrencyMarket> {
    return map { it.mapToCurrencyMarket() }
}