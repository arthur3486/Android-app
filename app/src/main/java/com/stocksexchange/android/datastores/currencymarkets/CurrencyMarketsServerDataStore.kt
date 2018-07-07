package com.stocksexchange.android.datastores.currencymarkets

import com.stocksexchange.android.api.StocksExchangeService
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.utils.fetchData
import com.stocksexchange.android.model.Result
import ru.gildor.coroutines.retrofit.awaitResult
import ru.gildor.coroutines.retrofit.Result as RetrofitResult

class CurrencyMarketsServerDataStore(
    private val stocksExchangeService: StocksExchangeService
) : CurrencyMarketsDataStore {


    override suspend fun save(currencyMarket: CurrencyMarket) {
        throw UnsupportedOperationException()
    }


    override suspend fun save(currencyMarkets: List<CurrencyMarket>) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun search(query: String): Result<List<CurrencyMarket>> {
        throw UnsupportedOperationException()
    }


    override suspend fun get(ids: List<Long>): Result<List<CurrencyMarket>> {
        throw UnsupportedOperationException()
    }


    override suspend fun getAll(): Result<List<CurrencyMarket>> {
        return stocksExchangeService
            .getCurrencyMarkets()
            .awaitResult()
            .fetchData()
    }


}