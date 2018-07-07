package com.stocksexchange.android.ui.transactions.search

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class TransactionsSearchPresenter(
    model: StubModel,
    view: TransactionsSearchContract.View
) : BasePresenter<StubModel, TransactionsSearchContract.View>(model, view), TransactionsSearchContract.ActionListener {


    constructor(view: TransactionsSearchContract.View): this(StubModel(), view)


}