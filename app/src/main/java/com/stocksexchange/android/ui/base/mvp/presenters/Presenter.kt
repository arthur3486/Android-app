package com.stocksexchange.android.ui.base.mvp.presenters

/**
 * A base interface for defining a presenter.
 */
interface Presenter {

    fun start()

    fun stop()

    fun onRestoreState(savedState: MutableMap<String, Any>)

    fun onSaveState(savedState: MutableMap<String, Any>)

}