package com.stocksexchange.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Parameters storing order creation related data.
 */
@Parcelize
data class OrderCreationParameters(
    val marketName: String,
    val type: String,
    val amount: String,
    val rate: String
) : Parcelable