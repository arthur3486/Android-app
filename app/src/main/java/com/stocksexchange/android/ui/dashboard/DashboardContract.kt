package com.stocksexchange.android.ui.dashboard

import com.stocksexchange.android.ui.base.mvp.views.ViewPagerView

interface DashboardContract {


    interface View : ViewPagerView {

        fun showAppBar(animate: Boolean)

        fun closeDrawer()

        fun restartActivity()

        fun launchFavoriteCurrencyMarketsActivity()

        fun launchSearchActivity()

        fun launchSignInActivity()

        fun launchWalletsActivity()

        fun launchOrdersActivity()

        fun launchDepositsActivity()

        fun launchWithdrawalsActivity()

        fun launchTicketsActivity()

        fun launchSettingsActivity()

        fun launchAboutActivity()

        fun launchFeedbackActivity()

        fun launchHelpActivity()

        fun isDrawerOpen(): Boolean

        fun isLandscape(): Boolean

    }


    interface ActionListener {

        fun onFavoritesButtonClicked()

        fun onSearchButtonClicked()

        fun onHeaderClicked()

        fun onSignInButtonClicked()

        fun onWalletsClicked()

        fun onOrdersClicked()

        fun onDepositsClicked()

        fun onWithdrawalsClicked()

        fun onTicketsClicked()

        fun onSettingsClicked()

        fun onAboutClicked()

        fun onFeedbackClicked()

        fun onHelpClicked()

        fun onBackPressed(): Boolean

    }


}