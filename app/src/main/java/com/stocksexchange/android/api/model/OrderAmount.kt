package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * A model class representing order buy and sell amount.
 */
data class OrderAmount(
    @SerializedName("buy_amount") var buyAmount: Double = -1.0,
    @SerializedName("sell_amount") var sellAmount: Double = -1.0
) : Serializable