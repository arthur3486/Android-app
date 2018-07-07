package com.stocksexchange.android.ui.currencymarketpreview

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.api.model.OrderTradeTypes
import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.model.CurrencyMarketPreviewTabs
import com.stocksexchange.android.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.ui.base.activities.BaseViewPagerActivity
import com.stocksexchange.android.ui.chart.fragment.ChartFragment
import com.stocksexchange.android.ui.currencymarketpreview.summary.SummaryFragment
import com.stocksexchange.android.ui.login.LoginActivity
import com.stocksexchange.android.ui.orders.fragment.OrdersFragment
import com.stocksexchange.android.ui.trade.buy.BuyActivity
import com.stocksexchange.android.ui.trade.sell.SellActivity
import com.stocksexchange.android.ui.utils.extensions.getColoredCompatDrawable
import com.stocksexchange.android.ui.utils.extensions.getStateListDrawable
import com.stocksexchange.android.ui.utils.extensions.makeVisible
import com.stocksexchange.android.ui.utils.interfaces.SummaryFetchable
import kotlinx.android.synthetic.main.currency_market_preview_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.intentFor
import org.koin.android.ext.android.get

class CurrencyMarketPreviewActivity : BaseViewPagerActivity<CurrencyMarketPreviewViewPagerAdapter, CurrencyMarketPreviewPresenter>(),
    CurrencyMarketPreviewContract.View {


    companion object {

        const val TAG = "CurrencyMarketPreviewActivity"

        private const val EXTRA_CURRENCY_MARKET = "currency_market"

        private const val SAVED_STATE_CURRENCY_MARKET = "currency_market"


        fun newInstance(context: Context, currencyMarket: CurrencyMarket): Intent {
            return context.intentFor<CurrencyMarketPreviewActivity>(
                EXTRA_CURRENCY_MARKET to currencyMarket
            )
        }

    }

    /**
     * A currency market this preview is based on.
     */
    private lateinit var mCurrencyMarket: CurrencyMarket




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.overshoot_scaling_window_b_enter_animation,
            R.anim.overshoot_scaling_window_a_exit_animation
        )
    }


    override fun initPresenter(): CurrencyMarketPreviewPresenter = CurrencyMarketPreviewPresenter(this)


    override fun init() {
        super.init()

        initBottomBar()
    }


    override fun initToolbar() {
        super.initToolbar()

        launch(UI) {
            updateFavoriteButtonState(get<CurrencyMarketsRepository>().isCurrencyMarketFavorite(mCurrencyMarket))

            toolbar.actionBtnIv.setOnClickListener { mPresenter?.onActionButtonClicked() }
            toolbar.actionBtnIv.makeVisible()
        }
    }


    override fun populateAdapter() {
        with(mAdapter) {
            addFragment(getFragment(CurrencyMarketPreviewTabs.SUMMARY.ordinal) ?: SummaryFragment.newInstance(mCurrencyMarket))
            addFragment(getFragment(CurrencyMarketPreviewTabs.CHART.ordinal) ?: ChartFragment.newStandardInstance(mCurrencyMarket.name))
            addFragment(getFragment(CurrencyMarketPreviewTabs.ORDERS.ordinal) ?: OrdersFragment.newPublicInstance(mCurrencyMarket.name))
        }
    }


    override fun initTabLayoutTabs() {
        with(mTabAnimator) {
            getTabAt(CurrencyMarketPreviewTabs.SUMMARY.ordinal)?.setTitle(getString(R.string.currency_market_preview_tab_summary_title))
            getTabAt(CurrencyMarketPreviewTabs.CHART.ordinal)?.setTitle(getString(R.string.currency_market_preview_tab_chart_title))
            getTabAt(CurrencyMarketPreviewTabs.ORDERS.ordinal)?.setTitle(getString(R.string.currency_market_preview_tab_orders_title))
        }
    }


    @Suppress("PLUGIN_WARNING")
    private fun initBottomBar() {
        buyBtn.background = getStateListDrawable(
            R.drawable.button_bg_state_pressed,
            R.color.colorGreenAccentDark,
            R.drawable.button_bg_state_released,
            R.color.colorGreenAccent
        )
        sellBtn.background = getStateListDrawable(
            R.drawable.button_bg_state_pressed,
            R.color.colorRedAccentDark,
            R.drawable.button_bg_state_released,
            R.color.colorRedAccent
        )

        buyBtn.setOnClickListener { mPresenter?.onTradeButtonClicked(OrderTradeTypes.BUY) }
        sellBtn.setOnClickListener { mPresenter?.onTradeButtonClicked(OrderTradeTypes.SELL) }
    }


    override fun lockToPortraitOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }


    override fun unlockFromPortraitOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }


    override fun updateFavoriteButtonState(isFavorite: Boolean) {
        toolbar.actionBtnIv.setImageDrawable(getColoredCompatDrawable(
            R.drawable.ic_star,
            if(isFavorite) {
                R.color.colorYellowAccentDark
            } else {
                R.color.colorPrimaryText
            }
        ))
    }


    override fun launchBuyActivity(currencyMarket: CurrencyMarket, summary: CurrencyMarketSummary,
                                   user: User) {
        startActivity(BuyActivity.newInstance(this, currencyMarket, summary, user))
    }


    override fun launchSellActivity(currencyMarket: CurrencyMarket, summary: CurrencyMarketSummary,
                                    user: User) {
        startActivity(SellActivity.newInstance(this, currencyMarket, summary, user))
    }


    override fun launchLoginActivity() {
        startActivity(LoginActivity.newInstance(this))
    }


    override fun getToolbarTitle(): String {
        return String.format(
            "%s / %s",
            mCurrencyMarket.currency,
            mCurrencyMarket.market
        )
    }


    override fun getViewPager(): ViewPager = viewPager


    override fun getViewPagerAdapter(): CurrencyMarketPreviewViewPagerAdapter {
        return CurrencyMarketPreviewViewPagerAdapter(supportFragmentManager)
    }


    override fun getTabLayout(): TabLayout = tabLayout


    override fun getCurrencyMarket(): CurrencyMarket = mCurrencyMarket


    override fun getCurrencyMarketSummary(): CurrencyMarketSummary? {
        return (mAdapter.getFragment(CurrencyMarketPreviewTabs.SUMMARY.ordinal) as? SummaryFetchable)?.getSummary()
    }


    override fun getContentLayoutResourceId(): Int = R.layout.currency_market_preview_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        mPresenter?.onBackButtonClicked()

        overridePendingTransition(
            R.anim.overshoot_scaling_window_a_enter_animation,
            R.anim.overshoot_scaling_window_b_exit_animation
        )
    }


    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        if(savedState != null) {
            mCurrencyMarket = (savedState.getSerializable(SAVED_STATE_CURRENCY_MARKET) as CurrencyMarket)
        } else {
            mCurrencyMarket = (intent?.getSerializableExtra(EXTRA_CURRENCY_MARKET) as CurrencyMarket)
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putSerializable(SAVED_STATE_CURRENCY_MARKET, mCurrencyMarket)
    }


}