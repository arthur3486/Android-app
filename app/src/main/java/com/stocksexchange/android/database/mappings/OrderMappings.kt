package com.stocksexchange.android.database.mappings

import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.database.model.DatabaseOrder

fun Order.mapToDatabaseOrder(): DatabaseOrder {
    return DatabaseOrder(
        marketName = marketName,
        currency = currency,
        market = market,
        type = type,
        tradeType = tradeType,
        amount = amount,
        originalAmount = originalAmount,
        buyAmount = buyAmount,
        sellAmount = sellAmount,
        rate = rate,
        ratesMap = ratesMap,
        timestamp = timestamp,
        id = id
    )
}


fun List<Order>.mapToDatabaseOrderList(): List<DatabaseOrder> {
    return map { it.mapToDatabaseOrder() }
}


fun DatabaseOrder.mapToOrder(): Order {
    return Order(
        marketName = marketName,
        type = type,
        tradeType = tradeType,
        amount = amount,
        originalAmount = originalAmount,
        buyAmount = buyAmount,
        sellAmount = sellAmount,
        rate = rate,
        ratesMap = ratesMap,
        timestamp = timestamp,
        id = id
    )
}


fun List<DatabaseOrder>.mapToOrderList(): List<Order> {
    return map { it.mapToOrder() }
}


fun Map<String, Order>.mapToOrderList(type: String): List<Order> {
    val list: MutableList<Order> = mutableListOf()

    map { (id, order) ->
        order.id = id.toLong()
        order.type = type

        list.add(order)
    }

    return list
}