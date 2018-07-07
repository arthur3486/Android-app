package com.stocksexchange.android.ui.orders.fragment

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.model.OrderParameters
import com.stocksexchange.android.ui.base.mvp.views.ListDataLoadingView

interface OrdersContract {


    interface View : ListDataLoadingView<List<Order>> {

        fun showToast(message: String)

        fun showSecondaryProgressBar()

        fun hideSecondaryProgressBar()

        fun addOrderChronologically(order: Order)

        fun containsOrder(order: Order): Boolean

        fun deleteOrder(orderId: Long)

        fun launchCurrencyMarketPreviewActivity(currencyMarket: CurrencyMarket)

        fun getOrderParameters(): OrderParameters

    }


    interface ActionListener {

        fun onMarketNameClicked(currencyMarket: CurrencyMarket?)

        fun onCancelButtonClicked(order: Order)

        fun onBackPressed()

    }


}