package com.stocksexchange.android.ui.base.mvp.presenters

import com.stocksexchange.android.ui.base.mvp.model.Model
import com.stocksexchange.android.ui.base.mvp.views.ViewPagerView

/**
 * A base view pager presenter to build presenters on.
 */
abstract class BaseViewPagerPresenter<out M, out V>(
    model: M,
    view: V
) : BasePresenter<M, V>(model, view) where
        M : Model,
        V : ViewPagerView {


    /**
     * Gets called whenever a tab has been selected.
     *
     * @param position The position of the selected tab
     * @param firstTime Whether the selection has happened
     * the first time or not
     */
    open fun onTabSelected(position: Int, firstTime: Boolean) {
        mView.selectFragmentAt(position, !firstTime)
    }


    /**
     * Gets called whenever a tab has been unselected.
     *
     * @param position The position of the unselected tab
     */
    open fun onTabUnselected(position: Int) {
        mView.unselectFragmentAt(position, true)
    }


    /**
     * Gets called whenever a tab has been reselected.
     *
     * @param position The position of the reselected tab
     */
    open fun onTabReselected(position: Int) {
        mView.scrollFragmentToTop(position)
    }


}