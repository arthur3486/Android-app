package com.stocksexchange.android.events

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.utils.helpers.tag

/**
 * An event to send to notify subscribers about
 * [CurrencyMarket] model class updates.
 */
class CurrencyMarketEvent private constructor(
    type: Int,
    attachment: CurrencyMarket,
    sourceTag: String,
    val action: Actions
) : BaseEvent<CurrencyMarket>(type, attachment, sourceTag) {


    companion object {


        fun update(currencyMarket: CurrencyMarket, source: Any): CurrencyMarketEvent {
            return update(currencyMarket, tag(source))
        }


        fun update(currencyMarket: CurrencyMarket, sourceTag: String): CurrencyMarketEvent {
            return CurrencyMarketEvent(TYPE_SINGLE_ITEM, currencyMarket, sourceTag, Actions.UPDATED)
        }


        fun favorite(currencyMarket: CurrencyMarket, source: Any): CurrencyMarketEvent {
            return favorite(currencyMarket, tag(source))
        }


        fun favorite(currencyMarket: CurrencyMarket, sourceTag: String): CurrencyMarketEvent {
            return CurrencyMarketEvent(TYPE_SINGLE_ITEM, currencyMarket, sourceTag, Actions.FAVORITED)
        }


        fun unfavorite(currencyMarket: CurrencyMarket, source: Any): CurrencyMarketEvent {
            return unfavorite(currencyMarket, tag(source))
        }


        fun unfavorite(currencyMarket: CurrencyMarket, sourceTag: String): CurrencyMarketEvent {
            return CurrencyMarketEvent(TYPE_SINGLE_ITEM, currencyMarket, sourceTag, Actions.UNFAVORITED)
        }


    }


    enum class Actions {

        UPDATED,
        FAVORITED,
        UNFAVORITED

    }


}