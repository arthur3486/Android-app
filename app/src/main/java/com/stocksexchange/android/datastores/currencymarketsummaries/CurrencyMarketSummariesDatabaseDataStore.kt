package com.stocksexchange.android.datastores.currencymarketsummaries

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.database.daos.CurrencyMarketSummaryDao
import com.stocksexchange.android.datastores.exceptions.CurrencyMarketSummaryNotFoundException
import com.stocksexchange.android.database.mappings.mapToCurrencyMarketSummary
import com.stocksexchange.android.database.mappings.mapToDatabaseCurrencyMarketSummary
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.helpers.executeBackgroundOperation
import com.stocksexchange.android.utils.helpers.performBackgroundOperation

class CurrencyMarketSummariesDatabaseDataStore(
    private val currencyMarketSummaryDao: CurrencyMarketSummaryDao
) : CurrencyMarketSummariesDataStore {


    override suspend fun save(currencyMarketSummary: CurrencyMarketSummary) {
        executeBackgroundOperation {
            currencyMarketSummary
                .mapToDatabaseCurrencyMarketSummary()
                .let { currencyMarketSummaryDao.insert(it) }
        }
    }


    override suspend fun get(currencyMarket: CurrencyMarket): Result<CurrencyMarketSummary> {
        return performBackgroundOperation {
            currencyMarketSummaryDao.get(currencyMarket.currency, currencyMarket.market)
                ?.mapToCurrencyMarketSummary()
                ?: throw CurrencyMarketSummaryNotFoundException()
        }
    }


}