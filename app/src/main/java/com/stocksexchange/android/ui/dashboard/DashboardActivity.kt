package com.stocksexchange.android.ui.dashboard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.Menu
import com.stocksexchange.android.AT_LEAST_LOLLIPOP
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.model.DashboardTabs
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.LAST_VOLUME_DESCENDING_COMPARATOR
import com.stocksexchange.android.repositories.users.UsersRepository
import com.stocksexchange.android.ui.about.AboutActivity
import com.stocksexchange.android.ui.base.activities.BaseViewPagerActivity
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketsFragment
import com.stocksexchange.android.ui.currencymarkets.search.CurrencyMarketsSearchActivity
import com.stocksexchange.android.ui.favoritecurrencymarkets.FavoriteCurrencyMarketsActivity
import com.stocksexchange.android.ui.feedback.FeedbackActivity
import com.stocksexchange.android.ui.help.HelpActivity
import com.stocksexchange.android.ui.login.LoginActivity
import com.stocksexchange.android.ui.orders.OrdersActivity
import com.stocksexchange.android.ui.settings.SettingsActivity
import com.stocksexchange.android.ui.tickets.TicketsActivity
import com.stocksexchange.android.ui.transactions.TransactionsActivity
import com.stocksexchange.android.ui.utils.ViewOutlineProviders
import com.stocksexchange.android.ui.utils.extensions.*
import com.stocksexchange.android.ui.views.MarkableImageView
import com.stocksexchange.android.ui.wallets.WalletsActivity
import kotlinx.android.synthetic.main.dashboard_activity_content_layout.*
import kotlinx.android.synthetic.main.dashboard_activity_layout.*
import kotlinx.android.synthetic.main.navigation_view_header_layout.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.*
import org.koin.android.ext.android.get

class DashboardActivity : BaseViewPagerActivity<DashboardViewPagerAdapter, DashboardPresenter>(),
    DashboardContract.View {


    companion object {

        private const val SAVED_STATE_COMPARATOR = "comparator"


        fun newInstance(context: Context): Intent {
            return context.intentFor<DashboardActivity>().clearTop()
        }

    }


    /**
     * A comparator that is currently selected by the sort panel.
     */
    // todo to be extracted to the settings in the future
    private var mComparator: CurrencyMarketComparator = LAST_VOLUME_DESCENDING_COMPARATOR




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.fade_in_animation,
            R.anim.fade_out_animation
        )
    }


    override fun initPresenter(): DashboardPresenter = DashboardPresenter(this)


    override fun init() {
        super.init()

        initCurrencyMarketsSortPanel()
        initDrawerLayout()
        initNavigationView()
    }


    override fun initToolbar() {
        setSupportActionBar(toolbar)

        favoritesBtnIv.setOnClickListener { mPresenter?.onFavoritesButtonClicked() }
        searchBtnIv.setOnClickListener { mPresenter?.onSearchButtonClicked() }
    }


    override fun populateAdapter() {
        with(mAdapter) {
            addFragment(getFragment(DashboardTabs.BTC.ordinal) ?: CurrencyMarketsFragment.newBtcInstance())
            addFragment(getFragment(DashboardTabs.USDT.ordinal) ?: CurrencyMarketsFragment.newUsdtInstance())
            addFragment(getFragment(DashboardTabs.NXT.ordinal) ?: CurrencyMarketsFragment.newNxtInstance())
            addFragment(getFragment(DashboardTabs.LTC.ordinal) ?: CurrencyMarketsFragment.newLtcInstance())
            addFragment(getFragment(DashboardTabs.ETH.ordinal) ?: CurrencyMarketsFragment.newEthInstance())
            addFragment(getFragment(DashboardTabs.TUSD.ordinal) ?: CurrencyMarketsFragment.newTusdInstance())
        }
    }


    override fun initTabLayoutTabs() {
        with(mTabAnimator) {
            getTabAt(DashboardTabs.BTC.ordinal)?.setTitle(DashboardTabs.BTC.name)
            getTabAt(DashboardTabs.USDT.ordinal)?.setTitle(DashboardTabs.USDT.name)
            getTabAt(DashboardTabs.NXT.ordinal)?.setTitle(DashboardTabs.NXT.name)
            getTabAt(DashboardTabs.LTC.ordinal)?.setTitle(DashboardTabs.LTC.name)
            getTabAt(DashboardTabs.ETH.ordinal)?.setTitle(DashboardTabs.ETH.name)
            getTabAt(DashboardTabs.TUSD.ordinal)?.setTitle(DashboardTabs.TUSD.name)
        }
    }


    private fun initCurrencyMarketsSortPanel() {
        currencyMarketsSortPanel.mOnSortPanelTitleClickListener = { mAdapter.sort(it.mComparator) }
        currencyMarketsSortPanel.setComparator(mComparator)
    }


    private fun initDrawerLayout() {
        val drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.dashboard_drawer_open,
            R.string.dashboard_drawer_close
        )

        if(resources.getBoolean(R.bool.isTablet) && isLandscape()) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
            drawerLayout.setScrimColor(Color.TRANSPARENT)
            drawerLayout.drawerElevation = 0f
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            drawerLayout.addDrawerListener(drawerToggle)
            drawerToggle.syncState()

            if(!AT_LEAST_LOLLIPOP) {
                drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START)
            }
        }
    }


    @SuppressWarnings("NewApi")
    private fun initNavigationView() {
        launch(UI) {
            val result = get<UsersRepository>().getSignedInUser()

            if(result.isSuccessful()) {
                initNavigationViewHeaderWithUserInfo(result.getSuccessfulResult().value)
            } else {
                initNavigationViewHeaderWithoutUserInfo()
                hideUserSpecificNavigationViewItems()
            }

            //todo
            navigationView.menu.findItem(R.id.tickets).isVisible = false
        }

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {

                R.id.wallets -> mPresenter?.onWalletsClicked()
                R.id.orders -> mPresenter?.onOrdersClicked()
                R.id.deposits -> mPresenter?.onDepositsClicked()
                R.id.withdrawals -> mPresenter?.onWithdrawalsClicked()
                R.id.tickets -> mPresenter?.onTicketsClicked()
                R.id.settings -> mPresenter?.onSettingsClicked()
                R.id.about -> mPresenter?.onAboutClicked()
                R.id.feedback -> mPresenter?.onFeedbackClicked()
                R.id.help -> mPresenter?.onHelpClicked()

            }

            false
        }
    }


    @SuppressWarnings("NewApi")
    private fun initNavigationViewHeaderWithUserInfo(user: User) {
        with(navigationView.getHeaderView(0)) {
            userNameTv.makeVisible()
            emailTv.makeVisible()
            signInBtn.makeGone()

            profilePictureIv.mShouldDrawBackground = true
            profilePictureIv.mBackgroundShape = MarkableImageView.Shape.OVALISH
            profilePictureIv.mMark = user.userName.first().toUpperCase().toString()
            profilePictureIv.setTextSize(dimen(R.dimen.navigation_view_header_profile_picture_mark_size))
            profilePictureIv.setOuterBackgroundColor(getCompatColor(R.color.colorAccent))
            profilePictureIv.setInnerBackgroundColor(getCompatColor(R.color.colorAccent))

            if(AT_LEAST_LOLLIPOP) {
                profilePictureIv.outlineProvider = ViewOutlineProviders.CIRCLE
                profilePictureIv.elevation = getDimension(R.dimen.navigation_view_header_profile_picture_elevation)
            }

            userNameTv.text = user.userName
            emailTv.text = user.email

            headerRl.setOnClickListener { mPresenter?.onHeaderClicked() }
        }
    }


    private fun initNavigationViewHeaderWithoutUserInfo() {
        with(navigationView.getHeaderView(0)) {
            signInBtn.makeVisible()
            userNameTv.makeGone()
            emailTv.makeGone()

            profilePictureIv.setImageResource(R.mipmap.ic_launcher_large)

            signInBtn.background = getStateListDrawable(
                R.drawable.button_bg_state_pressed,
                R.color.colorAccentDark,
                R.drawable.button_bg_state_released,
                R.color.colorAccent
            )
            signInBtn.setOnClickListener { mPresenter?.onSignInButtonClicked() }
        }
    }


    private fun hideUserSpecificNavigationViewItems() {
        val menu: Menu = navigationView.menu

        menu.findItem(R.id.wallets).isVisible = false
        menu.findItem(R.id.orders).isVisible = false
        menu.findItem(R.id.deposits).isVisible = false
        menu.findItem(R.id.withdrawals).isVisible = false
        menu.findItem(R.id.tickets).isVisible = false
    }


    override fun showAppBar(animate: Boolean) {
        appBarLayout.setExpanded(true, animate)
    }


    override fun closeDrawer() {
        drawerLayout.closeDrawer(Gravity.START)
    }


    override fun restartActivity() {
        recreate()
    }


    override fun launchFavoriteCurrencyMarketsActivity() {
        startActivity(FavoriteCurrencyMarketsActivity.newInstance(this))
    }


    override fun launchSearchActivity() {
        startActivity(CurrencyMarketsSearchActivity.newInstance(this))
    }


    override fun launchSignInActivity() {
        startActivity(LoginActivity.newInstance(this))
    }


    override fun launchWalletsActivity() {
        startActivity(WalletsActivity.newInstance(this))
    }


    override fun launchOrdersActivity() {
        startActivity(OrdersActivity.newInstance(this))
    }


    override fun launchDepositsActivity() {
        startActivity(TransactionsActivity.newDepositsInstance(this))
    }


    override fun launchWithdrawalsActivity() {
        startActivity(TransactionsActivity.newWithdrawalsInstance(this))
    }


    override fun launchTicketsActivity() {
        startActivity(TicketsActivity.newInstance(this))
    }


    override fun launchSettingsActivity() {
        startActivity(SettingsActivity.newInstance(this))
    }


    override fun launchAboutActivity() {
        startActivity(AboutActivity.newInstance(this))
    }


    override fun launchHelpActivity() {
        startActivity(HelpActivity.newInstance(this))
    }


    override fun launchFeedbackActivity() {
        startActivity(FeedbackActivity.newInstance(this))
    }


    override fun isDrawerOpen(): Boolean {
        return drawerLayout.isDrawerOpen(Gravity.START)
    }


    override fun isLandscape(): Boolean {
        return configuration.landscape
    }


    override fun getToolbarTitle(): String = getString(R.string.dashboard_toolbar_title)


    override fun getViewPager(): ViewPager = viewPager


    override fun getViewPagerAdapter(): DashboardViewPagerAdapter {
        return DashboardViewPagerAdapter(supportFragmentManager)
    }


    override fun getTabLayout(): TabLayout = tabLayout


    override fun getContentLayoutResourceId(): Int = R.layout.dashboard_activity_layout


    override fun onBackPressed() {
        if(mPresenter?.onBackPressed() == true) {
            return
        }

        super.onBackPressed()

        overridePendingTransition(
            R.anim.default_window_enter_animation,
            R.anim.default_window_exit_animation
        )
    }


    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        savedState?.apply {
            mComparator = getParcelable(SAVED_STATE_COMPARATOR)
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putParcelable(SAVED_STATE_COMPARATOR, currencyMarketsSortPanel.getComparator())
    }


}