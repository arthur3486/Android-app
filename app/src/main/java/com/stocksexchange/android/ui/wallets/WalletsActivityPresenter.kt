package com.stocksexchange.android.ui.wallets

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class WalletsActivityPresenter(
    model: StubModel,
    view: WalletsActivityContract.View
) : BasePresenter<StubModel, WalletsActivityContract.View>(model, view), WalletsActivityContract.ActionListener {


    constructor(view: WalletsActivityContract.View): this(StubModel(), view)


    override fun stop() {
        super.stop()

        mView.hidePopupMenu()
    }


    override fun onActionButtonClicked() {
        mView.launchSearchActivity()
    }


    override fun onSecondaryActionButtonClicked() {
        mView.showPopupMenu()
    }


    override fun onEmptyWalletsFlagStateChanged(shouldShowEmptyWallets: Boolean) {
        mView.reloadWallets(shouldShowEmptyWallets)
    }


}