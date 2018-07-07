package com.stocksexchange.android.ui.orders

import com.stocksexchange.android.ui.base.mvp.views.ViewPagerView

interface OrdersActivityContract {


    interface View : ViewPagerView {

        fun launchSearchActivity()

    }


    interface ActionListener {

        fun onActionButtonClicked()

    }


}