package com.stocksexchange.android.ui.currencymarketpreview

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.api.model.OrderTradeTypes
import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.ui.base.mvp.views.ViewPagerView

interface CurrencyMarketPreviewContract {


    interface View : ViewPagerView {

        fun showToast(message: String)

        fun lockToPortraitOrientation()

        fun unlockFromPortraitOrientation()

        fun updateFavoriteButtonState(isFavorite: Boolean)

        fun launchBuyActivity(currencyMarket: CurrencyMarket, summary: CurrencyMarketSummary, user: User)

        fun launchSellActivity(currencyMarket: CurrencyMarket, summary: CurrencyMarketSummary, user: User)

        fun launchLoginActivity()

        fun getCurrencyMarket(): CurrencyMarket

        fun getCurrencyMarketSummary(): CurrencyMarketSummary?

    }


    interface ActionListener {

        fun onActionButtonClicked()

        fun onTradeButtonClicked(orderTradeType: OrderTradeTypes)

        fun onBackButtonClicked()

    }


}