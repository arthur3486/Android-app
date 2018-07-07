package com.stocksexchange.android.ui.tickets.fragment

import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.model.DataTypes
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.TicketModes
import com.stocksexchange.android.model.TicketParameters
import com.stocksexchange.android.repositories.tickets.TicketsRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseDataLoadingModel
import com.stocksexchange.android.ui.tickets.fragment.TicketsModel.ActionListener
import org.koin.standalone.inject
import timber.log.Timber

class TicketsModel : BaseDataLoadingModel<
    List<Ticket>,
    TicketParameters,
    ActionListener
>() {


    private val mTicketsRepository: TicketsRepository by inject()




    override fun canLoadData(params: TicketParameters, dataType: DataTypes): Boolean {
        val ticketMode = params.mode
        val searchQuery = params.searchQuery

        val isTicketSearch = (ticketMode == TicketModes.SEARCH)
        val isNewData = (dataType == DataTypes.NEW_DATA)

        val isTicketSearchWithNoQuery = (isTicketSearch && searchQuery.isBlank())
        val isTicketSearchNewData = (isTicketSearch && isNewData)
        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied())

        return (!isTicketSearchWithNoQuery
                && !isTicketSearchNewData
                && !isNewDataWithIntervalNotApplied)
    }


    override fun refreshData() {
        mTicketsRepository.refresh()
    }


    override suspend fun getRepositoryResult(params: TicketParameters): RepositoryResult<List<Ticket>> {
        val result = when(params.mode) {
            TicketModes.STANDARD -> mTicketsRepository.get(params)
            TicketModes.SEARCH -> mTicketsRepository.search(params)
        }

        Timber.i("ticketsRepository.get(params: $params) = $result")

        return result
    }


    interface ActionListener : BaseDataLoadingActionListener<List<Ticket>>


}