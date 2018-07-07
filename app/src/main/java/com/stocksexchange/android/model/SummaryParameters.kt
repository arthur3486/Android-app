package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.android.api.model.CurrencyMarket
import kotlinx.android.parcel.Parcelize

/**
 * Parameters storing summary related data.
 */
@Parcelize
data class SummaryParameters(
    val currencyMarket: CurrencyMarket
) : Parcelable