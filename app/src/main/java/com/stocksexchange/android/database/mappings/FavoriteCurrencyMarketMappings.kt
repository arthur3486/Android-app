package com.stocksexchange.android.database.mappings

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.database.model.DatabaseFavoriteCurrencyMarket

fun CurrencyMarket.mapToDatabaseFavoriteCurrencyMarket(): DatabaseFavoriteCurrencyMarket {
    return DatabaseFavoriteCurrencyMarket(
        id = id
    )
}