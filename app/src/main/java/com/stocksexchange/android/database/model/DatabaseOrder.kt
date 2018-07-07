package com.stocksexchange.android.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.api.model.OrderAmount
import com.stocksexchange.android.database.model.DatabaseOrder.Companion.ID
import com.stocksexchange.android.database.model.DatabaseOrder.Companion.TABLE_NAME
import com.stocksexchange.android.database.model.DatabaseOrder.Companion.TYPE

/**
 * A Room database model class of [Order] class.
 */
@Entity(tableName = TABLE_NAME, primaryKeys = [ID, TYPE])
data class DatabaseOrder(
    @ColumnInfo(name = ID) var id: Long,
    @ColumnInfo(name = MARKET_NAME) var marketName: String,
    @ColumnInfo(name = CURRENCY) var currency: String,
    @ColumnInfo(name = MARKET) var market: String,
    @ColumnInfo(name = TYPE) var type: String,
    @ColumnInfo(name = TRADE_TYPE) var tradeType: String,
    @ColumnInfo(name = AMOUNT) var amount: Double,
    @ColumnInfo(name = ORIGINAL_AMOUNT) var originalAmount: Double,
    @ColumnInfo(name = BUY_AMOUNT) var buyAmount: Double,
    @ColumnInfo(name = SELL_AMOUNT) var sellAmount: Double,
    @ColumnInfo(name = RATE) var rate: Double,
    @ColumnInfo(name = RATES_MAP) var ratesMap: Map<Double, OrderAmount>,
    @ColumnInfo(name = TIMESTAMP) var timestamp: Long
) {

    companion object {

        const val TABLE_NAME = "orders"

        const val ID = "id"
        const val MARKET_NAME = "market_name"
        const val CURRENCY = "currency"
        const val MARKET = "market"
        const val TYPE = "type"
        const val TRADE_TYPE = "trade_type"
        const val AMOUNT = "amount"
        const val ORIGINAL_AMOUNT = "original_amount"
        const val BUY_AMOUNT = "buy_amount"
        const val SELL_AMOUNT = "sell_amount"
        const val RATE = "rate"
        const val RATES_MAP = "rates_map"
        const val TIMESTAMP = "timestamp"

    }


    constructor(): this(-1L, "", "", "", "", "", -1.0, -1.0, -1.0, -1.0, -1.0, mapOf(), 0L)

}