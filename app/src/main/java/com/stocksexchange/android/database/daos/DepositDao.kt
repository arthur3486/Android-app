package com.stocksexchange.android.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.stocksexchange.android.database.model.DatabaseDeposit

/**
 * A Room data access object used for interacting
 * with [DatabaseDeposit] model class.
 */
@Dao
interface DepositDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deposit: DatabaseDeposit)


    @Query("DELETE FROM ${DatabaseDeposit.TABLE_NAME}")
    fun deleteAll()


    @Query(
        "SELECT * FROM ${DatabaseDeposit.TABLE_NAME} " +
        "WHERE ${DatabaseDeposit.CURRENCY} = :currency"
    )
    fun get(currency: String): DatabaseDeposit?


}