package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.android.api.model.OrderOwnerTypes
import com.stocksexchange.android.api.model.OrderTradeTypes
import com.stocksexchange.android.api.model.OrderTypes
import kotlinx.android.parcel.Parcelize

/**
 * Parameters storing order related data.
 */
@Parcelize
data class OrderParameters(
    val marketName: String,
    val searchQuery: String,
    val mode: OrderModes,
    val type: OrderTypes,
    val tradeType: OrderTradeTypes,
    val ownerType: OrderOwnerTypes,
    val sortType: SortTypes,
    val count: Int
) : Parcelable