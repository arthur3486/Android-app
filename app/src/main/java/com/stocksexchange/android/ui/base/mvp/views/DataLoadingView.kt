package com.stocksexchange.android.ui.base.mvp.views

/**
 * A base data loading view to build views on.
 */
interface DataLoadingView<in Data> {

    fun showMainView()

    fun hideMainView()

    fun showEmptyView()

    fun showErrorView()

    fun hideInfoView()

    fun showProgressBar()

    fun hideProgressBar()

    fun showRefreshProgressBar()

    fun hideRefreshProgressBar()

    fun enableRefreshProgressBar()

    fun disableRefreshProgressBar()

    fun addData(data: Data)

    fun isDataSourceEmpty(): Boolean

    fun isViewSelected(): Boolean

}