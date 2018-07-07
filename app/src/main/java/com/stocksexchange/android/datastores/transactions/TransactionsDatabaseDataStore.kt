package com.stocksexchange.android.datastores.transactions

import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.database.daos.TransactionDao
import com.stocksexchange.android.datastores.exceptions.TransactionsNotFoundException
import com.stocksexchange.android.database.mappings.mapToDatabaseTransactionList
import com.stocksexchange.android.database.mappings.mapToTransactionList
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.model.SortTypes
import com.stocksexchange.android.model.TransactionParameters
import com.stocksexchange.android.utils.helpers.executeBackgroundOperation
import com.stocksexchange.android.utils.helpers.performBackgroundOperation

class TransactionsDatabaseDataStore(
    private val transactionDao: TransactionDao
) : TransactionsDataStore {


    override suspend fun save(transactions: List<Transaction>) {
        executeBackgroundOperation {
            transactionDao.insert(transactions.mapToDatabaseTransactionList())
        }
    }


    override suspend fun delete(type: String) {
        executeBackgroundOperation {
            transactionDao.delete(type)
        }
    }


    override suspend fun search(params: TransactionParameters): Result<List<Transaction>> {
        return performBackgroundOperation {
            val type = params.type.name
            val searchQuery = params.searchQuery.toLowerCase()
            val count = params.count

            when(params.sortType) {
                SortTypes.ASC -> transactionDao.searchAsc(type, searchQuery, count)
                SortTypes.DESC -> transactionDao.searchDesc(type, searchQuery, count)
            }.mapToTransactionList()
        }
    }


    override suspend fun get(params: TransactionParameters): Result<List<Transaction>> {
        return performBackgroundOperation {
            when(params.sortType) {
                SortTypes.ASC -> transactionDao.getAsc(params.type.name, params.count)
                SortTypes.DESC -> transactionDao.getDesc(params.type.name, params.count)
            }.mapToTransactionList()
                .takeUnless { it.isEmpty() }.let { it }
                ?: throw TransactionsNotFoundException()
        }
    }


}