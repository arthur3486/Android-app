package com.stocksexchange.android.datastores.currencymarketsummaries

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.model.Result

interface CurrencyMarketSummariesDataStore {

    suspend fun save(currencyMarketSummary: CurrencyMarketSummary)

    suspend fun get(currencyMarket: CurrencyMarket): Result<CurrencyMarketSummary>

}