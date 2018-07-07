package com.stocksexchange.android.repositories.orders

import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.repositories.BaseRepositoryCache

class OrdersCache : BaseRepositoryCache() {


    companion object {

        const val KEY_ACTIVE_ORDERS = "active_orders"
        const val KEY_COMPLETED_ORDERS = "completed_orders"
        const val KEY_CANCELLED_ORDERS = "cancelled_orders"

    }




    fun saveActiveOrders(orders: List<Order>) {
        cache.put(KEY_ACTIVE_ORDERS, orders)
    }


    fun removeActiveOrders() {
        cache.remove(KEY_ACTIVE_ORDERS)
    }


    @Suppress("UNCHECKED_CAST")
    fun getActiveOrders(): List<Order> {
        return (cache.get(KEY_ACTIVE_ORDERS) as List<Order>)
    }


    fun hasActiveOrders(): Boolean {
        return cache.contains(KEY_ACTIVE_ORDERS)
    }


    fun saveCompletedOrders(orders: List<Order>) {
        cache.put(KEY_COMPLETED_ORDERS, orders)
    }


    fun removeCompletedOrders() {
        cache.remove(KEY_COMPLETED_ORDERS)
    }


    @Suppress("UNCHECKED_CAST")
    fun getCompletedOrders(): List<Order> {
        return (cache.get(KEY_COMPLETED_ORDERS) as List<Order>)
    }


    fun hasCompletedOrders(): Boolean {
        return cache.contains(KEY_COMPLETED_ORDERS)
    }


    fun saveCancelledOrders(orders: List<Order>) {
        cache.put(KEY_CANCELLED_ORDERS, orders)
    }


    fun removeCancelledOrders() {
        cache.remove(KEY_CANCELLED_ORDERS)
    }


    @Suppress("UNCHECKED_CAST")
    fun getCancelledOrders(): List<Order> {
        return (cache.get(KEY_CANCELLED_ORDERS) as List<Order>)
    }


    fun hasCancelledOrders(): Boolean {
        return cache.contains(KEY_CANCELLED_ORDERS)
    }


    fun savePublicOrders(key: String, orders: List<Order>) {
        cache.put(key, orders)
    }


    @Suppress("UNCHECKED_CAST")
    fun getPublicOrders(key: String): List<Order> {
        return (cache.get(key) as List<Order>)
    }


    fun hasPublicOrders(key: String): Boolean {
        return cache.contains(key)
    }


}