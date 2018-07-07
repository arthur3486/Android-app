package com.stocksexchange.android.ui.wallets

interface WalletsActivityContract {


    interface View {

        fun showPopupMenu()

        fun hidePopupMenu()

        fun reloadWallets(shouldShowEmptyWallets: Boolean)

        fun launchSearchActivity()

    }


    interface ActionListener {

        fun onActionButtonClicked()

        fun onSecondaryActionButtonClicked()

        fun onEmptyWalletsFlagStateChanged(shouldShowEmptyWallets: Boolean)

    }


}