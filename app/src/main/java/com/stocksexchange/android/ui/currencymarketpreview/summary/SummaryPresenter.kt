package com.stocksexchange.android.ui.currencymarketpreview.summary

import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.model.SummaryParameters
import com.stocksexchange.android.ui.base.mvp.presenters.BaseDataLoadingPresenter

class SummaryPresenter(
    model: SummaryModel,
    view: SummaryContract.View
) : BaseDataLoadingPresenter<SummaryModel, SummaryContract.View, CurrencyMarketSummary, SummaryParameters>(model, view),
    SummaryContract.ActionListener, SummaryModel.ActionListener {


    init {
        model.setActionListener(this)
    }


    constructor(view: SummaryContract.View): this(SummaryModel(), view)


    override fun getDataLoadingParams(): SummaryParameters {
        return SummaryParameters(mView.getCurrencyMarket())
    }


    override fun onViewSelected() {
        if((mView.isDataSourceEmpty() || !mModel.mWasLastDataFetchingSuccessful)
                && !mModel.mIsDataLoading
                && mModel.isDataLoadingIntervalApplied()) {
            reloadData()
        }
    }


    override fun toString(): String {
        return "${super.toString()}_${mView.getCurrencyMarket().name}"
    }


}