package com.stocksexchange.android.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.stocksexchange.android.CURRENCY_MARKET_SEPARATOR
import com.stocksexchange.android.database.model.DatabaseCurrencyMarket

/**
 * A Room data access object used for interacting
 * with [DatabaseCurrencyMarket] model class.
 */
@Dao
interface CurrencyMarketDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencyMarket: DatabaseCurrencyMarket)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencyMarkets: List<DatabaseCurrencyMarket>)


    @Query("DELETE FROM ${DatabaseCurrencyMarket.TABLE_NAME}")
    fun deleteAll()


    @Query(
        "SELECT * FROM ${DatabaseCurrencyMarket.TABLE_NAME} " +
        "WHERE LOWER(${DatabaseCurrencyMarket.CURRENCY}) = :query OR " +
        "LOWER(${DatabaseCurrencyMarket.MARKET}) = :query OR (" +
        "REPLACE(LOWER(${DatabaseCurrencyMarket.NAME}), '$CURRENCY_MARKET_SEPARATOR', ' / ') LIKE (:query || '%'))"
    )
    fun search(query: String): List<DatabaseCurrencyMarket>


    @Query(
        "SELECT * FROM ${DatabaseCurrencyMarket.TABLE_NAME} " +
        "WHERE ${DatabaseCurrencyMarket.ID} IN (:ids)"
    )
    fun get(ids: List<Long>): List<DatabaseCurrencyMarket>


    @Query("SELECT * FROM ${DatabaseCurrencyMarket.TABLE_NAME}")
    fun getAll(): List<DatabaseCurrencyMarket>


}