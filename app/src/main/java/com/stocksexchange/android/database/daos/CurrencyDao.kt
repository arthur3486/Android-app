package com.stocksexchange.android.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.stocksexchange.android.database.model.DatabaseCurrency

/**
 * A Room data access object used for interacting
 * with [DatabaseCurrency] model class.
 */
@Dao
interface CurrencyDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencies: List<DatabaseCurrency>)


    @Query("DELETE FROM ${DatabaseCurrency.TABLE_NAME}")
    fun deleteAll()


    @Query(
        "SELECT * FROM ${DatabaseCurrency.TABLE_NAME} " +
        "WHERE LOWER(${DatabaseCurrency.NAME}) = :query OR " +
        "LOWER(${DatabaseCurrency.LONG_NAME}) = :query"
    )
    fun search(query: String): List<DatabaseCurrency>


    @Query("SELECT * FROM ${DatabaseCurrency.TABLE_NAME}")
    fun getAll(): List<DatabaseCurrency>


}