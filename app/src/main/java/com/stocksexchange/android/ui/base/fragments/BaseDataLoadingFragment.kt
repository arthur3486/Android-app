package com.stocksexchange.android.ui.base.fragments

import android.graphics.drawable.Drawable
import android.support.annotation.CallSuper
import android.support.v4.widget.SwipeRefreshLayout
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import com.stocksexchange.android.MAIN_VIEW_ANIMATION_DURATION
import com.stocksexchange.android.R
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.ui.base.mvp.presenters.BaseDataLoadingPresenter
import com.stocksexchange.android.ui.base.mvp.views.DataLoadingView
import com.stocksexchange.android.ui.utils.extensions.*
import com.stocksexchange.android.ui.utils.interfaces.CanObserveNetworkStateChanges
import com.stocksexchange.android.ui.views.InfoView
import org.jetbrains.anko.support.v4.ctx
import org.koin.android.ext.android.inject

/**
 * A base fragment class holding the common methods
 * for data loading.
 */
abstract class BaseDataLoadingFragment<P : BaseDataLoadingPresenter<*, *, Data, *>, Data : Any> : BaseFragment<P>(),
    DataLoadingView<Data>, CanObserveNetworkStateChanges {


    @CallSuper
    override fun init() {
        initProgressBar()
        initInfoView()
        initSwipeRefreshProgressBar()
    }


    protected open fun initProgressBar() {
        val progressBar = getProgressBar()

        progressBar.setColor(R.color.colorProgressBar)

        adjustView(progressBar)
        hideProgressBar()
    }


    protected open fun initInfoView() {
        val infoView = getInfoView()

        infoView.setIcon(getInfoViewIcon(inject<InfoViewIconProvider>().value))

        adjustView(infoView)
        hideInfoView()
    }


    /**
     * Gets called with views that need adjustments.
     *
     * @param view The view to adjust
     */
    protected open fun adjustView(view: android.view.View) {
        // Override if needed
    }


    protected open fun initSwipeRefreshProgressBar() {
        val swipeRefreshLayout = getRefreshProgressBar()

        swipeRefreshLayout.setColorSchemeColors(ctx.getCompatColor(R.color.colorAccent))
        swipeRefreshLayout.setOnRefreshListener { onRefreshData() }
    }


    /**
     * Shows the main view holding the loaded data.
     */
    override fun showMainView() {
        val mainView = getMainView()

        mainView.alpha = 0f
        mainView
            .animate()
            .alpha(1f)
            .setInterpolator(LinearInterpolator())
            .setDuration(MAIN_VIEW_ANIMATION_DURATION)
            .start()
    }


    /**
     * Hides the main view holding the loaded data.
     */
    override fun hideMainView() {
        val mainView = getMainView()

        if(mainView.alpha != 0f) {
            mainView.alpha = 0f
        }
    }


    /**
     * Shows the empty view in case there is no data available.
     */
    override fun showEmptyView() {
        val infoView = getInfoView()
        infoView.setCaption(getEmptyViewCaption())

        if(!infoView.isVisible()) {
            infoView.makeVisible()
        }
    }


    /**
     * Shows the error view in case an error occurred while loading data.
     */
    override fun showErrorView() {
        val infoView = getInfoView()
        infoView.setCaption(getErrorViewCaption())

        if(!infoView.isVisible()) {
            infoView.makeVisible()
        }
    }


    /**
     * Hides the info view (i.e., empty and error views).
     */
    override fun hideInfoView() {
        val infoView = getInfoView()

        if(!infoView.isGone()) {
            infoView.makeGone()
        }
    }


    /**
     * Shows the progress bar to notify about the data loading start.
     */
    override fun showProgressBar() {
        val progressBar = getProgressBar()

        if(!progressBar.isVisible()) {
            progressBar.makeVisible()
        }
    }


    /**
     * Hides the progress bar to notify about the data loading end.
     */
    override fun hideProgressBar() {
        val progressBar = getProgressBar()

        if(!progressBar.isGone()) {
            progressBar.makeGone()
        }
    }


    /**
     * Shows the refresh progress bar to notify about the data loading start.
     */
    override fun showRefreshProgressBar() {
        val swipeRefreshLayout = getRefreshProgressBar()

        if(!swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = true
        }
    }


    /**
     * Hides the refresh progress bar to notify about the data loading start.
     */
    override fun hideRefreshProgressBar() {
        val swipeRefreshLayout = getRefreshProgressBar()

        if(swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }


    /**
     * Enables the refresh progress bar.
     */
    override fun enableRefreshProgressBar() {
        getRefreshProgressBar().enable()
    }


    /**
     * Disables the refresh progress bar.
     */
    override fun disableRefreshProgressBar() {
        getRefreshProgressBar().disable()
    }


    /**
     * Checks whether the view is currently selected or not.
     */
    override fun isViewSelected(): Boolean {
        return isSelected()
    }


    /**
     * Should return the drawable for the info view.
     */
    abstract fun getInfoViewIcon(iconProvider: InfoViewIconProvider): Drawable?


    /**
     * Should return the caption for the info view.
     */
    abstract fun getEmptyViewCaption(): String


    /**
     * Override this method to return a more specific error message.
     */
    open fun getErrorViewCaption(): String {
        return getString(R.string.error_something_went_wrong)
    }


    /**
     * Should return a reference to the main view.
     */
    abstract fun getMainView(): android.view.View


    /**
     * Should return a reference to the info view.
     */
    abstract fun getInfoView(): InfoView


    /**
     * Should return a reference to the progress bar.
     */
    abstract fun getProgressBar(): ProgressBar


    /**
     * Should return a reference to the refresh progress bar.
     */
    abstract fun getRefreshProgressBar(): SwipeRefreshLayout


    override fun onNetworkConnected() {
        if(isInitialized()) {
            mPresenter?.onNetworkConnected()
        }
    }


    override fun onNetworkDisconnected() {
        if(isInitialized()) {
            mPresenter?.onNetworkDisconnected()
        }
    }


    override fun onSelected() {
        super.onSelected()

        if(isInitialized()) {
            mPresenter?.onViewSelected()
        }
    }


    /**
     * Gets called whenever a user pulled the refresh progress
     * bar to update the data.
     */
    open fun onRefreshData() {
        mPresenter?.onRefreshData()
    }


}