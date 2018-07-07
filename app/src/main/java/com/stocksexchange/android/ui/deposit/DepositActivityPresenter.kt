package com.stocksexchange.android.ui.deposit

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class DepositActivityPresenter(
    model: StubModel,
    view: DepositActivityContract.View
) : BasePresenter<StubModel, DepositActivityContract.View>(model, view), DepositActivityContract.ActionListener {


    constructor(view: DepositActivityContract.View): this(StubModel(), view)


}