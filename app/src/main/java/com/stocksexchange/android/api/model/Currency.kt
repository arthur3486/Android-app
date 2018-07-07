package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * A model class representing a crypto currency (e.g., BTC, ETH, etc.).
 */
data class Currency(
    @SerializedName("currency") val name: String,
    @SerializedName("currency_long") val longName: String,
    @SerializedName("minimum_withdrawal_amount") val minimumWithdrawalAmount: Double,
    @SerializedName("minimum_deposit_amount") val minimumDepositAmount: Double,
    @SerializedName("deposit_fee_currency") val depositFeeCurrency: String,
    @SerializedName("deposit_fee_const") val depositFee: Double,
    @SerializedName("withdrawal_fee_currency") val withdrawalFeeCurrency: String,
    @SerializedName("withdrawal_fee_const") val withdrawalFee: Double,
    @SerializedName("block_explorer_url") val blockExplorerUrl: String,
    @SerializedName("active") val isActive: Boolean
): Serializable