package com.stocksexchange.android.datastores.tickets

import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.api.model.TicketCreationResponse
import com.stocksexchange.android.api.model.TicketReplyResponse
import com.stocksexchange.android.database.daos.TicketDao
import com.stocksexchange.android.datastores.exceptions.TicketsNotFoundException
import com.stocksexchange.android.database.mappings.mapToDatabaseTicketList
import com.stocksexchange.android.database.mappings.mapToTicketList
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.model.TicketCreationParameters
import com.stocksexchange.android.model.TicketParameters
import com.stocksexchange.android.model.TicketReplyParameters
import com.stocksexchange.android.utils.helpers.executeBackgroundOperation
import com.stocksexchange.android.utils.helpers.performBackgroundOperation

class TicketsDatabaseDataStore(
    private val ticketDao: TicketDao
) : TicketsDataStore {


    override suspend fun save(tickets: List<Ticket>) {
        executeBackgroundOperation {
            ticketDao.insert(tickets.mapToDatabaseTicketList())
        }
    }


    override suspend fun create(params: TicketCreationParameters): Result<TicketCreationResponse> {
        throw UnsupportedOperationException()
    }


    override suspend fun reply(params: TicketReplyParameters): Result<TicketReplyResponse> {
        throw UnsupportedOperationException()
    }


    override suspend fun search(params: TicketParameters): Result<List<Ticket>> {
        return performBackgroundOperation {
            ticketDao.search(params.searchQuery).mapToTicketList()
        }
    }


    override suspend fun get(params: TicketParameters): Result<List<Ticket>> {
        return performBackgroundOperation {
            ticketDao.getAll()
                .mapToTicketList()
                .takeUnless { it.isEmpty() }.let { it }
                ?: throw TicketsNotFoundException()
        }
    }


}