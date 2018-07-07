package com.stocksexchange.android.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.stocksexchange.android.api.model.Currency
import com.stocksexchange.android.database.model.DatabaseCurrency.Companion.TABLE_NAME

/**
 * A Room database model class of [Currency] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseCurrency(
    @PrimaryKey @ColumnInfo(name = NAME) var name: String,
    @ColumnInfo(name = LONG_NAME) var longName: String,
    @ColumnInfo(name = MINIMUM_WITHDRAWAL_AMOUNT) var minimumWithdrawalAmount: Double,
    @ColumnInfo(name = MINIMUM_DEPOSIT_AMOUNT) var minimumDepositAmount: Double,
    @ColumnInfo(name = DEPOSIT_FEE_CURRENCY) var depositFeeCurrency: String,
    @ColumnInfo(name = DEPOSIT_FEE) var depositFee: Double,
    @ColumnInfo(name = WITHDRAWAL_FEE_CURRENCY) var withdrawalFeeCurrency: String,
    @ColumnInfo(name = WITHDRAWAL_FEE) var withdrawalFee: Double,
    @ColumnInfo(name = BLOCK_EXPLORER_URL) var blockExplorerUrl: String,
    @ColumnInfo(name = IS_ACTIVE) var isActive: Boolean
) {

    companion object {

        const val TABLE_NAME = "currencies"

        const val NAME = "name"
        const val LONG_NAME = "long_name"
        const val MINIMUM_WITHDRAWAL_AMOUNT = "minimum_withdrawal_amount"
        const val MINIMUM_DEPOSIT_AMOUNT = "minimum_deposit_amount"
        const val DEPOSIT_FEE_CURRENCY = "deposit_fee_currency"
        const val DEPOSIT_FEE = "deposit_fee"
        const val WITHDRAWAL_FEE_CURRENCY = "withdrawal_fee_currency"
        const val WITHDRAWAL_FEE = "withdrawal_fee"
        const val BLOCK_EXPLORER_URL = "block_explorer_url"
        const val IS_ACTIVE = "is_active"

    }


    constructor(): this("", "", -1.0, -1.0, "", -1.0, "", -1.0, "", false)

}