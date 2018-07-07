package com.stocksexchange.android.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.stocksexchange.android.database.model.DatabaseFavoriteCurrencyMarket.Companion.TABLE_NAME

/**
 * A Room database model class representing
 * user's favorite currency markets.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseFavoriteCurrencyMarket(
    @PrimaryKey @ColumnInfo(name = ID) var id: Long
) {

    companion object {

        const val TABLE_NAME = "favorite_currency_markets"

        const val ID = "id"

    }


    constructor(): this(-1L)

}