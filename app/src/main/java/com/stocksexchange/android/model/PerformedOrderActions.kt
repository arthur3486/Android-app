package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.android.api.model.Order
import kotlinx.android.parcel.Parcelize

/**
 * A container holding [Order] performed actions.
 */
@Parcelize
data class PerformedOrderActions(
    val cancelledOrdersMap: MutableMap<Long, Order> = mutableMapOf()
) : Parcelable {


    fun addCancelledOrder(order: Order): PerformedOrderActions {
        cancelledOrdersMap[order.id] = order
        return this
    }


    fun removeCancelledOrder(order: Order): PerformedOrderActions {
        cancelledOrdersMap.remove(order.id)
        return this
    }


    fun hasCancelledOrders(): Boolean {
        return cancelledOrdersMap.isNotEmpty()
    }


    fun isEmpty(): Boolean {
        return !hasCancelledOrders()
    }


}