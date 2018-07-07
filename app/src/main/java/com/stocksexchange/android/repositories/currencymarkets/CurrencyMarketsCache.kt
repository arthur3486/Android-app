package com.stocksexchange.android.repositories.currencymarkets

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.repositories.BaseRepositoryCache

class CurrencyMarketsCache : BaseRepositoryCache() {


    companion object {

        private const val KEY_CURRENCY_MARKETS = "currency_markets"
        private const val KEY_FAVORITE_CURRENCY_MARKETS = "favorite_currency_markets"

    }




    fun saveCurrencyMarkets(currencyMarkets: List<CurrencyMarket>) {
        cache.put(KEY_CURRENCY_MARKETS, currencyMarkets)
    }


    @Suppress("UNCHECKED_CAST")
    fun getCurrencyMarkets(): List<CurrencyMarket> {
        return (cache.get(KEY_CURRENCY_MARKETS) as List<CurrencyMarket>)
    }


    fun hasCurrencyMarkets(): Boolean {
        return cache.contains(KEY_CURRENCY_MARKETS)
    }


    fun saveFavoriteCurrencyMarkets(currencyMarkets: List<CurrencyMarket>) {
        cache.put(KEY_FAVORITE_CURRENCY_MARKETS, currencyMarkets)
    }


    @Suppress("UNCHECKED_CAST")
    fun getFavoriteCurrencyMarkets(): List<CurrencyMarket> {
        return (cache.get(KEY_FAVORITE_CURRENCY_MARKETS) as List<CurrencyMarket>)
    }


    fun hasFavoriteCurrencyMarkets(): Boolean {
        return cache.contains(KEY_FAVORITE_CURRENCY_MARKETS)
    }


}