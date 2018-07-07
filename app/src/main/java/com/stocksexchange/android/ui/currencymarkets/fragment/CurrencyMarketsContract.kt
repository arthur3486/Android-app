package com.stocksexchange.android.ui.currencymarkets.fragment

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.model.CurrencyMarketsParameters
import com.stocksexchange.android.ui.base.mvp.views.ListDataLoadingView

interface CurrencyMarketsContract {


    interface View : ListDataLoadingView<List<CurrencyMarket>> {

        fun sortDataSetIfNecessary()

        fun updateItemWith(item: CurrencyMarket, position: Int)

        fun launchCurrencyMarketPreviewActivity(currencyMarket: CurrencyMarket)

        fun addCurrencyMarketChronologically(currencyMarket: CurrencyMarket)

        fun deleteCurrencyMarket(currencyMarket: CurrencyMarket)

        fun containsCurrencyMarket(currencyMarket: CurrencyMarket): Boolean

        fun getMarketIndexForMarketId(id: Long): Int?

        fun getItem(position: Int): CurrencyMarket?

        fun getCurrencyMarketsParameters(): CurrencyMarketsParameters

    }


    interface ActionListener {

        fun onCurrencyMarketItemClicked(currencyMarket: CurrencyMarket)

        fun onBackPressed()

    }


}