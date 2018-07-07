package com.stocksexchange.android.ui.help

import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class HelpPresenter(
    model: HelpModel,
    view: HelpContract.View
) : BasePresenter<HelpModel, HelpContract.View>(model, view), HelpContract.ActionListener {


    constructor(view: HelpContract.View): this(HelpModel(), view)


    override fun start() {
        super.start()

        if(mView.isDataSetEmpty()) {
            mView.setItems(mModel.getHelpItems())
        }
    }


    override fun stop() {
        super.stop()

        mView.hidePopupMenu()
    }


    override fun onActionButtonClicked() {
        mView.showPopupMenu()
    }


    override fun onFeedbackMenuItemClicked() {
        mView.launchFeedbackActivity()
    }


    override fun onAboutMenuItemClicked() {
        mView.launchAboutActivity()
    }


}