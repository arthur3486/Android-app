package com.stocksexchange.android.datastores.currencies

import com.stocksexchange.android.api.model.Currency
import com.stocksexchange.android.model.Result

interface CurrenciesDataStore {

    suspend fun save(currencies: List<Currency>)

    suspend fun deleteAll()

    suspend fun search(query: String): Result<List<Currency>>

    suspend fun getAll(): Result<List<Currency>>

}