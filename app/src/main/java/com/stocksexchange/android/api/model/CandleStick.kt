package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * A model class representing the data for a candlestick chart.
 */
data class CandleStick(
    @SerializedName("open") val openPrice: Double,
    @SerializedName("close") val closePrice: Double,
    @SerializedName("low") val lowPrice: Double,
    @SerializedName("high") val highPrice: Double,
    @SerializedName("date") val date: String
) : Serializable