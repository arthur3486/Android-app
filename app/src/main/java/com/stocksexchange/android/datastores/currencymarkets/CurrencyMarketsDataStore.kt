package com.stocksexchange.android.datastores.currencymarkets

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.model.Result

interface CurrencyMarketsDataStore {

    suspend fun save(currencyMarket: CurrencyMarket)

    suspend fun save(currencyMarkets: List<CurrencyMarket>)

    suspend fun deleteAll()

    suspend fun search(query: String): Result<List<CurrencyMarket>>

    suspend fun get(ids: List<Long>): Result<List<CurrencyMarket>>

    suspend fun getAll(): Result<List<CurrencyMarket>>

}