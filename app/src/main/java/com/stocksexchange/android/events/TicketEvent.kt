package com.stocksexchange.android.events

import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.utils.helpers.tag

/**
 * An event to send to notify subscribers about
 * [Ticket] model class updates.
 */
class TicketEvent private constructor(
    type: Int,
    sourceTag: String,
    val action: Actions
) : BaseEvent<Void?>(type, null, sourceTag) {


    companion object {


        fun createTicket(source: Any): TicketEvent {
            return createTicket(tag(source))
        }


        fun createTicket(sourceTag: String): TicketEvent {
            return TicketEvent(TYPE_SINGLE_ITEM, sourceTag, Actions.CREATED)
        }


    }


    enum class Actions {

        CREATED

    }


}