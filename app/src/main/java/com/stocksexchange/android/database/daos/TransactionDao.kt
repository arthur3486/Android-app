package com.stocksexchange.android.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.stocksexchange.android.database.model.DatabaseTransaction

/**
 * A Room data access object used for interacting
 * with [DatabaseTransaction] model class.
 */
@Dao
interface TransactionDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transactions: List<DatabaseTransaction>)


    @Query(
        "DELETE FROM ${DatabaseTransaction.TABLE_NAME} " +
        "WHERE ${DatabaseTransaction.TYPE} = :type"
    )
    fun delete(type: String)


    @Query(
        "SELECT * FROM ${DatabaseTransaction.TABLE_NAME} " +
        "WHERE ${DatabaseTransaction.TYPE} = :type AND " +
        "LOWER(${DatabaseTransaction.CURRENCY}) = :currency " +
        "ORDER BY ${DatabaseTransaction.TIMESTAMP} ASC " +
        "LIMIT :count"
    )
    fun searchAsc(type: String, currency: String, count: Int): List<DatabaseTransaction>


    @Query(
        "SELECT * FROM ${DatabaseTransaction.TABLE_NAME} " +
        "WHERE ${DatabaseTransaction.TYPE} = :type AND " +
        "LOWER(${DatabaseTransaction.CURRENCY}) = :currency " +
        "ORDER BY ${DatabaseTransaction.TIMESTAMP} DESC " +
        "LIMIT :count"
    )
    fun searchDesc(type: String, currency: String, count: Int): List<DatabaseTransaction>


    @Query(
        "SELECT * FROM ${DatabaseTransaction.TABLE_NAME} " +
        "WHERE ${DatabaseTransaction.TYPE} = :type " +
        "ORDER BY ${DatabaseTransaction.TIMESTAMP} ASC " +
        "LIMIT :count"
    )
    fun getAsc(type: String, count: Int): List<DatabaseTransaction>


    @Query(
        "SELECT * FROM ${DatabaseTransaction.TABLE_NAME} " +
        "WHERE ${DatabaseTransaction.TYPE} = :type " +
        "ORDER BY ${DatabaseTransaction.TIMESTAMP} DESC " +
        "LIMIT :count"
    )
    fun getDesc(type: String, count: Int): List<DatabaseTransaction>


}