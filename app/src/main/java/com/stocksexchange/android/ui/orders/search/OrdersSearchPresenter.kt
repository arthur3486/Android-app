package com.stocksexchange.android.ui.orders.search

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class OrdersSearchPresenter(
    model: StubModel,
    view: OrdersSearchContract.View
) : BasePresenter<StubModel, OrdersSearchContract.View>(model, view), OrdersSearchContract.ActionListener {


    constructor(view: OrdersSearchContract.View): this(StubModel(), view)


}