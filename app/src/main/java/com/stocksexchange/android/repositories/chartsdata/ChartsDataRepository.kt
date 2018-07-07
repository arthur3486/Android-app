package com.stocksexchange.android.repositories.chartsdata

import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.model.ChartParameters
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.repositories.Repository

interface ChartsDataRepository : Repository {

    suspend fun save(params: ChartParameters, chartData: ChartData)

    suspend fun get(params: ChartParameters): RepositoryResult<ChartData>

}