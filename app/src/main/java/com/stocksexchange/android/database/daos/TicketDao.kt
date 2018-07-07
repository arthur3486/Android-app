package com.stocksexchange.android.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.stocksexchange.android.database.model.DatabaseTicket

/**
 * A Room data access object used for interacting
 * with [DatabaseTicket] model class.
 */
@Dao
interface TicketDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tickets: List<DatabaseTicket>)


    @Query(
        "SELECT * FROM ${DatabaseTicket.TABLE_NAME} " +
        "WHERE ${DatabaseTicket.SUBJECT} LIKE '%' || :query || '%'"
    )
    fun search(query: String): List<DatabaseTicket>


    @Query("SELECT * FROM ${DatabaseTicket.TABLE_NAME}")
    fun getAll(): List<DatabaseTicket>


}