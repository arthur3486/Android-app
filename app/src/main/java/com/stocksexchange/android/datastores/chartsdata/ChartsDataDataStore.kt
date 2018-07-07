package com.stocksexchange.android.datastores.chartsdata

import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.model.ChartParameters
import com.stocksexchange.android.model.Result

interface ChartsDataDataStore {

    suspend fun save(chartData: ChartData)

    suspend fun get(params: ChartParameters): Result<ChartData>

}