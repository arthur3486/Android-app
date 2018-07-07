package com.stocksexchange.android.datastores.deposits

import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.model.Result

interface DepositsDataStore {

    suspend fun save(deposit: Deposit)

    suspend fun deleteAll()

    suspend fun generateWalletAddress(currency: String): Result<Deposit>

    suspend fun get(currency: String): Result<Deposit>

}