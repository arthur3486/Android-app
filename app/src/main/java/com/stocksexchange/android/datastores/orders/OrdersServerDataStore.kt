package com.stocksexchange.android.datastores.orders

import com.stocksexchange.android.PUBLIC_ORDERS_LIMIT
import com.stocksexchange.android.api.StocksExchangeService
import com.stocksexchange.android.api.exceptions.InvalidCredentialsException
import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.api.model.OrderResponse
import com.stocksexchange.android.api.model.OrderStatuses
import com.stocksexchange.android.api.model.PrivateApiMethods
import com.stocksexchange.android.api.utils.extractData
import com.stocksexchange.android.api.utils.extractDataDirectly
import com.stocksexchange.android.api.utils.generateNonce
import com.stocksexchange.android.database.mappings.mapToOrderList
import com.stocksexchange.android.utils.handlers.CredentialsHandler
import com.stocksexchange.android.model.OrderCreationParameters
import com.stocksexchange.android.model.OrderParameters
import com.stocksexchange.android.model.Result
import ru.gildor.coroutines.retrofit.awaitResult
import ru.gildor.coroutines.retrofit.Result as RetrofitResult

class OrdersServerDataStore(
    private val stocksExchangeService: StocksExchangeService,
    private val credentialsHandler: CredentialsHandler
) : OrdersDataStore {


    override suspend fun save(orders: List<Order>) {
        throw UnsupportedOperationException()
    }


    override suspend fun update(order: Order) {
        throw UnsupportedOperationException()
    }


    override suspend fun delete(type: String) {
        throw UnsupportedOperationException()
    }


    override suspend fun delete(marketName: String, type: String) {
        throw UnsupportedOperationException()
    }


    override suspend fun create(params: OrderCreationParameters): Result<OrderResponse> {
        return if(credentialsHandler.hasValidCredentials()) {
            stocksExchangeService.createOrder(
                PrivateApiMethods.TRADE.methodName,
                generateNonce(), params.marketName,
                params.type, params.amount, params.rate
            ).awaitResult().extractDataDirectly()
        } else {
            Result.Failure(InvalidCredentialsException())
        }
    }


    override suspend fun cancel(orderId: Long): Result<OrderResponse> {
        return if(credentialsHandler.hasValidCredentials()) {
            stocksExchangeService.cancelOrder(
                PrivateApiMethods.CANCEL_ORDER.methodName,
                generateNonce(), orderId
            ).awaitResult().extractDataDirectly()
        } else {
            Result.Failure(InvalidCredentialsException())
        }
    }


    override suspend fun search(params: OrderParameters): Result<List<Order>> {
        throw UnsupportedOperationException()
    }


    override suspend fun getActiveOrders(params: OrderParameters): Result<List<Order>> {
        return if(credentialsHandler.hasValidCredentials()) {
            stocksExchangeService.getActiveOrders(
                PrivateApiMethods.ACTIVE_ORDERS.methodName,
                generateNonce(), params.marketName,
                params.tradeType.name, params.ownerType.name,
                params.sortType.name, params.count
            ).awaitResult().extractData{ it.mapToOrderList(params.type.name) }
        } else {
            Result.Failure(InvalidCredentialsException())
        }
    }


    override suspend fun getCompletedOrders(params: OrderParameters): Result<List<Order>> {
        return getHistoryOrders(params, OrderStatuses.FINISHED)

    }


    override suspend fun getCancelledOrders(params: OrderParameters): Result<List<Order>> {
        return getHistoryOrders(params, OrderStatuses.CANCELLED)
    }


    override suspend fun getPublicOrders(params: OrderParameters): Result<List<Order>> {
        return stocksExchangeService.getPublicOrders(params.marketName).awaitResult().extractData {
            it.take(PUBLIC_ORDERS_LIMIT).apply {
                forEach {
                    it.marketName = params.marketName
                    it.type = params.type.name
                }
            }
        }
    }


    private suspend fun getHistoryOrders(params: OrderParameters, status: OrderStatuses): Result<List<Order>> {
        return if(credentialsHandler.hasValidCredentials()) {
            stocksExchangeService.getHistoryOrders(
                PrivateApiMethods.TRADE_HISTORY.methodName,
                generateNonce(), params.marketName,
                status.id, params.ownerType.name,
                params.sortType.name, params.count
            ).awaitResult().extractData { it.mapToOrderList(params.type.name) }
        } else {
            Result.Failure(InvalidCredentialsException())
        }
    }


}