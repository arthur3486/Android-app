package com.stocksexchange.android.ui.trade.sell

import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.ui.base.mvp.presenters.BaseTradePresenter

class SellPresenter(
    model: SellModel,
    view: SellContract.View
) : BaseTradePresenter<SellModel, SellContract.View>(model, view), SellContract.ActionListener,
    SellModel.ActionListener {


    init {
        model.setActionListener(this)
    }


    constructor(view: SellContract.View): this(SellModel(), view)


    override fun isUserBalanceInsufficient(amount: Double): Boolean {
        return (amount >= mView.getUserCurrencyBalance())
    }


    override fun getBalanceTooSmallStringArg(summary: CurrencyMarketSummary): String {
        return summary.currency
    }


}