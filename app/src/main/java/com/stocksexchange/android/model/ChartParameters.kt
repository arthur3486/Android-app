package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.android.api.model.ChartDataIntervals
import kotlinx.android.parcel.Parcelize

/**
 * Parameters storing chart related data.
 */
@Parcelize
data class ChartParameters(
    val mode: ChartModes,
    val marketName: String,
    val interval: ChartDataIntervals,
    val sortType: SortTypes,
    val count: Int,
    val page: Int
) : Parcelable