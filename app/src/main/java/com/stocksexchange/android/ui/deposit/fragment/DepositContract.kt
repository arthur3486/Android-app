package com.stocksexchange.android.ui.deposit.fragment

import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.ui.base.mvp.views.DataLoadingView

interface DepositContract {


    interface View : DataLoadingView<Deposit> {

        fun showToast(message: String)

        fun showQrDialog(hash: String, titleResId: Int)

        fun dismissQrDialog()

        fun downloadQrCodeImage()

        fun getCurrency(): String

        fun getDeposit(): Deposit?

    }


    interface ActionListener {

        fun onDepositAddressClicked(address: String)

        fun onDepositAddressLongClicked(address: String)

        fun onPublicKeyClicked(publicKey: String)

        fun onPublicKeyLongClicked(publicKey: String)

        fun onPaymentIdClicked(paymentId: String)

        fun onPaymentIdLongClicked(paymentId: String)

        fun onCopyHashButtonClicked(hash: String)

        fun onSaveQrCodeImageButtonClicked()

    }


}