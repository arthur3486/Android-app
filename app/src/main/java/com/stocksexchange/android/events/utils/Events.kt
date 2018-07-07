package com.stocksexchange.android.events.utils

import com.stocksexchange.android.model.PerformedCurrencyMarketActions
import com.stocksexchange.android.events.CurrencyMarketEvent
import com.stocksexchange.android.events.PerformedCurrencyMarketsActionsEvent
import org.greenrobot.eventbus.EventBus

/**
 * Handles the [CurrencyMarketEvent] by storing them inside the [PerformedCurrencyMarketActions].
 *
 * @param event The event to handle
 * @param performedActions The container to store the event into
 */
fun handleCurrencyMarketEvent(event: CurrencyMarketEvent, performedActions: PerformedCurrencyMarketActions) {
    when(event.action) {

        CurrencyMarketEvent.Actions.UPDATED -> {
            performedActions.addUpdatedCurrencyMarket(event.attachment)
        }

        CurrencyMarketEvent.Actions.FAVORITED -> {
            performedActions.addFavoriteCurrencyMarket(event.attachment)
            performedActions.removeUnfavoriteCurrencyMarket(event.attachment)
        }

        CurrencyMarketEvent.Actions.UNFAVORITED -> {
            performedActions.addUnfavoriteCurrencyMarket(event.attachment)
            performedActions.removeFavoriteCurrencyMarket(event.attachment)
        }

    }
}


/**
 * Handles [PerformedCurrencyMarketsActionsEvent] by basically posting the appropriate
 * [CurrencyMarketEvent] events with the passed in source.
 *
 * @param source The source to send derived events with
 * @param performedActionsEvent The event to handle
 */
fun handlePerformedCurrencyMarketsActionsEvent(source: Any, performedActionsEvent: PerformedCurrencyMarketsActionsEvent) {
    val attachment = performedActionsEvent.attachment

    if(attachment.hasUpdatedCurrencyMarkets()) {
        for(currencyMarket in attachment.updatedCurrencyMarketsMap.values) {
            EventBus.getDefault().post(CurrencyMarketEvent.update(currencyMarket, source))
        }
    }

    if(attachment.hasFavoriteCurrencyMarkets()) {
        for(currencyMarket in attachment.favoriteCurrencyMarketsMap.values) {
            EventBus.getDefault().post(CurrencyMarketEvent.favorite(currencyMarket, source))
        }
    }

    if(attachment.hasUnfavoriteCurrencyMarkets()) {
        for(currencyMarket in attachment.unfavoriteCurrencyMarketsMap.values) {
            EventBus.getDefault().post(CurrencyMarketEvent.unfavorite(currencyMarket, source))
        }
    }
}