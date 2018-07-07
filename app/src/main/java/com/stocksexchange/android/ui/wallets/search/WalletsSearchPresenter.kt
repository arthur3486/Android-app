package com.stocksexchange.android.ui.wallets.search

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class WalletsSearchPresenter(
    model: StubModel,
    view: WalletsSearchContract.View
) : BasePresenter<StubModel, WalletsSearchContract.View>(model, view), WalletsSearchContract.ActionListener {


    constructor(view: WalletsSearchContract.View): this(StubModel(), view)


}