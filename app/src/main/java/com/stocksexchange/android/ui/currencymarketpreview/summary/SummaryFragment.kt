package com.stocksexchange.android.ui.currencymarketpreview.summary

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.ui.base.fragments.BaseDataLoadingFragment
import com.stocksexchange.android.ui.utils.DoubleFormatter
import com.stocksexchange.android.ui.utils.extensions.getCompatColor
import com.stocksexchange.android.ui.utils.extensions.getLocale
import com.stocksexchange.android.ui.utils.extensions.setPaddingBottom
import com.stocksexchange.android.ui.utils.extensions.setPaddingTop
import com.stocksexchange.android.ui.utils.interfaces.SummaryFetchable
import com.stocksexchange.android.ui.views.InfoView
import kotlinx.android.synthetic.main.summary_fragment_layout.view.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.dimen

class SummaryFragment : BaseDataLoadingFragment<SummaryPresenter, CurrencyMarketSummary>(),
    SummaryContract.View, SummaryFetchable {


    companion object {

        private const val SAVED_STATE_CURRENCY_MARKET = "currency_market"
        private const val SAVED_STATE_CURRENCY_MARKET_SUMMARY = "currency_market_summary"


        fun newInstance(currencyMarket: CurrencyMarket): SummaryFragment {
            val fragment =  SummaryFragment()

            fragment.currencyMarket = currencyMarket

            return fragment
        }

    }


    /**
     * A currency market this preview is based on.
     */
    private lateinit var currencyMarket: CurrencyMarket

    /**
     * Details about the currency market.
     */
    private var currencyMarketSummary: CurrencyMarketSummary? = null




    override fun initPresenter(): SummaryPresenter = SummaryPresenter(this)


    override fun init() {
        super.init()

        initSummary()
    }


    private fun initSummary() {
        if(!isDataSourceEmpty()) {
            addData(currencyMarketSummary!!)
        }
    }


    override fun adjustView(view: View) {
        super.adjustView(view)

        when(view.id) {

            R.id.infoView -> {
                view.setPaddingTop(dimen(R.dimen.currency_market_preview_fragment_info_view_padding_top))
                view.setPaddingBottom(dimen(R.dimen.currency_market_preview_fragment_info_view_padding_bottom))
            }

        }
    }


    override fun addData(data: CurrencyMarketSummary) {
        currencyMarketSummary = data

        if(data.isActive) {
            mRootView.statusOb.setSubtitleText(getString(R.string.state_active))
            mRootView.statusOb.setSubtitleColor(ctx.getCompatColor(R.color.colorGreenAccent))
        } else {
            mRootView.statusOb.setSubtitleText(getString(R.string.state_disabled))
            mRootView.statusOb.setSubtitleColor(ctx.getCompatColor(R.color.colorRedAccent))
        }

        val formatter = DoubleFormatter.getInstance(ctx.getLocale())

        mRootView.currencyOb.setSubtitleText(data.currencyName)
        mRootView.lastPriceOb.setSubtitleText("${formatter.formatPrice(currencyMarket.lastPrice)} ${currencyMarket.market}")
        mRootView.dailyMaxBuyOb.setSubtitleText("${formatter.formatPrice(currencyMarket.dailyBuyMaxPrice)} ${currencyMarket.market}")
        mRootView.dailyMinSellOb.setSubtitleText("${formatter.formatPrice(currencyMarket.dailySellMinPrice)} ${currencyMarket.market}")

        mRootView.dailyChangeOb.setSubtitleText(formatter.formatDailyChange(currencyMarket.dailyChange))
        mRootView.dailyChangeOb.setSubtitleColor(ctx.getCompatColor(when {
            (currencyMarket.dailyChange > 0.0) -> R.color.colorGreenAccent
            (currencyMarket.dailyChange < 0.0) -> R.color.colorRedAccent

            else -> R.color.colorBlueAccent
        }))

        mRootView.dailyVolumeOb.setSubtitleText("${formatter.formatVolume(currencyMarket.dailyVolume)} ${currencyMarket.currency}")
        mRootView.minOrderAmountOb.setSubtitleText(formatter.formatMinOrderAmount(currencyMarket.minOrderAmount))
        mRootView.buyFeeOb.setSubtitleText(formatter.formatFeePercent(data.buyFeePercent))
        mRootView.sellFeeOb.setSubtitleText(formatter.formatFeePercent(data.sellFeePercent))
    }


    override fun isDataSourceEmpty(): Boolean {
        return (currencyMarketSummary == null)
    }


    override fun getInfoViewIcon(iconProvider: InfoViewIconProvider): Drawable? {
        return iconProvider.getSummaryIcon()
    }


    override fun getEmptyViewCaption(): String {
        return getString(R.string.error_no_summary_available)
    }


    override fun getMainView(): View = mRootView.summaryContainerLl


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getCurrencyMarket(): CurrencyMarket {
        return currencyMarket
    }


    override fun getSummary(): CurrencyMarketSummary? {
        return currencyMarketSummary
    }


    override fun getContentLayoutResourceId(): Int = R.layout.summary_fragment_layout


    override fun onRestoreState(savedState: Bundle?) {
        if(savedState != null) {
            currencyMarket = (savedState.getSerializable(SAVED_STATE_CURRENCY_MARKET) as CurrencyMarket)
            currencyMarketSummary = savedState.getParcelable(SAVED_STATE_CURRENCY_MARKET_SUMMARY)
        }

        super.onRestoreState(savedState)
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putSerializable(SAVED_STATE_CURRENCY_MARKET, currencyMarket)
        savedState.putParcelable(SAVED_STATE_CURRENCY_MARKET_SUMMARY, currencyMarketSummary)
    }


}