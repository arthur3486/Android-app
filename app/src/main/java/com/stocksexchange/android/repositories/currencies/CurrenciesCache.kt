package com.stocksexchange.android.repositories.currencies

import com.stocksexchange.android.api.model.Currency
import com.stocksexchange.android.repositories.BaseRepositoryCache

class CurrenciesCache : BaseRepositoryCache() {


    companion object {

        private const val KEY_CURRENCIES = "currencies"

    }




    fun saveCurrencies(currencies: List<Currency>) {
        cache.put(KEY_CURRENCIES, currencies)
    }


    @Suppress("UNCHECKED_CAST")
    fun getCurrencies(): List<Currency> {
        return (cache.get(KEY_CURRENCIES) as List<Currency>)
    }


    fun hasCurrencies(): Boolean {
        return cache.contains(KEY_CURRENCIES)
    }


}