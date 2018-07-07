package com.stocksexchange.android.repositories.currencymarketsummaries

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.repositories.Repository

interface CurrencyMarketSummariesRepository : Repository {

    suspend fun save(currencyMarketSummary: CurrencyMarketSummary)

    suspend fun get(currencyMarket: CurrencyMarket): RepositoryResult<CurrencyMarketSummary>

}