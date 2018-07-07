package com.stocksexchange.android.ui.currencymarketpreview.summary

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.ui.base.mvp.views.DataLoadingView

interface SummaryContract {


    interface View : DataLoadingView<CurrencyMarketSummary> {

        fun getCurrencyMarket(): CurrencyMarket

    }


    interface ActionListener


}