package com.stocksexchange.android.ui.orders.fragment

import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.api.model.OrderResponse
import com.stocksexchange.android.api.model.OrderTypes
import com.stocksexchange.android.model.DataTypes
import com.stocksexchange.android.model.OrderModes
import com.stocksexchange.android.model.OrderParameters
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.repositories.orders.OrdersRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseDataLoadingModel
import com.stocksexchange.android.ui.orders.fragment.OrdersModel.ActionListener
import org.koin.standalone.inject
import timber.log.Timber

class OrdersModel : BaseDataLoadingModel<
    List<Order>,
    OrderParameters,
    ActionListener
>() {


    var mIsRequestFired: Boolean = false
        private set


    private val mOrdersRepository: OrdersRepository by inject()




    override fun canLoadData(params: OrderParameters, dataType: DataTypes): Boolean {
        val orderMode = params.mode
        val searchQuery = params.searchQuery

        val isOrderSearch = (orderMode == OrderModes.SEARCH)
        val isNewData = (dataType == DataTypes.NEW_DATA)

        val isOrderSearchWithNotQuery = (isOrderSearch && searchQuery.isBlank())
        val isOrderSearchNewData = (isOrderSearch && isNewData)
        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied())

        return (!isOrderSearchWithNotQuery
                && !isOrderSearchNewData
                && !isNewDataWithIntervalNotApplied)
    }


    override fun refreshData() {
        mOrdersRepository.refresh()
    }


    override suspend fun getRepositoryResult(params: OrderParameters): RepositoryResult<List<Order>> {
        val result = when(params.mode) {
            OrderModes.STANDARD -> {
                when(params.type) {
                    OrderTypes.ACTIVE -> mOrdersRepository.getActiveOrders(params)
                    OrderTypes.COMPLETED -> mOrdersRepository.getCompletedOrders(params)
                    OrderTypes.CANCELLED -> mOrdersRepository.getCancelledOrders(params)
                    OrderTypes.PUBLIC -> mOrdersRepository.getPublicOrders(params)
                }
            }

            OrderModes.SEARCH -> mOrdersRepository.search(params)
        }

        Timber.i("ordersRepository.getOrders(params: $params) = $result")

        return result
    }


    fun performOrderCancellationRequest(order: Order, orderParameters: OrderParameters) {
        performOrderCancellationRequestAsync(order, orderParameters)
        onOrderCancellationRequestSent()
    }


    private fun performOrderCancellationRequestAsync(order: Order, orderParameters: OrderParameters) {
        performAsync {
            val result = mOrdersRepository.cancel(order, orderParameters)

            Timber.i("ordersRepository.cancel(orderId: ${order.id}) = $result")

            if(result.isSuccessful()) {
                handleSuccessfulOrderCancellationRequest(result.getSuccessfulResult().value, order)
            } else {
                handleUnsuccessfulOrderCancellationRequest(result.getErroneousResult().exception)
            }
        }
    }


    private fun handleSuccessfulOrderCancellationRequest(orderResponse: OrderResponse, order: Order) {
        onOrderCancellationRequestReceived()

        mActionListener?.onOrderCancellationRequestSucceeded(orderResponse, order)
    }


    private fun handleUnsuccessfulOrderCancellationRequest(error: Throwable) {
        onOrderCancellationRequestReceived()

        mActionListener?.onOrderCancellationRequestFailed(error)
    }


    private fun onOrderCancellationRequestSent() {
        mIsRequestFired = true

        mActionListener?.onOrderCancellationRequestSent()
    }


    private fun onOrderCancellationRequestReceived() {
        mIsRequestFired = false

        mActionListener?.onOrderCancellationRequestReceived()
    }


    interface ActionListener : BaseDataLoadingActionListener<List<Order>> {

        fun onOrderCancellationRequestSent()

        fun onOrderCancellationRequestReceived()

        fun onOrderCancellationRequestSucceeded(orderResponse: OrderResponse, order: Order)

        fun onOrderCancellationRequestFailed(error: Throwable)

    }


}