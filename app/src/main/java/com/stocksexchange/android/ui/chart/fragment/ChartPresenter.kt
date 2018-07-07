package com.stocksexchange.android.ui.chart.fragment

import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.api.model.ChartDataIntervals
import com.stocksexchange.android.model.ChartParameters
import com.stocksexchange.android.ui.base.mvp.presenters.BaseDataLoadingPresenter

class ChartPresenter(
    model: ChartModel,
    view: ChartContract.View
) : BaseDataLoadingPresenter<ChartModel, ChartContract.View, ChartData, ChartParameters>(model, view),
    ChartContract.ActionListener, ChartModel.ActionListener {


    init {
        model.setActionListener(this)
    }


    constructor(view: ChartContract.View): this(ChartModel(), view)


    override fun getDataLoadingParams(): ChartParameters = mView.getChartParameters()


    override fun onViewSelected() {
        if((mView.isDataSourceEmpty() || !mModel.mWasLastDataFetchingSuccessful)
                && !mModel.mIsDataLoading
                && mModel.isDataLoadingIntervalApplied()) {
            reloadData()
        }
    }


    override fun onChartIntervalPicked(interval: ChartDataIntervals) {
        if(mView.getChartParameters().interval == interval) {
            return
        }

        mView.updateChartInterval(interval)
        mView.clearChartData()

        mModel.cancelDataLoading()
        reloadData()
    }


    override fun toString(): String {
        val params = mView.getChartParameters()
        val mode = params.mode.name
        val marketName = params.marketName

        return "${super.toString()}_${mode}_$marketName"
    }


}