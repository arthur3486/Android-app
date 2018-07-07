package com.stocksexchange.android.datastores.chartsdata

import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.database.daos.ChartDataDao
import com.stocksexchange.android.datastores.exceptions.ChartDataNotFoundException
import com.stocksexchange.android.database.mappings.mapToChartData
import com.stocksexchange.android.database.mappings.mapToDatabaseChartData
import com.stocksexchange.android.model.ChartParameters
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.helpers.executeBackgroundOperation
import com.stocksexchange.android.utils.helpers.performBackgroundOperation

class ChartsDataDatabaseDataStore(
    private val chartDataDao: ChartDataDao
) : ChartsDataDataStore {


    override suspend fun save(chartData: ChartData) {
        executeBackgroundOperation {
            chartData
                .mapToDatabaseChartData()
                .let { chartDataDao.insert(it) }
        }
    }


    override suspend fun get(params: ChartParameters): Result<ChartData> {
        return performBackgroundOperation {
            chartDataDao.get(
                params.marketName,
                params.interval.intervalName,
                params.sortType.name,
                params.count,
                params.page
            )?.mapToChartData() ?: throw ChartDataNotFoundException()
        }
    }


}