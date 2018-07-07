package com.stocksexchange.android.repositories.tickets

import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.repositories.BaseRepositoryCache

class TicketsCache : BaseRepositoryCache() {


    companion object {

        private const val KEY_TICKETS = "tickets"

    }




    fun saveTickets(tickets: List<Ticket>) {
        cache.put(KEY_TICKETS, tickets)
    }


    @Suppress("UNCHECKED_CAST")
    fun getTickets(): List<Ticket> {
        return cache.get(KEY_TICKETS) as List<Ticket>
    }


    fun hasTickets(): Boolean {
        return cache.contains(KEY_TICKETS)
    }


}