package com.stocksexchange.android.ui.utils.diffcallbacks

import com.stocksexchange.android.api.model.CurrencyMarket

/**
 * A diff utility callback for [CurrencyMarket] model class.
 */
class CurrencyMarketsDiffCallback(
    oldList: List<CurrencyMarket>,
    newList: List<CurrencyMarket>
) : BaseDiffCallback<CurrencyMarket>(oldList, newList) {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == newList[newItemPosition].id)
    }


}