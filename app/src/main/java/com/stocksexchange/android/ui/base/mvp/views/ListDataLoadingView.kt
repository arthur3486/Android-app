package com.stocksexchange.android.ui.base.mvp.views

import com.stocksexchange.android.ui.utils.interfaces.CanObserveNetworkStateChanges
import com.stocksexchange.android.ui.utils.interfaces.Scrollable

/**
 * A base list data loading view to build views on.
 */
interface ListDataLoadingView<in Data> : DataLoadingView<Data>, Scrollable,
    CanObserveNetworkStateChanges {

    fun setSearchQuery(query: String)

    fun getDataSetSize(): Int

    fun getSearchQuery(): String

}