package com.stocksexchange.android.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.database.model.DatabaseCurrencyMarket.Companion.TABLE_NAME

/**
 * A Room database model class of [CurrencyMarket] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseCurrencyMarket(
    @PrimaryKey @ColumnInfo(name = ID) var id: Long,
    @ColumnInfo(name = NAME) var name: String,
    @ColumnInfo(name = CURRENCY) var currency: String,
    @ColumnInfo(name = MARKET) var market: String,
    @ColumnInfo(name = MIN_ORDER_AMOUNT) var minOrderAmount: Double,
    @ColumnInfo(name = DAILY_BUY_MAX_PRICE) var dailyBuyMaxPrice: Double,
    @ColumnInfo(name = DAILY_SELL_MIN_PRICE) var dailySellMinPrice: Double,
    @ColumnInfo(name = LAST_PRICE) var lastPrice: Double,
    @ColumnInfo(name = LAST_PRICE_DAY_AGO) var lastPriceDayAgo: Double,
    @ColumnInfo(name = DAILY_VOLUME) var dailyVolume: Double,
    @ColumnInfo(name = SPREAD) var spread: Double,
    @ColumnInfo(name = BUY_FEE_PERCENT) var buyFeePercent: Double,
    @ColumnInfo(name = SELL_FEE_PERCENT) var sellFeePercent: Double
) {

    companion object {

        const val TABLE_NAME = "currency_markets"

        const val ID = "id"
        const val NAME = "name"
        const val CURRENCY = "currency"
        const val MARKET = "market"
        const val MIN_ORDER_AMOUNT = "min_order_amount"
        const val DAILY_BUY_MAX_PRICE = "daily_buy_max_price"
        const val DAILY_SELL_MIN_PRICE = "daily_sell_min_price"
        const val LAST_PRICE = "last_price"
        const val LAST_PRICE_DAY_AGO = "last_price_day_ago"
        const val DAILY_VOLUME = "daily_volume"
        const val SPREAD = "spread"
        const val BUY_FEE_PERCENT = "buy_fee_percent"
        const val SELL_FEE_PERCENT = "sell_fee_percent"

    }


    constructor(): this(-1L, "", "", "", -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0)

}