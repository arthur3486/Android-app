package com.stocksexchange.android.ui.base.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.animation.LinearInterpolator
import com.stocksexchange.android.MAIN_VIEW_ANIMATION_DURATION
import com.stocksexchange.android.R
import com.stocksexchange.android.cache.ObjectCache
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseRecyclerViewAdapter
import com.stocksexchange.android.ui.base.mvp.presenters.BaseListDataLoadingPresenter
import com.stocksexchange.android.ui.base.mvp.views.ListDataLoadingView
import com.stocksexchange.android.ui.utils.VerticalSpacingItemDecorator
import com.stocksexchange.android.ui.utils.extensions.disableAnimations
import com.stocksexchange.android.ui.utils.interfaces.Searchable
import com.stocksexchange.android.ui.utils.listeners.OnDataSetChangeListener
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.dimen

/**
 * A base fragment class holding the common methods
 * to load list data.
 */
abstract class BaseListDataLoadingFragment<PR, DA, IM, AD> : BaseDataLoadingFragment<PR, DA>(),
    ListDataLoadingView<DA>,
    Searchable where
        PR : BaseListDataLoadingPresenter<*, *, DA, *>,
        DA : Any,
        IM : Any,
        AD : BaseRecyclerViewAdapter<IM, *, *> {


    companion object {

        private const val SAVED_STATE_ITEMS = "items"

    }


    /**
     * Items that this fragment's adapter contains.
     */
    protected var mItems: MutableList<IM> = mutableListOf()

    /**
     * An adapter used by the recycler view.
     */
    protected lateinit var mAdapter: AD




    override fun init() {
        super.init()

        initRecyclerView()
    }


    /**
     * Initializes the recycler view returned by the [getMainView] method.
     */
    open fun initRecyclerView() {
        val recyclerView = (getMainView() as RecyclerView)

        if(shouldDisableRVAnimations()) {
            recyclerView.disableAnimations()
        }

        recyclerView.layoutManager = LinearLayoutManager(ctx)
        recyclerView.addItemDecoration(VerticalSpacingItemDecorator(
            dimen(R.dimen.recycler_view_divider_size),
            dimen(R.dimen.card_view_elevation)
        ))

        initAdapter()
        mAdapter.mOnDataSetChangeListener = onDataSetChangeListener

        recyclerView.adapter = mAdapter

        adjustView(recyclerView)
    }


    /**
     * Should initialize an adapter for the RecyclerView.
     */
    abstract fun initAdapter()


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


    override fun hideMainView() {
        mAdapter.clear()
    }


    /**
     * A helper function to scroll RecyclerView's to the first position.
     */
    override fun scrollToTop() {
        (getMainView() as RecyclerView).scrollToPosition(0)
    }


    /**
     * Should change a current search query.
     */
    override fun setSearchQuery(query: String) {
        // Allow children to override this method
    }

    /**
     * Checks whether the RecyclerView's adapter is empty.
     */
    override fun isDataSourceEmpty(): Boolean {
        return (getDataSetSize() == 0)
    }


    /**
     * Override this method if you want to enable animations
     * for the RecyclerView. By default, they are disabled.
     */
    open fun shouldDisableRVAnimations(): Boolean {
        return true
    }


    /**
     * Returns the size of the RecyclerView's adapter.
     */
    override fun getDataSetSize(): Int {
        return if(::mAdapter.isInitialized) mAdapter.getItemCount() else 0
    }


    /**
     * Should return a current search query.
     */
    override fun getSearchQuery(): String {
        // Allow children to override this method
        return ""
    }


    override fun onPerformSearch(query: String) {
        if(query.isBlank() || (query == getSearchQuery())) {
            return
        }

        setSearchQuery(query)

        if(!isInitialized()) {
            return
        }

        mPresenter?.onSearchQueryChanged(query)
    }


    override fun onCancelSearch() {
        mPresenter?.cancelDataLoading()
    }


    private val onDataSetChangeListener: OnDataSetChangeListener<MutableList<IM>, IM> =
        object : OnDataSetChangeListener<MutableList<IM>, IM> {

            override fun onItemAdded(dataSet: MutableList<IM>, item: IM) {
                mPresenter?.onDataSetSizeChanged(dataSet.size)
            }

            override fun onItemDeleted(dataSet: MutableList<IM>, item: IM) {
                mPresenter?.onDataSetSizeChanged(dataSet.size)
            }

            override fun onDataSetReplaced(newDataSet: MutableList<IM>) {
                mPresenter?.onDataSetSizeChanged(newDataSet.size)
            }

            override fun onDataSetCleared(dataSet: MutableList<IM>) {
                mPresenter?.onDataSetSizeChanged(dataSet.size)
            }

        }


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        mItems = (ObjectCache.remove("${mPresenter!!}_$SAVED_STATE_ITEMS", mutableListOf<IM>()) as MutableList<IM>)
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        ObjectCache.put("${mPresenter!!}_$SAVED_STATE_ITEMS", mItems)
    }


    override fun onRecycle(isChangingConfigurations: Boolean) {
        super.onRecycle(isChangingConfigurations)

        if(!isChangingConfigurations) {
            ObjectCache.remove("${mPresenter!!}_$SAVED_STATE_ITEMS")
        }
    }


}