package com.stocksexchange.android.repositories.tickets

import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.api.model.TicketCreationResponse
import com.stocksexchange.android.api.model.TicketReplyResponse
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.TicketCreationParameters
import com.stocksexchange.android.model.TicketParameters
import com.stocksexchange.android.model.TicketReplyParameters
import com.stocksexchange.android.repositories.Repository

interface TicketsRepository : Repository {

    suspend fun save(tickets: List<Ticket>)

    suspend fun create(params: TicketCreationParameters): RepositoryResult<TicketCreationResponse>

    suspend fun reply(params: TicketReplyParameters): RepositoryResult<TicketReplyResponse>

    suspend fun search(params: TicketParameters): RepositoryResult<List<Ticket>>

    suspend fun get(params: TicketParameters): RepositoryResult<List<Ticket>>

}