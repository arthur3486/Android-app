package com.stocksexchange.android.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * A model class representing an asset a user holds.
 */
@Parcelize
data class Asset(
    @SerializedName("wallets_address") val walletAddress: String = "",
    @SerializedName("public_key") val publicKey: String = "",
    @SerializedName("funds") val funds: Float = 0.0f
) : Parcelable