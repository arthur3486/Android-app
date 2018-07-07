package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.android.api.model.TicketCategories
import com.stocksexchange.android.api.model.TicketStatuses
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TicketParameters(
    val mode: TicketModes = TicketModes.STANDARD,
    val ticketId: Long? = null,
    val status: TicketStatuses? = null,
    val category: TicketCategories? = null,
    val searchQuery: String = ""
) : Parcelable