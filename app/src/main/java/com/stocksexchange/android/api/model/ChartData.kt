package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName
import com.stocksexchange.android.api.StocksExchangeService
import java.io.Serializable

/**
 * A model class representing the [StocksExchangeService.getChartData] call response.
 */
data class ChartData(
    @SerializedName("pair") val marketName: String,
    @SerializedName("interval") val interval: String = "",
    @SerializedName("order") val order: String = "",
    @SerializedName("since") val startDate: String = "",
    @SerializedName("end") val endDate: String = "",
    @SerializedName("count") val count: Int,
    @SerializedName("count_pages") val pagesCount: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("graf") val candleSticks: List<CandleStick>
) : Serializable