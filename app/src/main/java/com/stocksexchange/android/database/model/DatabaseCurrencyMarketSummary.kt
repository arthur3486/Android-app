package com.stocksexchange.android.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.database.model.DatabaseCurrencyMarketSummary.Companion.TABLE_NAME

/**
 * A Room database model class of [CurrencyMarketSummary] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseCurrencyMarketSummary(
    @PrimaryKey @ColumnInfo(name = NAME) var name: String,
    @ColumnInfo(name = CURRENCY) var currency: String,
    @ColumnInfo(name = CURRENCY_NAME) var currencyName: String,
    @ColumnInfo(name = MARKET) var market: String,
    @ColumnInfo(name = MARKET_NAME) var marketName: String,
    @ColumnInfo(name = MIN_ORDER_AMOUNT) var minOrderAmount: Double,
    @ColumnInfo(name = MIN_BUY_PRICE) var minBuyPrice: Double,
    @ColumnInfo(name = MIN_SELL_PRICE) var minSellPrice: Double,
    @ColumnInfo(name = BUY_FEE_PERCENT) var buyFeePercent: Double,
    @ColumnInfo(name = SELL_FEE_PERCENT) var sellFeePercent: Double,
    @ColumnInfo(name = IS_ACTIVE) var isActive: Boolean
) {

    companion object {

        const val TABLE_NAME = "currency_market_summaries"

        const val NAME = "name"
        const val CURRENCY = "currency"
        const val CURRENCY_NAME = "currency_name"
        const val MARKET = "market"
        const val MARKET_NAME = "market_name"
        const val MIN_ORDER_AMOUNT = "min_order_amount"
        const val MIN_BUY_PRICE = "min_buy_price"
        const val MIN_SELL_PRICE = "min_sell_price"
        const val BUY_FEE_PERCENT = "buy_fee_percent"
        const val SELL_FEE_PERCENT = "sell_fee_percent"
        const val IS_ACTIVE = "is_active"

    }


    constructor(): this("", "", "", "", "", -1.0, -1.0, -1.0, -1.0, -1.0, false)

}