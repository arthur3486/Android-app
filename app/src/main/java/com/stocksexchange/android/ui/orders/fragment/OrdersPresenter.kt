package com.stocksexchange.android.ui.orders.fragment

import com.stocksexchange.android.ORDERS_ACTIVITY_TAB_COUNT
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.api.model.OrderResponse
import com.stocksexchange.android.api.model.OrderTypes
import com.stocksexchange.android.utils.exceptions.NoInternetException
import com.stocksexchange.android.model.DataTypes.NEW_DATA
import com.stocksexchange.android.model.DataTypes.OLD_DATA
import com.stocksexchange.android.model.HttpCodes.*
import com.stocksexchange.android.model.OrderModes
import com.stocksexchange.android.model.OrderParameters
import com.stocksexchange.android.model.PerformedOrderActions
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.android.utils.helpers.tag
import com.stocksexchange.android.events.OrderEvent
import com.stocksexchange.android.events.PerformedOrderActionsEvent
import com.stocksexchange.android.ui.base.mvp.presenters.BaseListDataLoadingPresenter
import com.stocksexchange.android.ui.utils.extensions.getWithDefault
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.standalone.inject
import retrofit2.HttpException

class OrdersPresenter(
    model: OrdersModel,
    view: OrdersContract.View
) : BaseListDataLoadingPresenter<OrdersModel, OrdersContract.View, List<Order>, OrderParameters>(model, view),
    OrdersContract.ActionListener, OrdersModel.ActionListener {


    companion object {

        private val CLASS = OrdersPresenter::class.java

        private val SAVED_STATE_PERFORMED_ACTIONS = tag(CLASS, "performed_actions")

    }


    private val mStringProvider: StringProvider by inject()

    private var mPerformedActions = PerformedOrderActions()




    init {
        model.setActionListener(this)
    }


    constructor(view: OrdersContract.View): this(OrdersModel(), view)


    override fun getDataLoadingParams(): OrderParameters {
        return mView.getOrderParameters()
    }


    override fun onViewSelected() {
        if((mView.isDataSourceEmpty() || !mModel.mWasLastDataFetchingSuccessful)
                && !mModel.mIsDataLoading
                && mModel.isDataLoadingIntervalApplied()) {
            loadData(if(mView.getOrderParameters().type == OrderTypes.PUBLIC) OLD_DATA else NEW_DATA, false)
        }
    }


    override fun onMarketNameClicked(currencyMarket: CurrencyMarket?) {
        if(currencyMarket == null) {
            mView.showToast(mStringProvider.getString(R.string.market_not_found))
        } else {
            mView.launchCurrencyMarketPreviewActivity(currencyMarket)
        }
    }


    override fun onCancelButtonClicked(order: Order) {
        if(mModel.mIsRequestFired) {
            return
        }

        mModel.performOrderCancellationRequest(order, mView.getOrderParameters())
    }


    override fun onOrderCancellationRequestSent() {
        mView.showSecondaryProgressBar()
    }


    override fun onOrderCancellationRequestReceived() {
        mView.hideSecondaryProgressBar()
    }


    override fun onOrderCancellationRequestSucceeded(orderResponse: OrderResponse, order: Order) {
        mView.deleteOrder(orderResponse.orderId)
        mView.showToast(mStringProvider.getString(R.string.order_cancelled))

        EventBus.getDefault().post(OrderEvent.cancel(order, this))
    }


    override fun onOrderCancellationRequestFailed(error: Throwable) {
        mView.showToast(when(error) {
            is NoInternetException -> mStringProvider.getNetworkCheckMessage()
            is HttpException -> {
                when(error.code()) {
                    TOO_MANY_REQUESTS.code -> mStringProvider.getTooManyRequestsMessage()
                    in INTERNAL_SERVER_ERROR.code..NETWORK_CONNECT_TIMEOUT.code -> mStringProvider.getServerUnresponsiveMessage()

                    else -> mStringProvider.getSomethingWentWrongMessage()
                }
            }

            else -> mStringProvider.getSomethingWentWrongMessage()
        })
    }


    override fun onBackPressed() {
        if(!mPerformedActions.isEmpty()) {
            EventBus.getDefault().postSticky(PerformedOrderActionsEvent.init(
                mPerformedActions, this, ORDERS_ACTIVITY_TAB_COUNT
            ))
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: PerformedOrderActionsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed(this) || event.isExhausted()) {
            return
        }

        val attachment = event.attachment

        if(attachment.hasCancelledOrders()) {
            for(order in attachment.cancelledOrdersMap.values) {
                onEvent(OrderEvent.cancel(order, event.sourceTag))
            }
        }

        event.consume(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: OrderEvent) {
        if(event.isOriginatedFrom(this)) {
            return
        }

        val orderMode = mView.getOrderParameters().mode
        val ordersType = mView.getOrderParameters().type
        val order = event.attachment

        when(orderMode) {

            OrderModes.STANDARD -> {
                when(event.action) {

                    OrderEvent.Actions.CANCELLED -> {
                        if((ordersType == OrderTypes.ACTIVE) && !mView.isDataSourceEmpty()) {
                            if(mView.containsOrder(order)) {
                                mView.deleteOrder(order.id)
                            }
                        }

                        if((ordersType == OrderTypes.CANCELLED) && !mView.isDataSourceEmpty()) {
                            if(!mView.containsOrder(order)) {
                                mView.addOrderChronologically(order)
                            }
                        }
                    }

                }
            }

            OrderModes.SEARCH -> {
                mPerformedActions.addCancelledOrder(order)
            }

        }
    }


    override fun canReceiveEvents(): Boolean {
        return true
    }


    override fun onRestoreState(savedState: MutableMap<String, Any>) {
        super.onRestoreState(savedState)

        with(savedState) {
            mPerformedActions = (getWithDefault(SAVED_STATE_PERFORMED_ACTIONS, PerformedOrderActions()) as PerformedOrderActions)
        }
    }


    override fun onSaveState(savedState: MutableMap<String, Any>) {
        super.onSaveState(savedState)

        savedState[SAVED_STATE_PERFORMED_ACTIONS] = mPerformedActions
    }


    override fun toString(): String {
        val params = mView.getOrderParameters()
        val mode = params.mode.name
        val type = params.type.name

        return "${super.toString()}_${mode}_$type"
    }


}