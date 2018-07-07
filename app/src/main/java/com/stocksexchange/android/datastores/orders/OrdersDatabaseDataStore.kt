package com.stocksexchange.android.datastores.orders

import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.api.model.OrderResponse
import com.stocksexchange.android.database.daos.OrderDao
import com.stocksexchange.android.database.mappings.mapToDatabaseOrder
import com.stocksexchange.android.datastores.exceptions.OrdersNotFoundException
import com.stocksexchange.android.database.mappings.mapToDatabaseOrderList
import com.stocksexchange.android.database.mappings.mapToOrderList
import com.stocksexchange.android.model.OrderCreationParameters
import com.stocksexchange.android.model.OrderParameters
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.model.SortTypes
import com.stocksexchange.android.utils.helpers.executeBackgroundOperation
import com.stocksexchange.android.utils.helpers.performBackgroundOperation

class OrdersDatabaseDataStore(
    private val orderDao: OrderDao
) : OrdersDataStore {


    override suspend fun save(orders: List<Order>) {
        executeBackgroundOperation {
            orders
                .mapToDatabaseOrderList()
                .let { orderDao.insert(it) }
        }
    }


    override suspend fun update(order: Order) {
        executeBackgroundOperation {
            order.mapToDatabaseOrder().let { orderDao.update(it) }
        }
    }


    override suspend fun delete(type: String) {
        executeBackgroundOperation {
            orderDao.delete(type)
        }
    }


    override suspend fun delete(marketName: String, type: String) {
        executeBackgroundOperation {
            orderDao.delete(marketName, type)
        }
    }


    override suspend fun create(params: OrderCreationParameters): Result<OrderResponse> {
        throw UnsupportedOperationException()
    }


    override suspend fun cancel(orderId: Long): Result<OrderResponse> {
        throw UnsupportedOperationException()
    }


    override suspend fun search(params: OrderParameters): Result<List<Order>> {
        return performBackgroundOperation {
            val query = params.searchQuery.toLowerCase()
            val type = params.type.name
            val count = params.count

            when(params.sortType) {
                SortTypes.ASC -> orderDao.searchAsc(query, type, count)
                SortTypes.DESC -> orderDao.searchDesc(query, type, count)
            }.mapToOrderList()
        }
    }


    override suspend fun getActiveOrders(params: OrderParameters): Result<List<Order>> {
        return getUserOrders(params)
    }


    override suspend fun getCompletedOrders(params: OrderParameters): Result<List<Order>> {
        return getUserOrders(params)
    }


    override suspend fun getCancelledOrders(params: OrderParameters): Result<List<Order>> {
        return getUserOrders(params)
    }


    override suspend fun getPublicOrders(params: OrderParameters): Result<List<Order>> {
        return performBackgroundOperation {
            when(params.sortType) {
                SortTypes.ASC -> orderDao.getAsc(params.marketName, params.type.name, params.count)
                SortTypes.DESC -> orderDao.getDesc(params.marketName, params.type.name, params.count)
            }.mapToOrderList()
                .takeUnless { it.isEmpty() }.let { it }
                ?: throw OrdersNotFoundException()
        }
    }


    private suspend fun getUserOrders(params: OrderParameters): Result<List<Order>> {
        return performBackgroundOperation {
            when(params.sortType) {
                SortTypes.ASC -> orderDao.getAsc(params.type.name, params.count)
                SortTypes.DESC -> orderDao.getDesc(params.type.name, params.count)
            }.mapToOrderList()
                .takeUnless { it.isEmpty() }.let { it }
                ?: throw OrdersNotFoundException()
        }
    }


}