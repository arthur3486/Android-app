package com.stocksexchange.android.ui.orders

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BaseViewPagerPresenter

class OrdersActivityPresenter(
    model: StubModel,
    view: OrdersActivityContract.View
) : BaseViewPagerPresenter<StubModel, OrdersActivityContract.View>(model, view), OrdersActivityContract.ActionListener {


    constructor(view: OrdersActivityContract.View): this(StubModel(), view)


    override fun onActionButtonClicked() {
        mView.launchSearchActivity()
    }


}