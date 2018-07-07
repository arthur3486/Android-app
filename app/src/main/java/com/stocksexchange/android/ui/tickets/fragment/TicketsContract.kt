package com.stocksexchange.android.ui.tickets.fragment

import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.model.TicketParameters
import com.stocksexchange.android.ui.base.mvp.views.ListDataLoadingView

interface TicketsContract {


    interface View : ListDataLoadingView<List<Ticket>> {

        fun getTicketParameters(): TicketParameters

    }


    interface ActionListener


}