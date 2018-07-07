package com.stocksexchange.android.ui.base.mvp.views

/**
 * A base view pager view to build views on.
 */
interface ViewPagerView {

    fun selectFragmentAt(position: Int, shouldDelay: Boolean)

    fun unselectFragmentAt(position: Int, shouldDelay: Boolean)

    fun scrollFragmentToTop(position: Int)

}