package com.stocksexchange.android.ui.chart.fragment

import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.model.ChartParameters
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.repositories.chartsdata.ChartsDataRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseDataLoadingModel
import com.stocksexchange.android.ui.chart.fragment.ChartModel.ActionListener
import org.koin.standalone.inject
import timber.log.Timber

class ChartModel : BaseDataLoadingModel<
    ChartData,
    ChartParameters,
    ActionListener
>() {


    private val mChartsDataRepository: ChartsDataRepository by inject()




    override fun refreshData() {
        mChartsDataRepository.refresh()
    }


    override suspend fun getRepositoryResult(params: ChartParameters): RepositoryResult<ChartData> {
        val result = mChartsDataRepository.get(params)

        Timber.i("chartsDataRepository.get(params: $params) = $result")

        return result
    }


    interface ActionListener : BaseDataLoadingActionListener<ChartData>


}