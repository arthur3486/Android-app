package com.stocksexchange.android.ui.base.adapters.viewpager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.utils.interfaces.CanObserveNetworkStateChanges
import com.stocksexchange.android.ui.utils.listeners.OnBackPressListener

/**
 * An enhanced view pager adapter to display a list of fragments.
 */
abstract class BaseViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm),
    CanObserveNetworkStateChanges, OnBackPressListener {


    /**
     * The view pager id that this adapter is associated with.
     * Used for calculating tags associated with fragments.
     */
    var mViewPagerId: Int = -1

    /**
     * The fragment manager this adapter interacts with.
     */
    protected val mFragmentManager: FragmentManager = fm

    /**
     * The list of fragments this adapter contains.
     */
    protected val mFragmentList: MutableList<BaseFragment<*>> = mutableListOf()




    /**
     * Adds a fragment to this adapter.
     *
     * @param fragment The fragment to be added
     */
    fun addFragment(fragment: BaseFragment<*>) {
        mFragmentList.add(fragment)
    }


    /**
     * Obtains a tag associated with a fragment position.
     *
     * @param fragmentPosition The position of a specific fragment within this adapter
     * @return Unique tag for the fragment position
     */
    fun getTagForFragmentPosition(fragmentPosition: Int): String {
        return "android:switcher:$mViewPagerId:$fragmentPosition"
    }


    /**
     * Attempts to find and return a fragment associated with a tag.
     *
     * @param tag The tag associated with the fragment
     * @return The fragment associated with the tag or null if the tag is not associated
     * with any fragment
     */
    fun getFragmentForTag(tag: String): BaseFragment<*>? {
        val fragment = mFragmentManager.findFragmentByTag(tag)
        return fragment?.takeIf { it is BaseFragment<*> }?.let { it as BaseFragment<*> }
    }


    /**
     * Tries to find and return a fragment associated with a position.
     *
     * @param position The position of the fragment within this adapter
     * @return The fragment associated with the position or null if the position
     * is not associated with any fragment
     *
     * @see getFragmentForTag
     * @see getTagForFragmentPosition
     */
    fun getFragment(position: Int): BaseFragment<*>? {
        return getFragmentForTag(getTagForFragmentPosition(position))
    }


    override fun getCount() = mFragmentList.size


    override fun getItem(position: Int): Fragment? {
        return position.takeIf { it in 0..(count - 1) }.let { mFragmentList[position] }
    }


    override fun onNetworkConnected() {
        mFragmentList.forEach {
            if(it is CanObserveNetworkStateChanges) {
                it.onNetworkConnected()
            }
        }
    }


    override fun onNetworkDisconnected() {
        mFragmentList.forEach {
            if(it is CanObserveNetworkStateChanges) {
                it.onNetworkDisconnected()
            }
        }
    }


    override fun onBackPressed() {
        mFragmentList.forEach {
            it.onBackPressed()
        }
    }


}