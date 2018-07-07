package com.stocksexchange.android.ui.base.activities

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.stocksexchange.android.R
import com.stocksexchange.android.TAB_TOGGLING_DELAY
import com.stocksexchange.android.ui.base.adapters.viewpager.BaseViewPagerAdapter
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.base.mvp.presenters.BaseViewPagerPresenter
import com.stocksexchange.android.ui.base.mvp.views.ViewPagerView
import com.stocksexchange.android.ui.utils.CustomTabAnimator
import com.stocksexchange.android.ui.utils.extensions.setColor
import com.stocksexchange.android.ui.utils.interfaces.Scrollable
import com.stocksexchange.android.ui.utils.listeners.adapters.OnTabSelectedListenerAdapter
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*

/**
 * A base activity to extend from if the activity hosts the
 * [ViewPager].
 */
abstract class BaseViewPagerActivity<VPA : BaseViewPagerAdapter, P : BaseViewPagerPresenter<*, *>> : BaseActivity<P>(),
        ViewPagerView {


    companion object {

        private const val SAVED_STATE_SELECTED_TAB = "selected_tab"

    }


    /**
     * An integer representing the currently selected tab position.
     */
    protected open var mSelectedTabPosition: Int = 0

    /**
     * An adapter to be used with a view pager.
     */
    protected lateinit var mAdapter: VPA

    /**
     * A custom wrapper around [TabLayout] used for animating tabs.
     */
    protected lateinit var mTabAnimator: CustomTabAnimator




    override fun init() {
        initToolbar()
        initViewPager()
        initTabLayout()

        onTabSelected(getTabLayout().getTabAt(mSelectedTabPosition)!!, true)
    }


    protected open fun initToolbar() {
        toolbar.returnBackBtnIv.setOnClickListener { onBackPressed() }
        toolbar.titleTv.text = getToolbarTitle()
        toolbar.progressBar.setColor(R.color.colorProgressBar)
    }


    protected open fun initViewPager() {
        val viewPager = getViewPager()

        mAdapter = getViewPagerAdapter()
        mAdapter.mViewPagerId = viewPager.id

        populateAdapter()

        viewPager.adapter = mAdapter
        viewPager.offscreenPageLimit = mAdapter.count
    }


    /**
     * Should perform adapter population.
     */
    abstract fun populateAdapter()


    protected open fun initTabLayout() {
        val tabLayout = getTabLayout()

        tabLayout.setupWithViewPager(getViewPager())
        tabLayout.getTabAt(mSelectedTabPosition)?.select()
        tabLayout.addOnTabSelectedListener(onTabSelectedListener)

        mTabAnimator = CustomTabAnimator.newInstance(tabLayout)
        initTabLayoutTabs()
    }


    /**
     * Should perform [TabLayout] tabs initialization here.
     */
    abstract fun initTabLayoutTabs()


    /**
     * Selects the fragment at a particular position (i.e., calls
     * [BaseFragment.onSelected] method).
     *
     * @param position The fragment's position
     * @param shouldDelay Whether to delay the selection or not
     */
    override fun selectFragmentAt(position: Int, shouldDelay: Boolean) {
        getTabLayout().postDelayed(
            { mAdapter.getFragment(position)?.onSelected() },
            (if(shouldDelay) TAB_TOGGLING_DELAY else 0L)
        )
    }


    /**
     * Unselects the fragment at a particular position (i.e., calls
     * [BaseFragment.onSelected] method).
     *
     * @param position The fragment's position
     * @param shouldDelay Whether to delay the unselection or not
     */
    override fun unselectFragmentAt(position: Int, shouldDelay: Boolean) {
        getTabLayout().postDelayed(
            { mAdapter.getFragment(position)?.onUnselected() },
            (if(shouldDelay) TAB_TOGGLING_DELAY else 0L)
        )
    }


    /**
     * Scrolls the fragment to top if it is an instance of
     * [Scrollable] interface.
     *
     * @param position The position of fragment inside the
     * adapter to scroll
     */
    override fun scrollFragmentToTop(position: Int) {
        val fragment = mAdapter.getFragment(position)

        if(fragment is Scrollable) {
            fragment.scrollToTop()
        }
    }


    override fun canObserveNetworkStateChanges(): Boolean {
        return true
    }


    /**
     * Should return the title for the toolbar.
     */
    abstract fun getToolbarTitle(): String


    /**
     * Should return a reference to the ViewPager.
     */
    abstract fun getViewPager(): ViewPager


    /**
     * Should return an instance of [VPA] adapter.
     */
    abstract fun getViewPagerAdapter(): VPA


    /**
     * Should return a reference to the TabLayout.
     */
    abstract fun getTabLayout(): TabLayout


    override fun onConnected() {
        if(isInitialized()) {
            mAdapter.onNetworkConnected()
        }
    }


    override fun onDisconnected() {
        if(isInitialized()) {
            mAdapter.onNetworkDisconnected()
        }
    }


    protected open fun onTabSelected(tab: TabLayout.Tab, firstTime: Boolean) {
        mPresenter?.onTabSelected(tab.position, firstTime)
    }


    protected open fun onTabUnselected(tab: TabLayout.Tab) {
        mPresenter?.onTabUnselected(tab.position)
    }


    protected open fun onTabReselected(tab: TabLayout.Tab) {
        mPresenter?.onTabReselected(tab.position)
    }


    override fun onBackPressed() {
        super.onBackPressed()

        if(isInitialized()) {
            mAdapter.onBackPressed()
        }
    }


    private val onTabSelectedListener: OnTabSelectedListenerAdapter = object : OnTabSelectedListenerAdapter {

        override fun onTabSelected(tab: TabLayout.Tab) {
            this@BaseViewPagerActivity.onTabSelected(tab, false)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            this@BaseViewPagerActivity.onTabUnselected(tab)
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            this@BaseViewPagerActivity.onTabReselected(tab)
        }

    }


    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        savedState?.apply {
            mSelectedTabPosition = getInt(SAVED_STATE_SELECTED_TAB)
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putInt(SAVED_STATE_SELECTED_TAB, getTabLayout().selectedTabPosition)
    }


}