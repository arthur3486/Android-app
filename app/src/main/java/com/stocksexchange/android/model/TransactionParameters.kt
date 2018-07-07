package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.android.api.model.TransactionOperations
import com.stocksexchange.android.api.model.TransactionStatuses
import kotlinx.android.parcel.Parcelize

/**
 * Parameters storing transaction related data.
 */
@Parcelize
data class TransactionParameters(
    val mode: TransactionModes,
    val type: TransactionTypes,
    val currency: String,
    val operation: TransactionOperations,
    val status: TransactionStatuses,
    val sortType: SortTypes,
    val count: Int,
    val searchQuery: String
) : Parcelable