package com.stocksexchange.android.repositories.transactions

import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.TransactionParameters
import com.stocksexchange.android.repositories.Repository

interface TransactionsRepository : Repository {

    suspend fun save(params: TransactionParameters, transactions: List<Transaction>)

    suspend fun delete(type: String)

    suspend fun deleteAll()

    suspend fun search(params: TransactionParameters): RepositoryResult<List<Transaction>>

    suspend fun get(params: TransactionParameters): RepositoryResult<List<Transaction>>

}