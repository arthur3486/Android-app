package com.stocksexchange.android.repositories.orders

import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.api.model.OrderResponse
import com.stocksexchange.android.api.model.OrderTypes
import com.stocksexchange.android.datastores.orders.OrdersDataStore
import com.stocksexchange.android.datastores.orders.OrdersDatabaseDataStore
import com.stocksexchange.android.utils.exceptions.NoInternetException
import com.stocksexchange.android.model.*
import com.stocksexchange.android.utils.providers.ConnectionProvider
import com.stocksexchange.android.repositories.BaseRepository
import com.stocksexchange.android.repositories.users.UsersRepository
import com.stocksexchange.android.utils.helpers.handleRepositoryResult

class OrdersRepositoryImpl(
    private val serverDataStore: OrdersDataStore,
    private val databaseDataStore: OrdersDatabaseDataStore,
    private val usersRepository: UsersRepository,
    private val connectionProvider: ConnectionProvider
) : BaseRepository<OrdersCache>(), OrdersRepository {


    override val cache: OrdersCache = OrdersCache()




    override suspend fun save(orders: List<Order>) {
        databaseDataStore.save(orders)
    }


    override suspend fun update(order: Order) {
        databaseDataStore.update(order)
    }


    override suspend fun delete(type: String) {
        databaseDataStore.delete(type)
    }


    override suspend fun delete(marketName: String, type: String) {
        databaseDataStore.delete(marketName, type)
    }


    override suspend fun deleteUserOrders() {
        databaseDataStore.delete(OrderTypes.ACTIVE.name)
        databaseDataStore.delete(OrderTypes.COMPLETED.name)
        databaseDataStore.delete(OrderTypes.CANCELLED.name)

        if(cache.hasActiveOrders()) {
            cache.removeActiveOrders()
        }

        if(cache.hasCompletedOrders()) {
            cache.removeCompletedOrders()
        }

        if(cache.hasCancelledOrders()) {
            cache.removeCancelledOrders()
        }
    }


    override suspend fun create(params: OrderCreationParameters): RepositoryResult<OrderResponse> {
        return if(connectionProvider.isNetworkAvailable()) {
            val result = RepositoryResult(serverResult = serverDataStore.create(params))

            if(result.isServerResultSuccessful()) {
                updateSignedInUserFunds(result.getSuccessfulResult().value.funds)
            }

            result
        } else {
            RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }
    }


    override suspend fun cancel(order: Order, params: OrderParameters): RepositoryResult<OrderResponse> {
        return if(connectionProvider.isNetworkAvailable()) {
            val result = RepositoryResult(serverResult = serverDataStore.cancel(order.id))

            if(result.isServerResultSuccessful()) {
                val updatedOrder = order.copy(type = OrderTypes.CANCELLED.name)

                // Updating the entry inside the database
                update(updatedOrder)

                // Deleting the entry from the cache
                if(cache.hasActiveOrders()) {
                    cache.saveActiveOrders(cache.getActiveOrders().filterNot { it.id == order.id })
                }

                // Adding the entry to the cancelled orders cache
                if(cache.hasCancelledOrders()) {
                    val cancelledOrders = cache.getCancelledOrders().toMutableList()

                    val index = cancelledOrders.indices.firstOrNull {
                        cancelledOrders[it].compareTo(order) == (if(params.sortType == SortTypes.ASC) 1 else -1)
                    } ?: cancelledOrders.size

                    cancelledOrders.add(index, updatedOrder)
                    saveCancelledOrdersToCache(cancelledOrders)
                }

                updateSignedInUserFunds(result.getSuccessfulResult().value.funds)
            }

            result
        } else {
            RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }
    }


    override suspend fun search(params: OrderParameters): RepositoryResult<List<Order>> {
        val result = when(params.type) {
            OrderTypes.ACTIVE -> getActiveOrders(params)
            OrderTypes.COMPLETED -> getCompletedOrders(params)
            OrderTypes.CANCELLED -> getCancelledOrders(params)
            OrderTypes.PUBLIC -> getPublicOrders(params)
        }

        // Making sure that the data is present since the search is performed
        // solely on database records
        return if(result.isSuccessful()) {
            RepositoryResult(databaseResult = databaseDataStore.search(params))
        } else {
            result
        }
    }


    override suspend fun getActiveOrders(params: OrderParameters): RepositoryResult<List<Order>> {
        val result = RepositoryResult<List<Order>>()

        if(!cache.hasActiveOrders() || cache.isInvalidated) {
            var onSuccess: suspend ((Result.Success<List<Order>>) -> Unit) = {}

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.getActiveOrders(params)
                onSuccess = {
                    // Getting rid of old ones since most of them are already closed
                    // and it would be a mistake to show them to the user in the
                    // future database loads (e.g., when the network is not
                    // available)
                    delete(params.type.name)
                    save(it.value)
                    saveActiveOrdersToCache(it.value)
                }
            }

            if(result.isServerResultErroneous(true)) {
                result.databaseResult = databaseDataStore.getActiveOrders(params)
                onSuccess = { saveActiveOrdersToCache(it.value) }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getActiveOrders())
        }

        return result
    }


    override suspend fun getCompletedOrders(params: OrderParameters): RepositoryResult<List<Order>> {
        val result = RepositoryResult<List<Order>>()

        if(!cache.hasCompletedOrders() || cache.isInvalidated) {
            var onSuccess: suspend ((Result.Success<List<Order>>) -> Unit) = {}

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.getCompletedOrders(params)
                onSuccess = {
                    // Deleting the old completed orders to remove the redundant ones
                    // since the new data set may not already have them
                    delete(params.type.name)
                    save(it.value)
                    saveCompletedOrdersToCache(it.value)
                }
            }

            if(result.isServerResultErroneous(true)) {
                result.databaseResult = databaseDataStore.getCompletedOrders(params)
                onSuccess = { saveCompletedOrdersToCache(it.value) }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getCompletedOrders())
        }

        return result
    }


    override suspend fun getCancelledOrders(params: OrderParameters): RepositoryResult<List<Order>> {
        val result = RepositoryResult<List<Order>>()

        if(!cache.hasCancelledOrders() || cache.isInvalidated) {
            var onSuccess: suspend ((Result.Success<List<Order>>) -> Unit) = {}

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.getCancelledOrders(params)
                onSuccess = {
                    // Deleting the old cancelled orders to remove the redundant ones
                    // since the new data set may not already have them
                    delete(params.type.name)
                    save(it.value)
                    saveCancelledOrdersToCache(it.value)
                }
            }

            if(result.isServerResultErroneous(true)) {
                result.databaseResult = databaseDataStore.getCancelledOrders(params)
                onSuccess = { saveCancelledOrdersToCache(it.value) }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getCancelledOrders())
        }

        return result
    }


    override suspend fun getPublicOrders(params: OrderParameters): RepositoryResult<List<Order>> {
        val result = RepositoryResult<List<Order>>()

        if(!cache.hasPublicOrders(params.toString()) || cache.isInvalidated) {
            var onSuccess: suspend ((Result.Success<List<Order>>) -> Unit) = {}

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.getPublicOrders(params)
                onSuccess = {
                    // Getting rid of old ones since they aren't much needed
                    // and it would be a mistake to show them to the user in the
                    // future database loads (e.g., when the network is not
                    // available)
                    delete(params.marketName, params.type.name)
                    save(it.value)
                    savePublicOrdersToCache(params, it.value)
                }
            }

            if(result.isServerResultErroneous(true)) {
                result.databaseResult = databaseDataStore.getPublicOrders(params)
                onSuccess = { savePublicOrdersToCache(params, it.value) }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getPublicOrders(params.toString()))
        }

        return result
    }


    private suspend fun updateSignedInUserFunds(funds: Map<String, String>) {
        val userResult = usersRepository.getSignedInUser()

        if(userResult.isSuccessful()) {
            usersRepository.saveSignedInUser(userResult.getSuccessfulResult().value.copy(funds = funds))
        }
    }


    private fun saveActiveOrdersToCache(orders: List<Order>) {
        cache.saveActiveOrders(orders)
    }


    private fun saveCompletedOrdersToCache(orders: List<Order>) {
        cache.saveCompletedOrders(orders)
    }


    private fun saveCancelledOrdersToCache(orders: List<Order>) {
        cache.saveCancelledOrders(orders)
    }


    private fun savePublicOrdersToCache(params: OrderParameters, orders: List<Order>) {
        cache.savePublicOrders(params.toString(), orders)
    }


}