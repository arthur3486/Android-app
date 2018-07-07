package com.stocksexchange.android.ui.tickets

interface TicketsActivityContract {


    interface View {

        fun launchTicketSearchActivity()

        fun launchTicketCreationActivity()

    }


    interface ActionListener {

        fun onActionButtonClicked()

        fun onActionButtonFabClicked()

    }


}