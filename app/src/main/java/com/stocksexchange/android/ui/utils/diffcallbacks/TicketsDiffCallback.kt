package com.stocksexchange.android.ui.utils.diffcallbacks

import com.stocksexchange.android.api.model.Ticket

/**
 * A diff utility callback for [Ticket] model class.
 */
class TicketsDiffCallback(
    oldList: List<Ticket>,
    newList: List<Ticket>
) : BaseDiffCallback<Ticket>(oldList, newList) {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == newList[newItemPosition].id)
    }


}