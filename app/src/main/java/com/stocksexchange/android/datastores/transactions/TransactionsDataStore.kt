package com.stocksexchange.android.datastores.transactions

import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.model.TransactionParameters

interface TransactionsDataStore {

    suspend fun save(transactions: List<Transaction>)

    suspend fun delete(type: String)

    suspend fun search(params: TransactionParameters): Result<List<Transaction>>

    suspend fun get(params: TransactionParameters): Result<List<Transaction>>

}