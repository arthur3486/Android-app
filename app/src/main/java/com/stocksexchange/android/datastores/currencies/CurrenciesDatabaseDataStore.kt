package com.stocksexchange.android.datastores.currencies

import com.stocksexchange.android.api.model.Currency
import com.stocksexchange.android.database.daos.CurrencyDao
import com.stocksexchange.android.database.mappings.mapToCurrencyList
import com.stocksexchange.android.database.mappings.mapToDatabaseCurrencyList
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.helpers.executeBackgroundOperation
import com.stocksexchange.android.utils.helpers.performBackgroundOperation

class CurrenciesDatabaseDataStore(
    private val currencyDao: CurrencyDao
) : CurrenciesDataStore {


    override suspend fun save(currencies: List<Currency>) {
        executeBackgroundOperation {
            currencies.mapToDatabaseCurrencyList().let {
                currencyDao.insert(it)
            }
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            currencyDao.deleteAll()
        }
    }


    override suspend fun search(query: String): Result<List<Currency>> {
        return performBackgroundOperation {
            currencyDao.search(query.toLowerCase()).mapToCurrencyList()
        }
    }


    override suspend fun getAll(): Result<List<Currency>> {
        return performBackgroundOperation {
            currencyDao.getAll().mapToCurrencyList()
        }
    }


}