package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName
import com.stocksexchange.android.CURRENCY_MARKET_SEPARATOR
import java.io.Serializable

/**
 * A model class representing user's orders.
 *
 * Note (28 April, 2018): Need to provide @SerializedName
 * for the "type" property since the GSON deserializer will
 * throw an exception saying "class Order declares multiple
 * JSON fields named type".
 */
data class Order(
    @SerializedName("pair") var marketName: String = "",
    @SerializedName("type") var tradeType: String = "",
    @SerializedName(value = "amount", alternate = ["quantity"]) var amount: Double = -1.0,
    @SerializedName(value = "rate", alternate = ["price"]) var rate: Double = -1.0,
    @SerializedName("original_amount") var originalAmount: Double = -1.0,
    @SerializedName("buy_amount") var buyAmount: Double = -1.0,
    @SerializedName("sell_amount") var sellAmount: Double = -1.0,
    @SerializedName("rates") var ratesMap: Map<Double, OrderAmount> = mapOf(),
    @SerializedName("timestamp") var timestamp: Long = 0L,
    @SerializedName("id") var id: Long = -1L,
    @SerializedName("irrelevant") var type: String = ""
) : Serializable, Comparable<Order> {


    var currency: String = ""
        private set
        get() {
            if(field.isBlank()) {
                field = if(marketName.isBlank()) "" else marketName.split(CURRENCY_MARKET_SEPARATOR)[0]
            }

            return field
        }


    var market: String = ""
        private set
        get() {
            if(field.isBlank()) {
                field = if(marketName.isBlank()) "" else marketName.split(CURRENCY_MARKET_SEPARATOR)[1]
            }

            return field
        }


    override fun compareTo(other: Order): Int {
        return when {
            (timestamp > other.timestamp) -> 1
            (timestamp < other.timestamp) -> -1
            else -> 0
        }
    }


    /**
     * Checks whether the trade type is of buy type.
     *
     * @return true if trade type is buy; false otherwise
     */
    fun isBuyTrade(): Boolean {
        return (tradeType.toLowerCase() == "buy")
    }


    /**
     * Checks whether the trade type is of sell type.
     *
     * @return true if trade type is sell; false otherwise
     */
    fun isSellTrade(): Boolean {
        return (tradeType.toLowerCase() == "sell")
    }


}