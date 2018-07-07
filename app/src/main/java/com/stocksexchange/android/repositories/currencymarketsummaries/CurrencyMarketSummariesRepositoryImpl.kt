package com.stocksexchange.android.repositories.currencymarketsummaries

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.datastores.currencymarketsummaries.CurrencyMarketSummariesDataStore
import com.stocksexchange.android.datastores.currencymarketsummaries.CurrencyMarketSummariesDatabaseDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.providers.ConnectionProvider
import com.stocksexchange.android.repositories.BaseRepository
import com.stocksexchange.android.utils.helpers.handleRepositoryResult

class CurrencyMarketSummariesRepositoryImpl(
    private val serverDataStore: CurrencyMarketSummariesDataStore,
    private val databaseDataStore: CurrencyMarketSummariesDatabaseDataStore,
    private val connectionProvider: ConnectionProvider
) : BaseRepository<CurrencyMarketSummariesCache>(), CurrencyMarketSummariesRepository {


    override val cache = CurrencyMarketSummariesCache()




    override suspend fun save(currencyMarketSummary: CurrencyMarketSummary) {
        databaseDataStore.save(currencyMarketSummary)
        saveToCache(currencyMarketSummary)
    }


    override suspend fun get(currencyMarket: CurrencyMarket): RepositoryResult<CurrencyMarketSummary> {
        val result = RepositoryResult<CurrencyMarketSummary>()

        if(!cache.hasCurrencyMarketSummary(currencyMarket.name) || cache.isInvalidated) {
            var onSuccess: suspend ((Result.Success<CurrencyMarketSummary>) -> Unit) = {}

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.get(currencyMarket)
                onSuccess = { save(it.value) }
            }

            if(result.isServerResultErroneous(true)) {
                result.databaseResult = databaseDataStore.get(currencyMarket)
                onSuccess = { saveToCache(it.value) }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getCurrencyMarketSummary(currencyMarket.name))
        }

        return result
    }


    private fun saveToCache(summary: CurrencyMarketSummary) {
        cache.saveCurrencyMarketSummary(summary.name, summary)
    }


}