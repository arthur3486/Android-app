package com.stocksexchange.android.ui.tickets.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseRecyclerViewAdapter

class TicketsRecyclerViewAdapter(
    context: Context,
    items: MutableList<Ticket>
) : BaseRecyclerViewAdapter<Ticket, TicketViewHolder, TicketResources>(context, items) {


    /**
     * Resources for the adapter.
     */
    private val mResources: TicketResources = TicketResources.newInstance(context)




    override fun onCreateViewHolder(parent: ViewGroup, inflater: LayoutInflater,
                                    resources: TicketResources?, viewType: Int): TicketViewHolder {
        return TicketViewHolder(
            inflater.inflate(TicketViewHolder.MAIN_LAYOUT_ID, parent, false),
            resources
        )
    }


    override fun onBindViewHolder(viewHolder: TicketViewHolder, itemModel: Ticket,
                                  resources: TicketResources?, viewType: Int) {
        viewHolder.bind(itemModel, resources)
    }


    override fun getResources(): TicketResources? {
        return mResources
    }


}