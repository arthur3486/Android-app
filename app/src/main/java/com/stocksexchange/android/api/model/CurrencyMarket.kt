package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName
import com.stocksexchange.android.CURRENCY_MARKET_SEPARATOR
import java.io.Serializable

/**
 * A class representing a currency market (e.g., ETH_BTC, NXT_ETH).
 */
data class CurrencyMarket(
    @SerializedName("market_id") val id: Long = -1L,
    @SerializedName("market_name") val name: String = "",
    @SerializedName("min_order_amount") val minOrderAmount: Double = -1.0,
    @SerializedName("ask") val dailyBuyMaxPrice: Double = -1.0,
    @SerializedName("bid") val dailySellMinPrice: Double = -1.0,
    @SerializedName("last") val lastPrice: Double = -1.0,
    @SerializedName("lastDayAgo") val lastPriceDayAgo: Double = -1.0,
    @SerializedName("vol") val dailyVolume: Double = -1.0,
    @SerializedName("spread") val spread: Double = -1.0,
    @SerializedName("buy_fee_percent") val buyFeePercent: Double = -1.0,
    @SerializedName("sell_fee_percent") val sellFeePercent: Double = -1.0
) : Serializable {


    var currency: String = ""
        private set
        get() {
            if(field.isBlank()) {
                field = if(name.isBlank()) "" else name.split(CURRENCY_MARKET_SEPARATOR)[0]
            }

            return field
        }


    var market: String = ""
        private set
        get() {
            if(field.isBlank()) {
                field = if(name.isBlank()) "" else name.split(CURRENCY_MARKET_SEPARATOR)[1]
            }

            return field
        }


    var lastVolume: Double = -1.0
        private set
        get() {
            if(field == -1.0) {
                field = (lastPrice * dailyVolume)
            }

            return field
        }


    var dailyChange: Double = -1.0
        private set
        get() {
            if(field == -1.0) {
                field = if(lastPriceDayAgo != 0.0) {
                    (((lastPrice - lastPriceDayAgo) / lastPriceDayAgo) * 100.0)
                } else {
                    0.0
                }
            }

            return field
        }


}