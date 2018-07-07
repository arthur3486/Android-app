package com.stocksexchange.android.ui.base.mvp.presenters

import com.stocksexchange.android.datastores.exceptions.NotFoundException
import com.stocksexchange.android.model.DataLoadingStates
import com.stocksexchange.android.model.DataTypes
import com.stocksexchange.android.model.HttpCodes
import com.stocksexchange.android.ui.base.mvp.model.BaseDataLoadingModel
import com.stocksexchange.android.ui.base.mvp.views.DataLoadingView
import com.stocksexchange.android.ui.utils.interfaces.CanObserveNetworkStateChanges
import com.stocksexchange.android.ui.utils.interfaces.DataLoadable
import retrofit2.HttpException

/**
 * A base data loading presenter to build presenters on.
 */
abstract class BaseDataLoadingPresenter<out Model, out View, Data, Parameters>(
    model: Model,
    view: View
) : BasePresenter<Model, View>(model, view),
    DataLoadable<Data>,
    CanObserveNetworkStateChanges where
        Model : BaseDataLoadingModel<Data, Parameters, *>,
        View : DataLoadingView<Data>,
        Data : Any,
        Parameters : Any {


    override fun start() {
        super.start()

        if(mView.isDataSourceEmpty()) {
            mView.hideMainView()
            mView.showEmptyView()
        }

        if((mView.isDataSourceEmpty() || !mModel.mWasLastDataFetchingSuccessful)
                && !mModel.mIsDataLoading
                && mView.isViewSelected()
                && mModel.isDataLoadingIntervalApplied()) {
            reloadData()
        }
    }


    /**
     * Reloads the data by resetting the flags and changing
     * the view state.
     */
    protected open fun reloadData() {
        if(mModel.mIsDataLoading) {
            return
        }

        mModel.resetParameters()

        mView.hideMainView()
        mView.hideInfoView()

        loadData(DataTypes.OLD_DATA, false)
    }


    /**
     * Loads the data by delegating the work to the model.
     */
    protected open fun loadData(dataType: DataTypes, wasInitiatedByTheUser: Boolean) {
        mModel.getData(getDataLoadingParams(), dataType, wasInitiatedByTheUser)
    }


    /**
     * Should return parameters holding data necessary
     * for data loading.
     */
    abstract fun getDataLoadingParams(): Parameters


    override fun onDataLoadingStarted() {
        mView.hideInfoView()

        // Showing the proper progress bar
        if(mView.isDataSourceEmpty() && !mModel.mWasLastDataFetchingInitiatedByTheUser) {
            mView.hideRefreshProgressBar()
            mView.disableRefreshProgressBar()
            mView.showProgressBar()
        } else {
            mView.showRefreshProgressBar()
        }
    }


    override fun onDataLoadingEnded() {
        // Hiding the proper progress bar
        if(mModel.mWasLastDataFetchingInitiatedByTheUser) {
            mView.hideRefreshProgressBar()
        } else {
            mView.hideProgressBar()
            mView.enableRefreshProgressBar()
        }

        // Just to make sure InfoView showing is handled
        if(mView.isDataSourceEmpty()) {
            mView.showEmptyView()
        } else {
            mView.hideInfoView()
        }
    }


    override fun onDataLoadingStateChanged(dataLoadingState: DataLoadingStates) {
        if(dataLoadingState == DataLoadingStates.IDLE) {
            if(!mView.isDataSourceEmpty() && !mModel.mIsDataLoadingCancelled) {
                mView.showMainView()
            }
        }
    }


    override fun onDataLoadingSucceeded(data: Data) {
        mView.addData(data)
    }


    override fun onDataLoadingFailed(error: Throwable) {
        mView.hideInfoView()

        if(mView.isDataSourceEmpty()) {
            if(((error is HttpException) && (error.code() == HttpCodes.TOO_MANY_REQUESTS.code)) || (error is NotFoundException)) {
                mView.showEmptyView()
            } else {
                mView.showErrorView()
            }
        }
    }


    /**
     * Gets called whenever a view becomes visible to the user on the screen.
     */
    open fun onViewSelected() {
        if((mView.isDataSourceEmpty() || !mModel.mWasLastDataFetchingSuccessful)
                && !mModel.mIsDataLoading
                && mModel.isDataLoadingIntervalApplied()) {
            loadData(DataTypes.OLD_DATA, false)
        }
    }


    /**
     * Gets called in case user explicitly specified (e.g., pulled a swipe to refresh
     * progress bar) that he/she wants to refresh data.
     */
    fun onRefreshData() {
        loadData(DataTypes.NEW_DATA, true)
    }


    override fun onNetworkConnected() {
        if((mView.isDataSourceEmpty() || !mModel.mWasLastDataFetchingSuccessful)
                && mView.isViewSelected()
                && !mModel.mIsDataLoading) {
            loadData(DataTypes.NEW_DATA, false)
        }
    }


    override fun onNetworkDisconnected() {
        // Stub
    }


}