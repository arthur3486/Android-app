package com.stocksexchange.android.repositories.chartsdata

import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.repositories.BaseRepositoryCache

class ChartsDataCache : BaseRepositoryCache() {


    fun saveChartData(key: String, chartData: ChartData) {
        cache.put(key, chartData)
    }


    fun getChartData(key: String): ChartData {
        return (cache.get(key) as ChartData)
    }


    fun hasChartData(key: String): Boolean {
        return cache.contains(key)
    }


}