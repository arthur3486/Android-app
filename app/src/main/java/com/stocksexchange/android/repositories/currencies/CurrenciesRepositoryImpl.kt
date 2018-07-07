package com.stocksexchange.android.repositories.currencies

import com.stocksexchange.android.api.model.Currency
import com.stocksexchange.android.datastores.currencies.CurrenciesDataStore
import com.stocksexchange.android.datastores.currencies.CurrenciesDatabaseDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.providers.ConnectionProvider
import com.stocksexchange.android.repositories.BaseRepository
import com.stocksexchange.android.utils.helpers.handleRepositoryResult

class CurrenciesRepositoryImpl(
    private val serverDataStore: CurrenciesDataStore,
    private val databaseDataStore: CurrenciesDatabaseDataStore,
    private val connectionProvider: ConnectionProvider
) : BaseRepository<CurrenciesCache>(), CurrenciesRepository {


    override val cache: CurrenciesCache = CurrenciesCache()




    override suspend fun save(currencies: List<Currency>) {
        databaseDataStore.save(currencies)
        saveCurrenciesToCache(currencies)
    }


    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
    }


    override suspend fun search(query: String): RepositoryResult<List<Currency>> {
        val result = getAll()

        // Making sure that the data is present since the search is performed
        // solely on database records
        return if(result.isSuccessful()) {
            RepositoryResult(databaseResult = databaseDataStore.search(query))
        } else {
            result
        }
    }


    override suspend fun getAll(): RepositoryResult<List<Currency>> {
        val result = RepositoryResult<List<Currency>>()

        if(cache.isEmpty() || cache.isInvalidated) {
            var onSuccess: suspend ((Result.Success<List<Currency>>) -> Unit) = {}

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.getAll()
                onSuccess = {
                    // Getting rid of old ones since quite a few could be no
                    // longer active and it would be a mistake to show them
                    // to the user in the future database loads (e.g., when
                    // the network is not available)
                    deleteAll()
                    save(it.value)
                }
            }

            if(result.isServerResultErroneous(true)) {
                result.databaseResult = databaseDataStore.getAll()
                onSuccess = { saveCurrenciesToCache(it.value) }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getCurrencies())
        }

        return result
    }


    private fun saveCurrenciesToCache(currencies: List<Currency>) {
        cache.saveCurrencies(currencies)
    }


}