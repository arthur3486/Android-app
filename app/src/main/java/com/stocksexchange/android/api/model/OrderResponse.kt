package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName
import com.stocksexchange.android.api.StocksExchangeService

/**
 * A model class representing the [StocksExchangeService.createOrder] and
 * [StocksExchangeService.cancelOrder] call response.
 */
data class OrderResponse(
    @SerializedName("funds") val funds: Map<String, String> = mapOf(),
    @SerializedName("order_id") val orderId: Long = -1L
)