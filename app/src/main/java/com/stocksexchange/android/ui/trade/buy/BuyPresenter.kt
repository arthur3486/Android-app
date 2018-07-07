package com.stocksexchange.android.ui.trade.buy

import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.ui.base.mvp.presenters.BaseTradePresenter

class BuyPresenter(
    model: BuyModel,
    view: BuyContract.View
) : BaseTradePresenter<BuyModel, BuyContract.View>(model, view), BuyContract.ActionListener,
    BuyModel.ActionListener {


    init {
        model.setActionListener(this)
    }


    constructor(view: BuyContract.View): this(BuyModel(), view)


    override fun isUserBalanceInsufficient(amount: Double): Boolean {
        return ((amount * mView.getAtPriceInput()) >= mView.getUserMarketBalance())
    }


    override fun getBalanceTooSmallStringArg(summary: CurrencyMarketSummary): String {
        return summary.market
    }


}