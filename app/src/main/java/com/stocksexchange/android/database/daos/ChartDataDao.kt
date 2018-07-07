package com.stocksexchange.android.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.stocksexchange.android.database.model.DatabaseChartData

/**
 * A Room data access object used for interacting
 * with [DatabaseChartData] model class.
 */
@Dao
interface ChartDataDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chartData: DatabaseChartData)


    @Query(
        "SELECT * FROM ${DatabaseChartData.TABLE_NAME} WHERE " +
        "${DatabaseChartData.MARKET_NAME} = :marketName AND " +
        "${DatabaseChartData.INTERVAL} = :interval AND " +
        "`${DatabaseChartData.ORDER}` = :order AND " +
        "${DatabaseChartData.COUNT} = :count AND " +
        "${DatabaseChartData.CURRENT_PAGE} = :currentPage"
    )
    fun get(
        marketName: String,
        interval: String,
        order: String,
        count: Int,
        currentPage: Int
    ): DatabaseChartData?


}