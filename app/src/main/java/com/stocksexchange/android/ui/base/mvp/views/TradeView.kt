package com.stocksexchange.android.ui.base.mvp.views

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.api.model.OrderTradeTypes
import java.util.*

/**
 * A base trading view to build views on.
 */
interface TradeView {

    fun showToast(message: String)

    fun showProgressBar()

    fun hideProgressBar()

    fun enableTradeButton()

    fun disableTradeButton()

    fun setAmountInputError(error: String)

    fun setAtPriceInputError(error: String)

    fun updateUserFunds(funds: Map<String, String>)

    fun updateBalance()

    fun updateTradeDetails(amount: Double, price: Double)

    fun getAmountInput(): Double

    fun getAtPriceInput(): Double

    fun getUserCurrencyBalance(): Double

    fun getUserMarketBalance(): Double

    fun getMinOrderAmount(): String

    fun getOrderTradeType(): OrderTradeTypes

    fun getCurrencyMarket(): CurrencyMarket

    fun getCurrencyMarketSummary(): CurrencyMarketSummary

    fun getLocale(): Locale

}