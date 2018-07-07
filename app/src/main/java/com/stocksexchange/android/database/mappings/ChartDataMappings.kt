package com.stocksexchange.android.database.mappings

import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.database.model.DatabaseChartData

fun ChartData.mapToDatabaseChartData(): DatabaseChartData {
    return DatabaseChartData(
        marketName = marketName,
        interval = interval,
        order = order,
        startDate = startDate,
        endDate = endDate,
        count = count,
        pagesCount = pagesCount,
        currentPage = currentPage,
        candleSticks = candleSticks
    )
}


fun DatabaseChartData.mapToChartData(): ChartData {
    return ChartData(
        marketName = marketName,
        interval = interval,
        order = order,
        startDate = startDate,
        endDate = endDate,
        count = count,
        pagesCount = pagesCount,
        currentPage = currentPage,
        candleSticks = candleSticks
    )
}