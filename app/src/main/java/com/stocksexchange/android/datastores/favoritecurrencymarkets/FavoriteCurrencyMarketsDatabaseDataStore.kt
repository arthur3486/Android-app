package com.stocksexchange.android.datastores.favoritecurrencymarkets

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.database.daos.FavoriteCurrencyMarketDao
import com.stocksexchange.android.database.mappings.mapToDatabaseFavoriteCurrencyMarket
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.helpers.executeBackgroundOperation
import com.stocksexchange.android.utils.helpers.performBackgroundOperation

class FavoriteCurrencyMarketsDatabaseDataStore(
    private val favoriteCurrencyMarketDao: FavoriteCurrencyMarketDao
) : FavoriteCurrencyMarketsDataStore {


    override suspend fun favorite(currencyMarket: CurrencyMarket) {
        executeBackgroundOperation {
            favoriteCurrencyMarketDao.insert(currencyMarket.mapToDatabaseFavoriteCurrencyMarket())
        }
    }


    override suspend fun unfavorite(currencyMarket: CurrencyMarket) {
        executeBackgroundOperation {
            favoriteCurrencyMarketDao.delete(currencyMarket.id)
        }
    }


    override suspend fun getAll(): Result<List<Long>> {
        return performBackgroundOperation {
            favoriteCurrencyMarketDao.getAll()
        }
    }


}