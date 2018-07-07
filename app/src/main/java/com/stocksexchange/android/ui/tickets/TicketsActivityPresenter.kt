package com.stocksexchange.android.ui.tickets

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class TicketsActivityPresenter(
    model: StubModel,
    view: TicketsActivityContract.View
) : BasePresenter<StubModel, TicketsActivityContract.View>(model, view), TicketsActivityContract.ActionListener {


    constructor(view: TicketsActivityContract.View): this(StubModel(), view)


    override fun onActionButtonClicked() {
        mView.launchTicketSearchActivity()
    }


    override fun onActionButtonFabClicked() {
        mView.launchTicketCreationActivity()
    }


}