package com.stocksexchange.android.datastores.deposits

import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.database.daos.DepositDao
import com.stocksexchange.android.datastores.exceptions.DepositNotFoundException
import com.stocksexchange.android.database.mappings.mapToDatabaseDeposit
import com.stocksexchange.android.database.mappings.mapToDeposit
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.helpers.executeBackgroundOperation
import com.stocksexchange.android.utils.helpers.performBackgroundOperation

class DepositsDatabaseDataStore(
    private val depositDao: DepositDao
) : DepositsDataStore {


    override suspend fun save(deposit: Deposit) {
        executeBackgroundOperation {
            depositDao.insert(deposit.mapToDatabaseDeposit())
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            depositDao.deleteAll()
        }
    }


    override suspend fun generateWalletAddress(currency: String): Result<Deposit> {
        throw UnsupportedOperationException()
    }


    override suspend fun get(currency: String): Result<Deposit> {
        return performBackgroundOperation {
            depositDao.get(currency)?.mapToDeposit() ?: throw DepositNotFoundException()
        }
    }


}