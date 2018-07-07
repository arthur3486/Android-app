package com.stocksexchange.android.ui.currencymarketpreview

import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.OrderTradeTypes
import com.stocksexchange.android.model.CurrencyMarketPreviewTabs
import com.stocksexchange.android.model.PerformedCurrencyMarketActions
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.android.utils.helpers.tag
import com.stocksexchange.android.events.CurrencyMarketEvent
import com.stocksexchange.android.events.PerformedCurrencyMarketsActionsEvent
import com.stocksexchange.android.events.utils.handleCurrencyMarketEvent
import com.stocksexchange.android.ui.base.mvp.presenters.BaseViewPagerPresenter
import com.stocksexchange.android.ui.utils.extensions.getWithDefault
import org.greenrobot.eventbus.EventBus
import org.koin.standalone.inject

class CurrencyMarketPreviewPresenter(
    model: CurrencyMarketPreviewModel,
    view: CurrencyMarketPreviewContract.View
) : BaseViewPagerPresenter<CurrencyMarketPreviewModel, CurrencyMarketPreviewContract.View>(model, view),
    CurrencyMarketPreviewContract.ActionListener, CurrencyMarketPreviewModel.ActionListener {


    companion object {

        private val CLASS = CurrencyMarketPreviewPresenter::class.java

        private val SAVED_STATE_PERFORMED_ACTIONS = tag(CLASS, "performed_actions")

    }

    private var mPerformedActions = PerformedCurrencyMarketActions()

    private val mStringProvider: StringProvider by inject()




    init {
        model.setActionListener(this)
    }


    constructor(view: CurrencyMarketPreviewContract.View): this(CurrencyMarketPreviewModel(), view)


    override fun onActionButtonClicked() {
        mModel.handleFavoriteAction(mView.getCurrencyMarket())
    }


    override fun onCurrencyMarketFavorited(currencyMarket: CurrencyMarket) {
        mView.updateFavoriteButtonState(true)

        handleCurrencyMarketEvent(CurrencyMarketEvent.favorite(currencyMarket, this), mPerformedActions)
    }


    override fun onCurrencyMarketUnfavorited(currencyMarket: CurrencyMarket) {
        mView.updateFavoriteButtonState(false)

        handleCurrencyMarketEvent(CurrencyMarketEvent.unfavorite(currencyMarket, this), mPerformedActions)
    }


    override fun onTabSelected(position: Int, firstTime: Boolean) {
        when(position) {

            CurrencyMarketPreviewTabs.SUMMARY.ordinal,
            CurrencyMarketPreviewTabs.ORDERS.ordinal -> {
                mView.lockToPortraitOrientation()
            }

            else -> {
                mView.unlockFromPortraitOrientation()
            }

        }

        super.onTabSelected(position, firstTime)
    }


    override fun onTradeButtonClicked(orderTradeType: OrderTradeTypes) {
        val currencyMarket = mView.getCurrencyMarket()
        val summary = mView.getCurrencyMarketSummary()

        if(summary?.isActive != true) {
            mView.showToast(mStringProvider.getString(R.string.error_exchange_disabled))
            return
        }

        mModel.fetchUserAsync(
            {
                when(orderTradeType) {
                    OrderTradeTypes.BUY -> mView.launchBuyActivity(currencyMarket, summary, it)
                    else -> mView.launchSellActivity(currencyMarket, summary, it)
                }
            },
            {
                mView.launchLoginActivity()
            }
        )
    }


    override fun onBackButtonClicked() {
        if(!mPerformedActions.isEmpty()) {
            EventBus.getDefault().postSticky(PerformedCurrencyMarketsActionsEvent.init(
                mPerformedActions, this
            ))
        }
    }


    override fun onRestoreState(savedState: MutableMap<String, Any>) {
        super.onRestoreState(savedState)

        with(savedState) {
            mPerformedActions = (getWithDefault(SAVED_STATE_PERFORMED_ACTIONS, PerformedCurrencyMarketActions()) as PerformedCurrencyMarketActions)
        }
    }


    override fun onSaveState(savedState: MutableMap<String, Any>) {
        super.onSaveState(savedState)

        savedState[SAVED_STATE_PERFORMED_ACTIONS] = mPerformedActions
    }


    override fun toString(): String {
        return "${super.toString()}_${mView.getCurrencyMarket().name}"
    }


}