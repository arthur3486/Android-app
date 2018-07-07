package com.stocksexchange.android.datastores.currencies

import com.stocksexchange.android.api.StocksExchangeService
import com.stocksexchange.android.api.model.Currency
import com.stocksexchange.android.api.utils.fetchData
import com.stocksexchange.android.model.Result
import ru.gildor.coroutines.retrofit.awaitResult

class CurrenciesServerDataStore(
    private val stocksExchangeService: StocksExchangeService
) : CurrenciesDataStore {


    override suspend fun save(currencies: List<Currency>) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun search(query: String): Result<List<Currency>> {
        throw UnsupportedOperationException()
    }


    override suspend fun getAll(): Result<List<Currency>> {
        return stocksExchangeService
            .getCurrencies()
            .awaitResult()
            .fetchData()
    }


}