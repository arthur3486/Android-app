package com.stocksexchange.android.model

import com.stocksexchange.android.api.model.Currency
import java.io.Serializable

/**
 * A model class holding wallet related data.
 */
data class Wallet(
    val currency: Currency,
    val availableBalance: Double,
    val balanceInOrders: Double
): Serializable