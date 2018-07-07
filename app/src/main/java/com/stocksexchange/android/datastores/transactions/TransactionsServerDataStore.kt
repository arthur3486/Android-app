package com.stocksexchange.android.datastores.transactions

import com.stocksexchange.android.api.StocksExchangeService
import com.stocksexchange.android.api.exceptions.InvalidCredentialsException
import com.stocksexchange.android.api.model.PrivateApiMethods
import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.api.utils.extractData
import com.stocksexchange.android.api.utils.generateNonce
import com.stocksexchange.android.database.mappings.mapToTransactionList
import com.stocksexchange.android.utils.handlers.CredentialsHandler
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.model.TransactionParameters
import ru.gildor.coroutines.retrofit.awaitResult

class TransactionsServerDataStore(
    private val stocksExchangeService: StocksExchangeService,
    private val credentialsHandler: CredentialsHandler
) : TransactionsDataStore {


    override suspend fun save(transactions: List<Transaction>) {
        throw UnsupportedOperationException()
    }


    override suspend fun delete(type: String) {
        throw UnsupportedOperationException()
    }


    override suspend fun search(params: TransactionParameters): Result<List<Transaction>> {
        throw UnsupportedOperationException()
    }


    override suspend fun get(params: TransactionParameters): Result<List<Transaction>> {
        return if(credentialsHandler.hasValidCredentials()) {
            stocksExchangeService.getTransactions(
                PrivateApiMethods.TRANSACTION_HISTORY.methodName,
                generateNonce(), params.currency, params.operation.name,
                params.status.name, params.sortType.name, params.count
            ).awaitResult().extractData { it.mapToTransactionList(params.type.name) }
        } else {
            Result.Failure(InvalidCredentialsException())
        }
    }


}