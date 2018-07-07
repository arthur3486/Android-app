package com.stocksexchange.android.ui.currencymarketpreview.summary

import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.SummaryParameters
import com.stocksexchange.android.repositories.currencymarketsummaries.CurrencyMarketSummariesRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseDataLoadingModel
import com.stocksexchange.android.ui.currencymarketpreview.summary.SummaryModel.ActionListener
import org.koin.standalone.inject
import timber.log.Timber

class SummaryModel : BaseDataLoadingModel<
    CurrencyMarketSummary,
    SummaryParameters,
    ActionListener
>() {


    private val currencyMarketSummariesRepository: CurrencyMarketSummariesRepository by inject()




    override fun refreshData() {
        currencyMarketSummariesRepository.refresh()
    }


    override suspend fun getRepositoryResult(params: SummaryParameters): RepositoryResult<CurrencyMarketSummary> {
        val result = currencyMarketSummariesRepository.get(params.currencyMarket)

        Timber.i("currencyMarketSummariesRepository.get(currencyMarket: ${params.currencyMarket}) = $result")

        return result
    }


    interface ActionListener : BaseDataLoadingActionListener<CurrencyMarketSummary>


}