package com.stocksexchange.android.ui.chart

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class ChartActivityPresenter(
    model: StubModel,
    view: ChartActivityContract.View
) : BasePresenter<StubModel, ChartActivityContract.View>(model, view), ChartActivityContract.ActionListener {


    constructor(view: ChartActivityContract.View): this(StubModel(), view)


}