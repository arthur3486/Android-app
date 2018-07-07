package com.stocksexchange.android.repositories.deposits

import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.repositories.Repository

interface DepositsRepository : Repository {

    suspend fun save(currency: String, deposit: Deposit)

    suspend fun deleteAll()

    suspend fun generateWalletAddress(currency: String): RepositoryResult<Deposit>

    suspend fun get(currency: String): RepositoryResult<Deposit>

}