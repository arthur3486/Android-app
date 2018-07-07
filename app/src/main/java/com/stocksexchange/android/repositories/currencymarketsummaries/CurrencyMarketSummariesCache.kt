package com.stocksexchange.android.repositories.currencymarketsummaries

import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.repositories.BaseRepositoryCache

class CurrencyMarketSummariesCache : BaseRepositoryCache() {


    fun saveCurrencyMarketSummary(key: String, currencyMarketSummary: CurrencyMarketSummary) {
        cache.put(key, currencyMarketSummary)
    }


    fun getCurrencyMarketSummary(key: String): CurrencyMarketSummary {
        return (cache.get(key) as CurrencyMarketSummary)
    }


    fun hasCurrencyMarketSummary(key: String): Boolean {
        return cache.contains(key)
    }


}