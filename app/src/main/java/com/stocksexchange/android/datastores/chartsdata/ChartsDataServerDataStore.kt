package com.stocksexchange.android.datastores.chartsdata

import com.stocksexchange.android.api.StocksExchangeService
import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.api.utils.extractDataDirectly
import com.stocksexchange.android.model.ChartParameters
import com.stocksexchange.android.model.Result
import ru.gildor.coroutines.retrofit.awaitResult
import ru.gildor.coroutines.retrofit.Result as RetrofitResult

class ChartsDataServerDataStore(
    private val stocksExchangeService: StocksExchangeService
) : ChartsDataDataStore {


    override suspend fun save(chartData: ChartData) {
        throw UnsupportedOperationException()
    }


    override suspend fun get(params: ChartParameters): Result<ChartData> {
        return stocksExchangeService.getChartData(
            params.marketName, params.interval.intervalName,
            params.sortType.name, params.count, params.page
        ).awaitResult().extractDataDirectly()
    }


}