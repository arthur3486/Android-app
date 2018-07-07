package com.stocksexchange.android.ui.base.mvp.model

import com.stocksexchange.android.api.model.OrderResponse
import com.stocksexchange.android.model.OrderCreationParameters
import com.stocksexchange.android.repositories.orders.OrdersRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseTradeModel.BaseTradeActionListener
import org.koin.standalone.inject
import timber.log.Timber

/**
 * A base trading model to build model classes on.
 */
abstract class BaseTradeModel<
    in ActionListener: BaseTradeActionListener
> : BaseModel() {


    var mIsRequestFired: Boolean = false
        private set

    private val mOrdersRepository: OrdersRepository by inject()

    private var mActionListener: ActionListener? = null




    fun performTradeRequest(params: OrderCreationParameters) {
        performTradeRequestAsync(params)
        onTradeRequestSent()
    }


    private fun performTradeRequestAsync(params: OrderCreationParameters) {
        performAsync {
            val result = mOrdersRepository.create(params)

            Timber.i("ordersRepository.create(params: $params) = $result")

            if(result.isSuccessful()) {
                handleSuccessfulTradeResponse(result.getSuccessfulResult().value)
            } else {
                handleUnsuccessfulTradeResponse(result.getErroneousResult().exception)
            }
        }
    }


    private fun handleSuccessfulTradeResponse(orderResponse: OrderResponse) {
        onTradeResponseReceived()

        mActionListener?.onTradeRequestSucceeded(orderResponse)
    }


    private fun handleUnsuccessfulTradeResponse(error: Throwable) {
        onTradeResponseReceived()

        mActionListener?.onTradeRequestFailed(error)
    }


    private fun onTradeRequestSent() {
        mIsRequestFired = true

        mActionListener?.onTradeRequestSent()
    }


    private fun onTradeResponseReceived() {
        mIsRequestFired = false

        mActionListener?.onTradeResponseReceived()
    }


    fun setActionListener(listener: ActionListener) {
        mActionListener = listener
    }


    interface BaseTradeActionListener {

        fun onTradeRequestSent()

        fun onTradeResponseReceived()

        fun onTradeRequestSucceeded(orderResponse: OrderResponse)

        fun onTradeRequestFailed(error: Throwable)

    }


}