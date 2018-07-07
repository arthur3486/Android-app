package com.stocksexchange.android.ui.splash

import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class SplashPresenter(
    model: SplashModel,
    view: SplashContract.View
) : BasePresenter<SplashModel, SplashContract.View>(model, view), SplashContract.ActionListener {


    constructor(view: SplashContract.View): this(SplashModel(), view)


    override fun start() {
        super.start()

        mView.installSecurityProvider()
    }


    override fun onSecurityProviderInstalled() {
        mModel.initServices(mView.getLocale()) {
            mView.launchDashboard()
        }
    }


}