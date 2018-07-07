package com.stocksexchange.android.ui.tickets.creation

import com.stocksexchange.android.api.model.TicketCreationResponse
import com.stocksexchange.android.model.TicketCreationParameters
import com.stocksexchange.android.repositories.tickets.TicketsRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import org.koin.standalone.inject
import timber.log.Timber

class TicketCreationModel : BaseModel() {


    var isRequestFired: Boolean = false
        private set

    private var actionListener: ActionListener? = null

    private val ticketsRepository: TicketsRepository by inject()




    fun performTicketCreationRequest(params: TicketCreationParameters) {
        performTicketCreationRequestAsync(params)
        onTicketCreationRequestSent()
    }


    fun performTicketCreationRequestAsync(params: TicketCreationParameters) {
        performAsync {
            val result = ticketsRepository.create(params)

            Timber.i("ticketsRepository.create(params: $params) = $result")

            if(result.isSuccessful()) {
                handleSuccessfulTicketCreationResponse(result.getSuccessfulResult().value)
            } else {
                handleUnsuccessfulTicketCreationResponse(result.getErroneousResult().exception)
            }
        }
    }


    private fun handleSuccessfulTicketCreationResponse(response: TicketCreationResponse) {
        onTicketCreationResponseReceived()

        actionListener?.onTicketCreationRequestSucceeded(response)
    }


    private fun handleUnsuccessfulTicketCreationResponse(error: Throwable) {
        onTicketCreationResponseReceived()

        actionListener?.onTicketCreationRequestFailed(error)
    }


    private fun onTicketCreationRequestSent() {
        isRequestFired = true

        actionListener?.onTicketCreationRequestSent()
    }


    private fun onTicketCreationResponseReceived() {
        isRequestFired = false

        actionListener?.onTicketCreationResponseReceived()
    }


    fun setActionListener(listener: ActionListener) {
        actionListener = listener
    }


    interface ActionListener {

        fun onTicketCreationRequestSent()

        fun onTicketCreationResponseReceived()

        fun onTicketCreationRequestSucceeded(response: TicketCreationResponse)

        fun onTicketCreationRequestFailed(error: Throwable)

    }

}