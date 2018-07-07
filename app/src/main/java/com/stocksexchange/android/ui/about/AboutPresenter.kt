package com.stocksexchange.android.ui.about

import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class AboutPresenter(
    model: StubModel,
    view: AboutContract.View
) : BasePresenter<StubModel, AboutContract.View>(model, view), AboutContract.ActionListener {


    constructor(view: AboutContract.View): this(StubModel(), view)


    override fun stop() {
        super.stop()

        mView.hidePopupMenu()
    }


    override fun onActionButtonClicked() {
        mView.showPopupMenu()
    }


    override fun onTermsOfUseMenuItemClicked() {
        mView.launchBrowser(BuildConfig.STOCKS_EXCHANGE_TERMS_OF_USE_URL)
    }


    override fun onVisitOurWebsiteButtonClicked() {
        mView.launchBrowser(BuildConfig.STOCKS_EXCHANGE_WEBSITE_URL)
    }


}