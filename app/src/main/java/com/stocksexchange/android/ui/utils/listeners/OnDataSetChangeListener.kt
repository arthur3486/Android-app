package com.stocksexchange.android.ui.utils.listeners

/**
 * Implement this listener to get notified about data set changes
 * (insertion, update, deletion, and so on).
 */
interface OnDataSetChangeListener<in DS : Collection<IT>, IT> {


    fun onItemAdded(dataSet: DS, item: IT) {
        // Stub
    }


    fun onItemUpdated(dataSet: DS, item: IT) {
        // Stub
    }


    fun onItemReplaced(dataSet: DS, oldItem: IT, newItem: IT) {
        // Stub
    }


    fun onItemDeleted(dataSet: DS, item: IT) {
        // Stub
    }


    fun onDataSetReplaced(newDataSet: DS) {
        // Stub
    }


    fun onDataSetCleared(dataSet: DS) {
        // Stub
    }


}