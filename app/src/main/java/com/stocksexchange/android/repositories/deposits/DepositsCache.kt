package com.stocksexchange.android.repositories.deposits

import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.repositories.BaseRepositoryCache

class DepositsCache : BaseRepositoryCache() {


    fun saveDeposit(key: String, deposit: Deposit) {
        cache.put(key, deposit)
    }


    fun getDeposit(key: String): Deposit {
        return (cache.get(key) as Deposit)
    }


    fun hasDeposit(key: String): Boolean {
        return cache.contains(key)
    }


}