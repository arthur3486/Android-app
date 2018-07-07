package com.stocksexchange.android.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * A model class representing a user's assets portfolio.
 */
@Parcelize
data class AssetsPortfolio(
    @SerializedName("portfolio_price") val portfolioPrice: Float = 0.0f,
    @SerializedName("frozen_portfolio_price") val frozenPortfolioPrice: Float = 0.0f,
    @SerializedName("count") val count: Int = 0,
    @SerializedName("assets") val assets: List<Asset> = listOf()
) : Parcelable