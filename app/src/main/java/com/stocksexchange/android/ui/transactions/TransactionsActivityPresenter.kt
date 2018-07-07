package com.stocksexchange.android.ui.transactions

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class TransactionsActivityPresenter(
    model: StubModel,
    view: TransactionsActivityContract.View
) : BasePresenter<StubModel, TransactionsActivityContract.View>(model, view), TransactionsActivityContract.ActionListener {


    constructor(view: TransactionsActivityContract.View): this(StubModel(), view)


    override fun onActionButtonClicked() {
        mView.launchSearchActivity()
    }


}