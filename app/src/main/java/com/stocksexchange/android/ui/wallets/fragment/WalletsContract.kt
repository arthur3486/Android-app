package com.stocksexchange.android.ui.wallets.fragment

import com.stocksexchange.android.model.Wallet
import com.stocksexchange.android.model.WalletParameters
import com.stocksexchange.android.ui.base.mvp.views.ListDataLoadingView

interface WalletsContract {


    interface View : ListDataLoadingView<List<Wallet>> {

        fun showLongToast(message: String)

        fun launchDepositActivity(wallet: Wallet)

        fun launchWithdrawalActivity(wallet: Wallet)

        fun getWalletParameters(): WalletParameters

        fun onEmptyWalletsFlagChanged(shouldShowEmptyWallets: Boolean)

    }


    interface ActionListener {

        fun onDepositButtonClicked(wallet: Wallet)

        fun onWithdrawButtonClicked(wallet: Wallet)

        fun onEmptyWalletsFlagChanged(shouldShowEmptyWallets: Boolean)

    }


}