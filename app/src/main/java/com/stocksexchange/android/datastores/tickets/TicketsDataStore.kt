package com.stocksexchange.android.datastores.tickets

import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.api.model.TicketCreationResponse
import com.stocksexchange.android.api.model.TicketReplyResponse
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.model.TicketCreationParameters
import com.stocksexchange.android.model.TicketParameters
import com.stocksexchange.android.model.TicketReplyParameters

interface TicketsDataStore {

    suspend fun save(tickets: List<Ticket>)

    suspend fun create(params: TicketCreationParameters): Result<TicketCreationResponse>

    suspend fun reply(params: TicketReplyParameters): Result<TicketReplyResponse>

    suspend fun search(params: TicketParameters): Result<List<Ticket>>

    suspend fun get(params: TicketParameters): Result<List<Ticket>>

}