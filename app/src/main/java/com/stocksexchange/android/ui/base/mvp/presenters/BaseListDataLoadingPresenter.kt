package com.stocksexchange.android.ui.base.mvp.presenters

import com.stocksexchange.android.model.DataLoadingStates
import com.stocksexchange.android.model.DataTypes
import com.stocksexchange.android.utils.helpers.tag
import com.stocksexchange.android.ui.base.mvp.model.BaseDataLoadingModel
import com.stocksexchange.android.ui.base.mvp.views.ListDataLoadingView
import com.stocksexchange.android.ui.utils.extensions.getWithDefault

/**
 * A base list data loading presenter to build presenters on.
 */
abstract class BaseListDataLoadingPresenter<out Model, out View, Data, Parameters>(
    model: Model,
    view: View
) : BaseDataLoadingPresenter<Model, View, Data, Parameters>(model, view) where
        Model : BaseDataLoadingModel<Data, Parameters, *>,
        View : ListDataLoadingView<Data>,
        Data : Any,
        Parameters : Any {


    companion object {

        private val CLASS = BaseDataLoadingPresenter::class.java

        private val SAVED_STATE_PREVIOUS_DATA_SET_SIZE = tag(CLASS, "previous_data_set_size")

    }


    /**
     * An integer holding the previous data set size. Helpful in
     * determining whether to show the recycler view or not.
     */
    protected var mPreviousDataSetSize: Int = 0




    override fun loadData(dataType: DataTypes, wasInitiatedByTheUser: Boolean) {
        mPreviousDataSetSize = mView.getDataSetSize()

        super.loadData(dataType, wasInitiatedByTheUser)
    }


    override fun onDataLoadingEnded() {
        // Hiding the proper progress bar
        if((mPreviousDataSetSize == 0) && !mModel.mWasLastDataFetchingInitiatedByTheUser) {
            mView.hideProgressBar()
            mView.enableRefreshProgressBar()
        } else {
            mView.hideRefreshProgressBar()
        }

        // Just to make sure InfoView showing is handled
        onDataSetSizeChanged(mView.getDataSetSize())
    }


    override fun onDataLoadingStateChanged(dataLoadingState: DataLoadingStates) {
        if(dataLoadingState == DataLoadingStates.IDLE) {
            if((mPreviousDataSetSize == 0) && !mView.isDataSourceEmpty() && !mModel.mIsDataLoadingCancelled) {
                mView.showMainView()
            }
        }
    }


    /**
     * A helper function that is responsible for showing
     * and hiding the proper views based on the adapter's
     * data set size.
     */
    open fun onDataSetSizeChanged(size: Int) {
        if(size > 0) {
            mView.hideInfoView()
            mView.hideProgressBar()
        } else {
            mView.showEmptyView()
        }
    }


    /**
     * Gets called whenever a search query has been changed.
     *
     * @param query The new query
     */
    fun onSearchQueryChanged(query: String) {
        cancelDataLoading()
        reloadData()
    }


    /**
     * Gets called whenever a user explicitly wants
     * to cancel the data loading.
     */
    fun cancelDataLoading() {
        mModel.cancelDataLoading()
    }


    override fun onRestoreState(savedState: MutableMap<String, Any>) {
        super.onRestoreState(savedState)

        with(savedState) {
            mPreviousDataSetSize = (getWithDefault(SAVED_STATE_PREVIOUS_DATA_SET_SIZE, 0) as Int)
        }

    }


    override fun onSaveState(savedState: MutableMap<String, Any>) {
        super.onSaveState(savedState)

        savedState[SAVED_STATE_PREVIOUS_DATA_SET_SIZE] = mPreviousDataSetSize
    }


}