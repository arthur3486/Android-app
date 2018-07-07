package com.stocksexchange.android.ui.utils.diffcallbacks

import com.stocksexchange.android.api.model.Transaction

/**
 * A diff utility callback for [Transaction] model class.
 */
class TransactionsDiffCallback(
    oldList: List<Transaction>,
    newList: List<Transaction>
) : BaseDiffCallback<Transaction>(oldList, newList) {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == newList[newItemPosition].id)
    }


}