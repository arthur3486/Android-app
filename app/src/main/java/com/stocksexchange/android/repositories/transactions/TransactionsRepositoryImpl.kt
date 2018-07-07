package com.stocksexchange.android.repositories.transactions

import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.datastores.transactions.TransactionsDataStore
import com.stocksexchange.android.datastores.transactions.TransactionsDatabaseDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.model.TransactionParameters
import com.stocksexchange.android.model.TransactionTypes
import com.stocksexchange.android.utils.providers.ConnectionProvider
import com.stocksexchange.android.repositories.BaseRepository
import com.stocksexchange.android.utils.helpers.handleRepositoryResult

class TransactionsRepositoryImpl(
    private val serverDataStore: TransactionsDataStore,
    private val databaseDataStore: TransactionsDatabaseDataStore,
    private val connectionProvider: ConnectionProvider
) : BaseRepository<TransactionsCache>(), TransactionsRepository {


    override val cache: TransactionsCache = TransactionsCache()




    override suspend fun save(params: TransactionParameters, transactions: List<Transaction>) {
        databaseDataStore.save(transactions)
        saveTransactionsToCahce(params, transactions)
    }


    override suspend fun delete(type: String) {
        databaseDataStore.delete(type)
    }


    override suspend fun deleteAll() {
        val depositsType = TransactionTypes.DEPOSITS.name
        val withdrawalsType = TransactionTypes.WITHDRAWALS.name

        databaseDataStore.delete(depositsType)
        databaseDataStore.delete(withdrawalsType)

        if(cache.hasTransactions(depositsType)) {
            cache.removeTransactions(depositsType)
        }

        if(cache.hasTransactions(withdrawalsType)) {
            cache.removeTransactions(withdrawalsType)
        }
    }


    override suspend fun search(params: TransactionParameters): RepositoryResult<List<Transaction>> {
        val result = get(params)

        // Making sure that the data is present since the search is performed
        // solely on database records
        return if(result.isSuccessful()) {
            RepositoryResult(databaseResult = databaseDataStore.search(params))
        } else {
            result
        }
    }


    override suspend fun get(params: TransactionParameters): RepositoryResult<List<Transaction>> {
        val result = RepositoryResult<List<Transaction>>()

        if(!cache.hasTransactions(params.toString()) || cache.isInvalidated) {
            var onSuccess: suspend ((Result.Success<List<Transaction>>) -> Unit) = {}

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.get(params)
                onSuccess = {
                    // Deleting the old transactions to remove the redundant ones
                    // since the new data set may not already have them
                    delete(params.type.name)
                    save(params, it.value)
                }
            }

            if(result.isServerResultErroneous(true)) {
                result.databaseResult = databaseDataStore.get(params)
                onSuccess = { saveTransactionsToCahce(params, it.value) }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getTransactions(params.toString()))
        }

        return result
    }


    private fun saveTransactionsToCahce(params: TransactionParameters, transactions: List<Transaction>) {
        cache.saveTransactions(params.toString(), transactions)
    }


}