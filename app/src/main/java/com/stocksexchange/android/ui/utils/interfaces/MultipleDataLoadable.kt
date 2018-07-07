package com.stocksexchange.android.ui.utils.interfaces

import com.stocksexchange.android.model.DataLoadingStates

interface MultipleDataLoadable {

    fun onDataLoadingStarted(dataSource: Int)

    fun onDataLoadingEnded(dataSource: Int)

    fun onDataLoadingStateChanged(dataSource: Int, dataLoadingState: DataLoadingStates)

    fun onDataLoadingSucceeded(dataSource: Int, data: Any)

    fun onDataLoadingFailed(dataSource: Int, error: Throwable)

}