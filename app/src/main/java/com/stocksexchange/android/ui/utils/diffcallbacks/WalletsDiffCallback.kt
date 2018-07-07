package com.stocksexchange.android.ui.utils.diffcallbacks

import com.stocksexchange.android.model.Wallet

/**
 * A diff utility callback for [Wallet] model class.
 */
class WalletsDiffCallback(
    oldList: List<Wallet>,
    newList: List<Wallet>
) : BaseDiffCallback<Wallet>(oldList, newList) {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].currency.name == newList[newItemPosition].currency.name)
    }


}