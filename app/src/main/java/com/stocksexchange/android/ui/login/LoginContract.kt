package com.stocksexchange.android.ui.login

interface LoginContract {


    interface View {

        fun showToast(message: String)

        fun showProgress()

        fun hideProgress()

        fun enableQrCodeScanner()

        fun disableQrCodeScanner()

        fun launchDashboard()

    }


    interface ActionListener {

        fun onQrCodeScanned(qrCode: String)

    }


}
