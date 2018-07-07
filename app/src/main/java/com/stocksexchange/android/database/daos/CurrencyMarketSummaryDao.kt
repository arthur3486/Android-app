package com.stocksexchange.android.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.stocksexchange.android.database.model.DatabaseCurrencyMarketSummary

/**
 * A Room data access object used for interacting
 * with [DatabaseCurrencyMarketSummary] model class.
 */
@Dao
interface CurrencyMarketSummaryDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencyMarketSummary: DatabaseCurrencyMarketSummary)


    @Query(
        "SELECT * FROM ${DatabaseCurrencyMarketSummary.TABLE_NAME} " +
        "WHERE ${DatabaseCurrencyMarketSummary.CURRENCY} = :currency AND " +
        "${DatabaseCurrencyMarketSummary.MARKET} = :market"
    )
    fun get(currency: String, market: String): DatabaseCurrencyMarketSummary?


}