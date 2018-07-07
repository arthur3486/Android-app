package com.stocksexchange.android.events

import com.stocksexchange.android.model.PerformedOrderActions
import com.stocksexchange.android.utils.helpers.tag

/**
 * An event to send to notify subscribers about
 * [PerformedOrderActions] model class.
 */
class PerformedOrderActionsEvent private constructor(
    type: Int,
    attachment: PerformedOrderActions,
    sourceTag: String,
    consumerCount: Int
) : BaseEvent<PerformedOrderActions>(type, attachment, sourceTag, consumerCount) {


    companion object {


        fun init(performedActions: PerformedOrderActions, source: Any, consumerCount: Int): PerformedOrderActionsEvent {
            return init(performedActions, tag(source), consumerCount)
        }


        fun init(performedActions: PerformedOrderActions, sourceTag: String, consumerCount: Int): PerformedOrderActionsEvent {
            return PerformedOrderActionsEvent(TYPE_MULTIPLE_ITEMS, performedActions, sourceTag, consumerCount)
        }


    }


}