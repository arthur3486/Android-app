package com.stocksexchange.android.datastores.currencymarkets

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.database.daos.CurrencyMarketDao
import com.stocksexchange.android.datastores.exceptions.CurrencyMarketsNotFoundException
import com.stocksexchange.android.database.mappings.mapToCurrencyMarketList
import com.stocksexchange.android.database.mappings.mapToDatabaseCurrencyMarket
import com.stocksexchange.android.database.mappings.mapToDatabaseCurrencyMarketList
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.helpers.executeBackgroundOperation
import com.stocksexchange.android.utils.helpers.performBackgroundOperation

class CurrencyMarketsDatabaseDataStore(
    private val currencyMarketDao: CurrencyMarketDao
) : CurrencyMarketsDataStore {


    override suspend fun save(currencyMarket: CurrencyMarket) {
        executeBackgroundOperation {
            currencyMarket.mapToDatabaseCurrencyMarket().let {
                currencyMarketDao.insert(it)
            }
        }
    }


    override suspend fun save(currencyMarkets: List<CurrencyMarket>) {
        executeBackgroundOperation {
            currencyMarkets
                .mapToDatabaseCurrencyMarketList()
                .let { currencyMarketDao.insert(it) }
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            currencyMarketDao.deleteAll()
        }
    }


    override suspend fun search(query: String): Result<List<CurrencyMarket>> {
        return performBackgroundOperation {
            currencyMarketDao.search(query.toLowerCase()).mapToCurrencyMarketList()
        }
    }


    override suspend fun get(ids: List<Long>): Result<List<CurrencyMarket>> {
        return performBackgroundOperation {
            currencyMarketDao.get(ids).mapToCurrencyMarketList()
        }
    }


    override suspend fun getAll(): Result<List<CurrencyMarket>> {
        return performBackgroundOperation {
            currencyMarketDao.getAll().mapToCurrencyMarketList()
                .takeUnless { it.isEmpty() }.let { it }
                ?: throw CurrencyMarketsNotFoundException()
        }
    }


}