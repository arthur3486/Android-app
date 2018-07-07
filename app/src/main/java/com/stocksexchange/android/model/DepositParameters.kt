package com.stocksexchange.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Parameters storing deposit related data.
 */
@Parcelize
data class DepositParameters(
    val currency: String
) : Parcelable