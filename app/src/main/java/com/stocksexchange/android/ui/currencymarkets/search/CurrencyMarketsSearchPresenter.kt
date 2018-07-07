package com.stocksexchange.android.ui.currencymarkets.search

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class CurrencyMarketsSearchPresenter(
    model: StubModel,
    view: CurrencyMarketsSearchContract.View
) : BasePresenter<StubModel, CurrencyMarketsSearchContract.View>(model, view), CurrencyMarketsSearchContract.ActionListener {


    constructor(view: CurrencyMarketsSearchContract.View): this(StubModel(), view)


}