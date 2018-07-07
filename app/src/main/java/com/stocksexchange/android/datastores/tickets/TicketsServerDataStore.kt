package com.stocksexchange.android.datastores.tickets

import com.stocksexchange.android.api.StocksExchangeService
import com.stocksexchange.android.api.exceptions.InvalidCredentialsException
import com.stocksexchange.android.api.model.PrivateApiMethods
import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.api.model.TicketCreationResponse
import com.stocksexchange.android.api.model.TicketReplyResponse
import com.stocksexchange.android.api.utils.extractData
import com.stocksexchange.android.api.utils.extractDataDirectly
import com.stocksexchange.android.api.utils.generateNonce
import com.stocksexchange.android.database.mappings.mapToTicketList
import com.stocksexchange.android.utils.handlers.CredentialsHandler
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.model.TicketCreationParameters
import com.stocksexchange.android.model.TicketParameters
import com.stocksexchange.android.model.TicketReplyParameters
import ru.gildor.coroutines.retrofit.awaitResult

class TicketsServerDataStore(
    private val stocksExchangeService: StocksExchangeService,
    private val credentialsHandler: CredentialsHandler
) : TicketsDataStore {


    override suspend fun save(tickets: List<Ticket>) {
        throw UnsupportedOperationException()
    }


    override suspend fun create(params: TicketCreationParameters): Result<TicketCreationResponse> {
        return if(credentialsHandler.hasValidCredentials()) {
            stocksExchangeService.createTicket(
                PrivateApiMethods.CREATE_TICKET.methodName,
                generateNonce(), params.category.id,
                params.currency, params.subject, params.message
            ).awaitResult().extractDataDirectly()
        } else {
            Result.Failure(InvalidCredentialsException())
        }
    }


    override suspend fun reply(params: TicketReplyParameters): Result<TicketReplyResponse> {
        return if(credentialsHandler.hasValidCredentials()) {
            stocksExchangeService.replyToTicket(
                PrivateApiMethods.REPLY_TO_TICKET.methodName,
                generateNonce(), params.ticketId, params.message
            ).awaitResult().extractDataDirectly()
        } else {
            Result.Failure(InvalidCredentialsException())
        }
    }


    override suspend fun search(params: TicketParameters): Result<List<Ticket>> {
        throw UnsupportedOperationException()
    }


    override suspend fun get(params: TicketParameters): Result<List<Ticket>> {
        return if(credentialsHandler.hasValidCredentials()) {
             stocksExchangeService.getTickets(
                 PrivateApiMethods.GET_TICKETS.methodName,
                 generateNonce(), params.ticketId,
                 params.status?.id, params.category?.id
             ).awaitResult().extractData { it.mapToTicketList() }
        } else {
            Result.Failure(InvalidCredentialsException())
        }
    }


}