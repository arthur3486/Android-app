package com.stocksexchange.android.ui.transactions.fragment

import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.model.TransactionParameters
import com.stocksexchange.android.ui.base.mvp.views.ListDataLoadingView

interface TransactionsContract {


    interface View : ListDataLoadingView<List<Transaction>> {

        fun showToast(message: String)

        fun launchBrowser(url: String)

        fun getTransactionParameters(): TransactionParameters

    }


    interface ActionListener {

        fun onTransactionAddressClicked(transaction: Transaction)

        fun onTransactionIdClicked(transaction: Transaction)

    }


}