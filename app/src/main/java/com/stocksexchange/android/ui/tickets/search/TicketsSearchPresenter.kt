package com.stocksexchange.android.ui.tickets.search

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class TicketsSearchPresenter(
    model: StubModel,
    view: TicketsSearchContract.View
) : BasePresenter<StubModel, TicketsSearchContract.View>(model, view), TicketsSearchContract.ActionListener {


    constructor(view: TicketsSearchContract.View): this(StubModel(), view)


}