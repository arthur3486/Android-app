package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName

/**
 * A typical response wrapper API returns holding
 * the data.
 */
data class ApiResponse<out T>(
    @SerializedName("success") val success: Int = 0,
    @SerializedName("data", alternate = ["result"]) val data: T? = null,
    @SerializedName("error") val error: String = ""
)