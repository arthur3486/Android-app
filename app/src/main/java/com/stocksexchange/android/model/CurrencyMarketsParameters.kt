package com.stocksexchange.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Parameters storing currency markets related data.
 */
@Parcelize
data class CurrencyMarketsParameters(
    val searchQuery: String,
    val currencyMarketType: CurrencyMarketTypes
) : Parcelable