package com.stocksexchange.android.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stocksexchange.android.api.StocksExchangeService
import kotlinx.android.parcel.Parcelize

/**
 * A model class representing [StocksExchangeService.getDepositInfo]
 * method response.
 */
@Parcelize
data class Deposit(
    @SerializedName("msg") var message: String = "",
    @SerializedName("currency", alternate = ["code"]) val currency: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("publicKey") val publicKey: String = "",
    @SerializedName("payment_id") val paymentId: String = ""
) : Parcelable {


    /**
     * Checks whether the currency is not empty.
     *
     * @return true if not empty; false otherwise
     */
    fun hasCurrency(): Boolean {
        return currency.isNotBlank()
    }


    /**
     * Checks whether the address is not empty.
     *
     * @return true if not empty; false otherwise
     */
    fun hasAddress(): Boolean {
        return address.isNotBlank()
    }


    /**
     * Checks whether the public key is not empty.
     *
     * @return true if not empty; false otherwise
     */
    fun hasPublicKey(): Boolean {
        return publicKey.isNotBlank()
    }


    /**
     * Checks whether the payment id is not empty.
     *
     * @return true if not empty; false otherwise
     */
    fun hasPaymentId(): Boolean {
        return paymentId.isNotBlank()
    }


}