package com.stocksexchange.android.database.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration
import com.stocksexchange.android.database.model.DatabaseCurrency

object Migration_2_3 : Migration(2, 3) {


    override fun migrate(database: SupportSQLiteDatabase) {
        val tableName = DatabaseCurrency.TABLE_NAME
        val newTable = "${tableName}_new"

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS $newTable " +
            "(name TEXT NOT NULL, long_name TEXT NOT NULL, minimum_withdrawal_amount REAL NOT NULL, " +
            "minimum_deposit_amount REAL NOT NULL, deposit_fee_currency TEXT NOT NULL, " +
            "deposit_fee REAL NOT NULL, withdrawal_fee_currency TEXT NOT NULL, withdrawal_fee REAL NOT NULL, " +
            "block_explorer_url TEXT NOT NULL, is_active INTEGER NOT NULL, PRIMARY KEY(name))"
        )
        database.execSQL(
            "INSERT INTO $newTable " +
            "(name, long_name, minimum_withdrawal_amount, minimum_deposit_amount, deposit_fee_currency," +
            "deposit_fee, withdrawal_fee_currency, withdrawal_fee, block_explorer_url, is_active) " +
            "SELECT name, long_name, minimum_withdrawal_amount, minimum_deposit_amount, deposit_fee_currency," +
            "deposit_fee, withdrawal_fee_currency, withdrawal_fee, \"\", is_active FROM $tableName"
        )
        database.execSQL("DROP TABLE $tableName")
        database.execSQL("ALTER TABLE $newTable RENAME TO $tableName")
    }


}