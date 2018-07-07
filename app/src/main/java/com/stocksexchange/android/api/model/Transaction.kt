package com.stocksexchange.android.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * A model class representing user's deposits and withdrawals.
 */
data class Transaction(
    @SerializedName("TX_id") var transactionId: String = "",
    @SerializedName("Currency") var currency: String = "",
    @SerializedName("Amount") var amount: Double = -1.0,
    @SerializedName("Deposit_fee", alternate = ["Withdrawal_Fee"]) var fee: String = "",
    @SerializedName("Address") var address: String = "",
    @SerializedName("Status") var status: String = "",
    @SerializedName("Date") var timestamp: Long = -1L,
    var id: Long = -1L,
    var type: String = "",
    var blockExplorerUrl: String = ""
): Serializable {


    /**
     * Checks whether the transaction ID exists and is not empty.
     *
     * @return true if transaction ID exists and is not empty; false otherwise
     */
    fun hasTransactionId(): Boolean {
        return transactionId.isNotBlank()
    }


    /**
     * Checks whether the block explorer URL is not empty.
     *
     * @return true if block explorer URL is not empty; false otherwise
     */
    fun hasBlockExplorerUrl(): Boolean {
        return blockExplorerUrl.isNotBlank()
    }


}