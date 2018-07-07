package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.android.api.model.CurrencyMarket
import kotlinx.android.parcel.Parcelize

/**
 * A container holding [CurrencyMarket] performed actions.
 */
@Parcelize
data class PerformedCurrencyMarketActions(
    val updatedCurrencyMarketsMap: MutableMap<Long, CurrencyMarket> = mutableMapOf(),
    val favoriteCurrencyMarketsMap: MutableMap<Long, CurrencyMarket> = mutableMapOf(),
    val unfavoriteCurrencyMarketsMap: MutableMap<Long, CurrencyMarket> = mutableMapOf()
) : Parcelable {


    fun addUpdatedCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        updatedCurrencyMarketsMap[currencyMarket.id] = currencyMarket
        return this
    }


    fun removeUpdatedCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        updatedCurrencyMarketsMap.remove(currencyMarket.id)
        return this
    }


    fun hasUpdatedCurrencyMarkets(): Boolean {
        return updatedCurrencyMarketsMap.isNotEmpty()
    }


    fun addFavoriteCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        favoriteCurrencyMarketsMap[currencyMarket.id] = currencyMarket
        return this
    }


    fun removeFavoriteCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        favoriteCurrencyMarketsMap.remove(currencyMarket.id)
        return this
    }


    fun hasFavoriteCurrencyMarkets(): Boolean {
        return favoriteCurrencyMarketsMap.isNotEmpty()
    }


    fun addUnfavoriteCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        unfavoriteCurrencyMarketsMap[currencyMarket.id] = currencyMarket
        return this
    }


    fun removeUnfavoriteCurrencyMarket(currencyMarket: CurrencyMarket): PerformedCurrencyMarketActions {
        unfavoriteCurrencyMarketsMap.remove(currencyMarket.id)
        return this
    }


    fun hasUnfavoriteCurrencyMarkets(): Boolean {
        return unfavoriteCurrencyMarketsMap.isNotEmpty()
    }


    fun merge(actions: PerformedCurrencyMarketActions): PerformedCurrencyMarketActions {
        updatedCurrencyMarketsMap.putAll(actions.updatedCurrencyMarketsMap)
        favoriteCurrencyMarketsMap.putAll(actions.favoriteCurrencyMarketsMap)
        unfavoriteCurrencyMarketsMap.putAll(actions.unfavoriteCurrencyMarketsMap)

        return this
    }


    fun isEmpty(): Boolean {
        return (
            !hasUpdatedCurrencyMarkets() &&
            !hasFavoriteCurrencyMarkets() &&
            !hasUnfavoriteCurrencyMarkets()
        )
    }


}