package com.stocksexchange.android.database.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration
import com.stocksexchange.android.database.model.DatabaseTransaction

object Migration_5_6 : Migration(5,6) {


    override fun migrate(database: SupportSQLiteDatabase) {
        val tableName = DatabaseTransaction.TABLE_NAME
        val newTable = "${tableName}_new"

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS $newTable " +
            "(id INTEGER NOT NULL, transaction_id TEXT NOT NULL, type TEXT NOT NULL, " +
            "currency TEXT NOT NULL, status TEXT NOT NULL, amount REAL NOT NULL, " +
            "fee TEXT NOT NULL, address TEXT NOT NULL, timestamp INTEGER NOT NULL, " +
            "PRIMARY KEY(id))"
        )
        database.execSQL(
            "INSERT INTO $newTable " +
            "(id, transaction_id, type, currency, status, amount, fee, address, timestamp) " +
            "SELECT id, IFNULL(transaction_id, \"\"), type, currency, status, amount, fee, " +
            "address, timestamp FROM $tableName"
        )
        database.execSQL("DROP TABLE $tableName")
        database.execSQL("ALTER TABLE $newTable RENAME TO $tableName")
    }


}