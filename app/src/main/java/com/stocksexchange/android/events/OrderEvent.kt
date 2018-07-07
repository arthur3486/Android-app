package com.stocksexchange.android.events

import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.utils.helpers.tag

/**
 * An event to send to notify subscribers about
 * [Order] model class updates.
 */
class OrderEvent private constructor(
    type: Int,
    attachment: Order,
    sourceTag: String,
    val action: Actions
) : BaseEvent<Order>(type, attachment, sourceTag) {


    companion object {


        fun cancel(order: Order, source: Any): OrderEvent {
            return cancel(order, tag(source))
        }


        fun cancel(order: Order, sourceTag: String): OrderEvent {
            return OrderEvent(TYPE_SINGLE_ITEM, order, sourceTag, Actions.CANCELLED)
        }


    }


    enum class Actions {

        CANCELLED

    }


}