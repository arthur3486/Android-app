package com.stocksexchange.android.repositories.transactions

import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.repositories.BaseRepositoryCache

class TransactionsCache : BaseRepositoryCache() {



    fun saveTransactions(key: String, transactions: List<Transaction>) {
        cache.put(key, transactions)
    }


    fun removeTransactions(key: String) {
        cache.remove(key)
    }


    @Suppress("UNCHECKED_CAST")
    fun getTransactions(key: String): List<Transaction> {
        return (cache.get(key) as List<Transaction>)
    }


    fun hasTransactions(key: String): Boolean {
        return cache.contains(key)
    }


}