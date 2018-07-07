package com.stocksexchange.android.ui.utils.interfaces

import com.stocksexchange.android.model.DataLoadingStates

interface DataLoadable<in Data> {

    fun onDataLoadingStarted()

    fun onDataLoadingEnded()

    fun onDataLoadingStateChanged(dataLoadingState: DataLoadingStates)

    fun onDataLoadingSucceeded(data: Data)

    fun onDataLoadingFailed(error: Throwable)

}