package com.stocksexchange.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Parameters storing wallet related data.
 */
@Parcelize
data class WalletParameters(
    val shouldShowEmptyWallets: Boolean,
    val searchQuery: String,
    val walletMode: WalletModes
) : Parcelable