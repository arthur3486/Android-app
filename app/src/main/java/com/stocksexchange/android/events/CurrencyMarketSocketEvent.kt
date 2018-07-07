package com.stocksexchange.android.events

import com.stocksexchange.android.api.model.CurrencyMarketSocketData
import com.stocksexchange.android.utils.helpers.tag

/**
 * An event to send to notify subscribers about
 * [CurrencyMarketSocketData] model class updates.
 */
class CurrencyMarketSocketEvent private constructor(
    type: Int,
    attachment: CurrencyMarketSocketData,
    sourceTag: String
) : BaseEvent<CurrencyMarketSocketData>(type, attachment, sourceTag) {


    companion object {


        fun newInstance(data: CurrencyMarketSocketData, source: Any): CurrencyMarketSocketEvent {
            return newInstance(data, tag(source))
        }


        fun newInstance(data: CurrencyMarketSocketData, sourceTag: String): CurrencyMarketSocketEvent {
            return CurrencyMarketSocketEvent(TYPE_SINGLE_ITEM, data, sourceTag)
        }


    }


}