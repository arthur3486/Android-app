package com.stocksexchange.android.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * A model class representing a user's sign-ins.
 */
@Parcelize
data class UserSession(
    @SerializedName("ip") val ip: String = "",
    @SerializedName("date") val lastUserDate: String = "",
    @SerializedName("created_at") val creationDate: String = "",
    @SerializedName("active") val isActive: Boolean = false
) : Parcelable