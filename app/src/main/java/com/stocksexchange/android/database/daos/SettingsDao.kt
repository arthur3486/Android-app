package com.stocksexchange.android.database.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.stocksexchange.android.database.model.DatabaseSettings

/**
 * A Room data access object used for interacting
 * with [DatabaseSettings] model class.
 */
@Dao
interface SettingsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(settings: DatabaseSettings)


    @Query(
        "SELECT * FROM ${DatabaseSettings.TABLE_NAME} " +
        "WHERE ${DatabaseSettings.ID} = :id"
    )
    fun get(id: Int): DatabaseSettings?


}