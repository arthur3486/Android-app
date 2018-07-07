package com.stocksexchange.android.repositories.tickets

import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.api.model.TicketCreationResponse
import com.stocksexchange.android.api.model.TicketReplyResponse
import com.stocksexchange.android.datastores.tickets.TicketsDataStore
import com.stocksexchange.android.datastores.tickets.TicketsDatabaseDataStore
import com.stocksexchange.android.utils.exceptions.NoInternetException
import com.stocksexchange.android.model.*
import com.stocksexchange.android.utils.providers.ConnectionProvider
import com.stocksexchange.android.repositories.BaseRepository
import com.stocksexchange.android.utils.helpers.handleRepositoryResult

class TicketsRepositoryImpl(
    private val serverDataStore: TicketsDataStore,
    private val databaseDataStore: TicketsDatabaseDataStore,
    private val connectionProvider: ConnectionProvider
) : BaseRepository<TicketsCache>(), TicketsRepository {


    override val cache: TicketsCache = TicketsCache()




    override suspend fun save(tickets: List<Ticket>) {
        databaseDataStore.save(tickets)
        saveTicketsToCache(tickets)
    }


    override suspend fun create(params: TicketCreationParameters): RepositoryResult<TicketCreationResponse> {
        return if(connectionProvider.isNetworkAvailable()) {
            val result = serverDataStore.create(params)

            if(result is Result.Success) {
                //todo save to the database
            }

            RepositoryResult(result)
        } else {
            RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }
    }


    override suspend fun reply(params: TicketReplyParameters): RepositoryResult<TicketReplyResponse> {
        return if(connectionProvider.isNetworkAvailable()) {
            RepositoryResult(serverResult = serverDataStore.reply(params))
        } else {
            RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }
    }


    override suspend fun search(params: TicketParameters): RepositoryResult<List<Ticket>> {
        return RepositoryResult(databaseResult = databaseDataStore.search(params))
    }


    override suspend fun get(params: TicketParameters): RepositoryResult<List<Ticket>> {
        val result = RepositoryResult<List<Ticket>>()

        if(!cache.hasTickets() || cache.isInvalidated) {
            var onSuccess: suspend ((Result.Success<List<Ticket>>) -> Unit) = {}

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.get(params)
                onSuccess = { save(it.value) }
            }

            if(result.isServerResultErroneous(true)) {
                result.databaseResult = databaseDataStore.get(params)
                onSuccess = {
                    saveTicketsToCache(it.value)
                }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getTickets())
        }

        return result
    }


    private fun saveTicketsToCache(tickets: List<Ticket>) {
        cache.saveTickets(tickets)
    }


}