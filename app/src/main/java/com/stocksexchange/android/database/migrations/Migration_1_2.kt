package com.stocksexchange.android.database.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration
import com.stocksexchange.android.database.model.DatabaseOrder

object Migration_1_2 : Migration(1, 2) {


    override fun migrate(database: SupportSQLiteDatabase) {
        val tableName = DatabaseOrder.TABLE_NAME
        val newTable = "${tableName}_new"

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS $newTable " +
            "(id INTEGER NOT NULL, market_name TEXT NOT NULL, currency TEXT NOT NULL, " +
            "market TEXT NOT NULL, type TEXT NOT NULL, trade_type TEXT NOT NULL, " +
            "amount REAL NOT NULL, buy_amount REAL NOT NULL, sell_amount REAL NOT NULL, " +
            "rate REAL NOT NULL, timestamp INTEGER NOT NULL, PRIMARY KEY(id))"
        )
        database.execSQL(
            "INSERT INTO $newTable " +
            "(id, market_name, currency, market, type, " +
            "trade_type, amount, buy_amount, sell_amount, rate, timestamp) " +
            "SELECT id, market_name, \"\", \"\", type, trade_type, amount, " +
            "buy_amount, sell_amount, rate, timestamp FROM $tableName"
        )
        database.execSQL("DROP TABLE $tableName")
        database.execSQL("ALTER TABLE $newTable RENAME TO $tableName")
    }


}