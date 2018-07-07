package com.stocksexchange.android.repositories.orders

import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.api.model.OrderResponse
import com.stocksexchange.android.model.OrderCreationParameters
import com.stocksexchange.android.model.OrderParameters
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.repositories.Repository

interface OrdersRepository : Repository {

    suspend fun save(orders: List<Order>)

    suspend fun update(order: Order)

    suspend fun delete(type: String)

    suspend fun delete(marketName: String, type: String)

    suspend fun deleteUserOrders()

    suspend fun create(params: OrderCreationParameters): RepositoryResult<OrderResponse>

    suspend fun cancel(order: Order, params: OrderParameters): RepositoryResult<OrderResponse>

    suspend fun search(params: OrderParameters): RepositoryResult<List<Order>>

    suspend fun getActiveOrders(params: OrderParameters): RepositoryResult<List<Order>>

    suspend fun getCompletedOrders(params: OrderParameters): RepositoryResult<List<Order>>

    suspend fun getCancelledOrders(params: OrderParameters): RepositoryResult<List<Order>>

    suspend fun getPublicOrders(params: OrderParameters): RepositoryResult<List<Order>>

}