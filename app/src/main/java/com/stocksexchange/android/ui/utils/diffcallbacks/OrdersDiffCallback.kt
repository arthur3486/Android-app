package com.stocksexchange.android.ui.utils.diffcallbacks

import com.stocksexchange.android.api.model.Order

/**
 * A diff utility callback for [Order] model class.
 */
class OrdersDiffCallback(
    oldList: List<Order>,
    newList: List<Order>
) : BaseDiffCallback<Order>(oldList, newList) {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == newList[newItemPosition].id)
    }


}