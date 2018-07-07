package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName

/**
 * A wrapper model class holding the data for a
 * changed currency market passed through sockets.
 */
data class CurrencyMarketSocketData(
    @SerializedName("id") val id: Long,
    @SerializedName("price") val lastPrice: Double,
    @SerializedName("volume") val lastVolume: Double
)