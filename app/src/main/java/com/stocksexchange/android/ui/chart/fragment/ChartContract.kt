package com.stocksexchange.android.ui.chart.fragment

import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.api.model.ChartDataIntervals
import com.stocksexchange.android.model.ChartParameters
import com.stocksexchange.android.ui.base.mvp.views.DataLoadingView

interface ChartContract {


    interface View : DataLoadingView<ChartData> {

        fun updateChartInterval(interval: ChartDataIntervals)

        fun clearChartData()

        fun getChartParameters(): ChartParameters

    }


    interface ActionListener {

        fun onChartIntervalPicked(interval: ChartDataIntervals)

    }


}