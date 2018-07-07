package com.stocksexchange.android.datastores.favoritecurrencymarkets

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.model.Result

interface FavoriteCurrencyMarketsDataStore {

    suspend fun favorite(currencyMarket: CurrencyMarket)

    suspend fun unfavorite(currencyMarket: CurrencyMarket)

    suspend fun getAll(): Result<List<Long>>

}