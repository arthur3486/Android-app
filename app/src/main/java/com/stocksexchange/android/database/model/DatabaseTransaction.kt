package com.stocksexchange.android.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.database.model.DatabaseTransaction.Companion.TABLE_NAME

/**
 * A Room database model class of [Transaction] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseTransaction(
    @PrimaryKey @ColumnInfo(name = ID) var id: Long,
    @ColumnInfo(name = TRANSACTION_ID) var transactionId: String,
    @ColumnInfo(name = TYPE) var type: String,
    @ColumnInfo(name = CURRENCY) var currency: String,
    @ColumnInfo(name = STATUS) var status: String,
    @ColumnInfo(name = AMOUNT) var amount: Double,
    @ColumnInfo(name = FEE) var fee: String,
    @ColumnInfo(name = ADDRESS) var address: String,
    @ColumnInfo(name = TIMESTAMP) var timestamp: Long
) {

    companion object {

        const val TABLE_NAME = "transactions"

        const val ID = "id"
        const val TRANSACTION_ID = "transaction_id"
        const val TYPE = "type"
        const val CURRENCY = "currency"
        const val STATUS = "status"
        const val AMOUNT = "amount"
        const val FEE = "fee"
        const val ADDRESS = "address"
        const val TIMESTAMP = "timestamp"

    }


    constructor(): this(-1L, "", "", "", "", -1.0, "", "", -1L)

}