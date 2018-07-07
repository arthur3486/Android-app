package com.stocksexchange.android.repositories.deposits

import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.datastores.deposits.DepositsDataStore
import com.stocksexchange.android.datastores.deposits.DepositsDatabaseDataStore
import com.stocksexchange.android.utils.exceptions.NoInternetException
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.providers.ConnectionProvider
import com.stocksexchange.android.repositories.BaseRepository
import com.stocksexchange.android.utils.helpers.handleRepositoryResult

class DepositsRepositoryImpl(
    private val serverDataStore: DepositsDataStore,
    private val databaseDataStore: DepositsDatabaseDataStore,
    private val connectionProvider: ConnectionProvider
) : BaseRepository<DepositsCache>(), DepositsRepository {


    override val cache: DepositsCache = DepositsCache()




    override suspend fun save(currency: String, deposit: Deposit) {
        databaseDataStore.save(deposit)
        saveDepositToCache(currency, deposit)
    }


    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()

        if(!cache.isEmpty()) {
            cache.clear()
        }
    }


    override suspend fun generateWalletAddress(currency: String): RepositoryResult<Deposit> {
        return if(connectionProvider.isNetworkAvailable()) {
            RepositoryResult(serverResult = serverDataStore.generateWalletAddress(currency))
        } else {
            RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }
    }


    override suspend fun get(currency: String): RepositoryResult<Deposit> {
        val result = RepositoryResult<Deposit>()

        if(!cache.hasDeposit(currency) || cache.isInvalidated) {
            var onSuccess: suspend ((Result.Success<Deposit>) -> Unit) = {}

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.get(currency)
                onSuccess = { save(currency, it.value) }
            }

            if(result.isServerResultErroneous(true)) {
                result.databaseResult = databaseDataStore.get(currency)
                onSuccess = { saveDepositToCache(currency, it.value) }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getDeposit(currency))
        }

        return result
    }


    private fun saveDepositToCache(key: String, deposit: Deposit) {
        cache.saveDeposit(key, deposit)
    }


}