package com.stocksexchange.android.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import com.stocksexchange.android.api.model.CandleStick
import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.database.model.DatabaseChartData.Companion.INTERVAL
import com.stocksexchange.android.database.model.DatabaseChartData.Companion.MARKET_NAME
import com.stocksexchange.android.database.model.DatabaseChartData.Companion.TABLE_NAME

/**
 * A Room database model class of [ChartData] class.
 */
@Entity(tableName = TABLE_NAME, primaryKeys = [MARKET_NAME, INTERVAL])
data class DatabaseChartData(
    @ColumnInfo(name = MARKET_NAME) var marketName: String,
    @ColumnInfo(name = INTERVAL) var interval: String,
    @ColumnInfo(name = ORDER) var order: String,
    @ColumnInfo(name = START_DATE) var startDate: String,
    @ColumnInfo(name = END_DATE) var endDate: String,
    @ColumnInfo(name = COUNT) var count: Int,
    @ColumnInfo(name = PAGES_COUNT) var pagesCount: Int,
    @ColumnInfo(name = CURRENT_PAGE) var currentPage: Int,
    @ColumnInfo(name = CANDLE_STICKS) var candleSticks: List<CandleStick>
) {

    companion object {

        const val TABLE_NAME = "charts_data"

        const val MARKET_NAME = "market_name"
        const val INTERVAL = "interval"
        const val ORDER = "order"
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"
        const val COUNT = "count"
        const val PAGES_COUNT = "pages_count"
        const val CURRENT_PAGE = "current_page"
        const val CANDLE_STICKS = "candle_sticks"

    }


    constructor(): this("", "", "", "", "", -1, -1, -1, listOf())

}