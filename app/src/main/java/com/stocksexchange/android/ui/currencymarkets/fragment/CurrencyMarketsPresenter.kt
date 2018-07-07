package com.stocksexchange.android.ui.currencymarkets.fragment

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.model.CurrencyMarketTypes
import com.stocksexchange.android.model.CurrencyMarketsParameters
import com.stocksexchange.android.model.DataTypes.OLD_DATA
import com.stocksexchange.android.model.PerformedCurrencyMarketActions
import com.stocksexchange.android.utils.helpers.tag
import com.stocksexchange.android.events.CurrencyMarketEvent
import com.stocksexchange.android.events.CurrencyMarketSocketEvent
import com.stocksexchange.android.events.PerformedCurrencyMarketsActionsEvent
import com.stocksexchange.android.events.utils.handlePerformedCurrencyMarketsActionsEvent
import com.stocksexchange.android.ui.base.mvp.presenters.BaseListDataLoadingPresenter
import com.stocksexchange.android.ui.utils.extensions.getWithDefault
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CurrencyMarketsPresenter(
    model: CurrencyMarketsModel,
    view: CurrencyMarketsContract.View
) : BaseListDataLoadingPresenter<
    CurrencyMarketsModel,
    CurrencyMarketsContract.View,
    List<CurrencyMarket>,
    CurrencyMarketsParameters
>(model, view), CurrencyMarketsContract.ActionListener, CurrencyMarketsModel.ActionListener {


    companion object {

        private val CLASS = CurrencyMarketsPresenter::class.java

        private val SAVED_STATE_PERFORMED_ACTIONS = tag(CLASS, "performed_actions")

    }


    private var mPerformedActions = PerformedCurrencyMarketActions()




    init {
        model.setActionListener(this)
    }


    constructor(view: CurrencyMarketsContract.View): this(CurrencyMarketsModel(), view)


    override fun getDataLoadingParams(): CurrencyMarketsParameters = mView.getCurrencyMarketsParameters()


    override fun onViewSelected() {
        if((mView.isDataSourceEmpty() || !mModel.mWasLastDataFetchingSuccessful)
                && !mModel.mIsDataLoading
                && mModel.isDataLoadingIntervalApplied()) {
            loadData(OLD_DATA, false)
        } else {
            mView.sortDataSetIfNecessary()
        }
    }


    override fun onCurrencyMarketItemClicked(currencyMarket: CurrencyMarket) {
        mView.launchCurrencyMarketPreviewActivity(currencyMarket)
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(event: PerformedCurrencyMarketsActionsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        val currencyMarketType = mView.getCurrencyMarketsParameters().currencyMarketType

        handlePerformedCurrencyMarketsActionsEvent(event, event)

        // Merging with this fragment's actions in order to pass it
        // backwards in the stack
        if((currencyMarketType == CurrencyMarketTypes.FAVORITES) ||
            (currencyMarketType == CurrencyMarketTypes.SEARCH)) {
            mPerformedActions.merge(event.attachment)
        }

        event.consume()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CurrencyMarketEvent) {
        if(event.isOriginatedFrom(this)) {
            return
        }

        val currencyMarketType = mView.getCurrencyMarketsParameters().currencyMarketType
        val currencyMarket = event.attachment

        when(event.action) {

            CurrencyMarketEvent.Actions.UPDATED -> {
                val index = mView.getMarketIndexForMarketId(currencyMarket.id)

                if(index != null) {
                    mView.updateItemWith(currencyMarket, index)
                }
            }

            CurrencyMarketEvent.Actions.FAVORITED -> {
                if(currencyMarketType == CurrencyMarketTypes.FAVORITES) {
                    if(!mView.containsCurrencyMarket(currencyMarket)) {
                        mView.addCurrencyMarketChronologically(currencyMarket)
                    }
                }
            }

            CurrencyMarketEvent.Actions.UNFAVORITED -> {
                if(currencyMarketType == CurrencyMarketTypes.FAVORITES) {
                    if(mView.containsCurrencyMarket(currencyMarket)) {
                        mView.deleteCurrencyMarket(currencyMarket)
                    }
                }
            }

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CurrencyMarketSocketEvent) {
        if(event.isOriginatedFrom(this) || mView.isDataSourceEmpty()) {
            return
        }

        val attachment = event.attachment
        val index = mView.getMarketIndexForMarketId(attachment.id) ?: return
        val currencyMarket = mView.getItem(index)?.copy(
            lastPrice = attachment.lastPrice,
            dailyVolume = (attachment.lastVolume / attachment.lastPrice)
        ) ?: return
        val type = mView.getCurrencyMarketsParameters().currencyMarketType

        // Updating the adapter
        mView.updateItemWith(currencyMarket, index)

        // Updating the model
        mModel.updateCurrencyMarket(currencyMarket)

        // Storing the updated currency market to pass it on to
        // previous fragments (if any)
        if((type == CurrencyMarketTypes.FAVORITES) ||
            (type == CurrencyMarketTypes.SEARCH)) {
            mPerformedActions.addUpdatedCurrencyMarket(currencyMarket)
        }
    }


    override fun canReceiveEvents(): Boolean {
        return true
    }


    override fun onBackPressed() {
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
        return "${super.toString()}_${mView.getCurrencyMarketsParameters().currencyMarketType}"
    }


}