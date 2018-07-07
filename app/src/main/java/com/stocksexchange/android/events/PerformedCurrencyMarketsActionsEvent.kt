package com.stocksexchange.android.events

import com.stocksexchange.android.model.PerformedCurrencyMarketActions
import com.stocksexchange.android.utils.helpers.tag

/**
 * An event to send to notify subscribers about
 * [PerformedCurrencyMarketActions] model class.
 */
class PerformedCurrencyMarketsActionsEvent private constructor(
    type: Int,
    attachment: PerformedCurrencyMarketActions,
    sourceTag: String
) : BaseEvent<PerformedCurrencyMarketActions>(type, attachment, sourceTag) {


    companion object {


        fun init(performedActions: PerformedCurrencyMarketActions, source: Any): PerformedCurrencyMarketsActionsEvent {
            return init(performedActions, tag(source))
        }


        fun init(performedActions: PerformedCurrencyMarketActions, sourceTag: String): PerformedCurrencyMarketsActionsEvent {
            return PerformedCurrencyMarketsActionsEvent(TYPE_MULTIPLE_ITEMS, performedActions, sourceTag)
        }


    }


}