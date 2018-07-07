package com.stocksexchange.android.ui.dashboard

import com.stocksexchange.android.R
import com.stocksexchange.android.utils.providers.BooleanProvider
import com.stocksexchange.android.events.SettingsEvent
import com.stocksexchange.android.events.UserEvent
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BaseViewPagerPresenter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.standalone.inject

class DashboardPresenter(
    model: StubModel,
    view: DashboardContract.View
) : BaseViewPagerPresenter<StubModel, DashboardContract.View>(model, view), DashboardContract.ActionListener {


    private val mBooleanProvider: BooleanProvider by inject()




    constructor(view: DashboardContract.View): this(StubModel(), view)


    override fun onFavoritesButtonClicked() {
        mView.launchFavoriteCurrencyMarketsActivity()
    }


    override fun onSearchButtonClicked() {
        mView.launchSearchActivity()
    }


    override fun onSignInButtonClicked() {
        mView.launchSignInActivity()
    }


    override fun onHeaderClicked() {
        mView.launchSettingsActivity()
    }


    override fun onWalletsClicked() {
        mView.launchWalletsActivity()
    }


    override fun onOrdersClicked() {
        mView.launchOrdersActivity()
    }


    override fun onDepositsClicked() {
        mView.launchDepositsActivity()
    }


    override fun onWithdrawalsClicked() {
        mView.launchWithdrawalsActivity()
    }


    override fun onTicketsClicked() {
        mView.launchTicketsActivity()
    }


    override fun onSettingsClicked() {
        mView.launchSettingsActivity()
    }


    override fun onAboutClicked() {
        mView.launchAboutActivity()
    }


    override fun onHelpClicked() {
        mView.launchHelpActivity()
    }


    override fun onFeedbackClicked() {
        mView.launchFeedbackActivity()
    }


    override fun onTabReselected(position: Int) {
        super.onTabReselected(position)

        mView.showAppBar(true)
    }


    override fun onBackPressed(): Boolean {
        return if(shouldCloseDrawer()) {
            mView.closeDrawer()
            true
        } else {
            false
        }
    }


    private fun shouldCloseDrawer(): Boolean {
        return ((!(mBooleanProvider.getBoolean(R.bool.isTablet) && mView.isLandscape())) && mView.isDrawerOpen())
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(event: SettingsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        when(event.action) {

            SettingsEvent.Actions.DECIMAL_SEPARATOR_CHANGED,
            SettingsEvent.Actions.DEFAULTS_RESTORED -> {
                mView.restartActivity()
            }

        }

        event.consume()
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(event: UserEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        when(event.action) {

            UserEvent.Actions.SIGNED_OUT -> {
                mView.restartActivity()
            }

        }

        event.consume()
    }


    override fun canReceiveEvents(): Boolean {
        return true
    }


}