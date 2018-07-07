package com.stocksexchange.android.ui.favoritecurrencymarkets

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class FavoriteCurrencyMarketsPresenter(
    model: StubModel,
    view: FavoriteCurrencyMarketsContract.View
) : BasePresenter<StubModel, FavoriteCurrencyMarketsContract.View>(model, view), FavoriteCurrencyMarketsContract.ActionListener {


    constructor(view: FavoriteCurrencyMarketsContract.View): this(StubModel(), view)




    override fun onActionButtonClicked() {
        mView.launchSearchActivity()
    }


}