package com.stocksexchange.android.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * A class representing details about the specific currency market.
 */
@Parcelize
data class CurrencyMarketSummary(
    @SerializedName("market_name") val name: String = "",
    @SerializedName("currency") val currency: String = "",
    @SerializedName("currency_long") val currencyName: String = "",
    @SerializedName("partner") val market: String = "",
    @SerializedName("partner_long") val marketName: String = "",
    @SerializedName("min_order_amount") val minOrderAmount: Double = -1.0,
    @SerializedName("min_buy_price") val minBuyPrice: Double = -1.0,
    @SerializedName("min_sell_price") val minSellPrice: Double = -1.0,
    @SerializedName("buy_fee_percent") val buyFeePercent: Double = -1.0,
    @SerializedName("sell_fee_percent") val sellFeePercent: Double = -1.0,
    @SerializedName("active") val isActive: Boolean = false
) : Parcelable