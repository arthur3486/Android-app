package com.stocksexchange.android.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.database.model.DatabaseDeposit.Companion.TABLE_NAME

/**
 * A Room database model class of [Deposit] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseDeposit(
    @PrimaryKey @ColumnInfo(name = CURRENCY) var currency: String,
    @ColumnInfo(name = ADDRESS) var address: String,
    @ColumnInfo(name = PUBLIC_KEY) var publicKey: String,
    @ColumnInfo(name = PAYMENT_ID) var paymentId: String
) {

    companion object {

        const val TABLE_NAME = "deposits"

        const val CURRENCY = "currency"
        const val ADDRESS = "address"
        const val PUBLIC_KEY = "public_key"
        const val PAYMENT_ID = "payment_id"

    }


    constructor(): this("", "", "", "")

}