package com.stocksexchange.android.datastores.currencymarketsummaries

import com.stocksexchange.android.api.StocksExchangeService
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.api.utils.fetchData
import com.stocksexchange.android.model.Result
import ru.gildor.coroutines.retrofit.awaitResult
import ru.gildor.coroutines.retrofit.Result as RetrofitResult

class CurrencyMarketSummariesServerDataStore(
    private val stocksExchangeService: StocksExchangeService
) : CurrencyMarketSummariesDataStore {


    override suspend fun save(currencyMarketSummary: CurrencyMarketSummary) {
        throw UnsupportedOperationException()
    }


    override suspend fun get(currencyMarket: CurrencyMarket): Result<CurrencyMarketSummary> {
        return stocksExchangeService.getCurrencyMarketSummary(
            currencyMarket.currency,
            currencyMarket.market
        ).awaitResult().fetchData()
    }


}