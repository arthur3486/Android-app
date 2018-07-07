package com.stocksexchange.android.repositories.currencies

import com.stocksexchange.android.api.model.Currency
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.repositories.Repository

interface CurrenciesRepository : Repository {

    suspend fun save(currencies: List<Currency>)

    suspend fun deleteAll()

    suspend fun search(query: String): RepositoryResult<List<Currency>>

    suspend fun getAll(): RepositoryResult<List<Currency>>

}