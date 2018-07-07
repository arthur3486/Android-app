package com.stocksexchange.android.repositories.chartsdata

import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.datastores.chartsdata.ChartsDataDataStore
import com.stocksexchange.android.datastores.chartsdata.ChartsDataDatabaseDataStore
import com.stocksexchange.android.model.ChartParameters
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.providers.ConnectionProvider
import com.stocksexchange.android.repositories.BaseRepository
import com.stocksexchange.android.utils.helpers.handleRepositoryResult

class ChartsDataRepositoryImpl(
    private val serverDataStore: ChartsDataDataStore,
    private val databaseDataStore: ChartsDataDatabaseDataStore,
    private val connectionProvider: ConnectionProvider
) : BaseRepository<ChartsDataCache>(), ChartsDataRepository {


    override val cache = ChartsDataCache()




    override suspend fun save(params: ChartParameters, chartData: ChartData) {
        databaseDataStore.save(chartData)
        saveToCache(params, chartData)
    }


    override suspend fun get(params: ChartParameters): RepositoryResult<ChartData> {
        val result = RepositoryResult<ChartData>()

        if(!cache.hasChartData(params.toString()) || cache.isInvalidated) {
            var onSuccess: suspend ((Result.Success<ChartData>) -> Unit) = {}

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.get(params)
                onSuccess = { save(params, it.value) }
            }

            if(result.isServerResultErroneous(true)) {
                result.databaseResult = databaseDataStore.get(params)
                onSuccess = { saveToCache(params, it.value) }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getChartData(params.toString()))
        }

        return result
    }


    private fun saveToCache(params: ChartParameters, data: ChartData) {
        cache.saveChartData(params.toString(), data)
    }


}