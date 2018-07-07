package com.stocksexchange.android.ui.utils

import android.support.design.widget.TabLayout
import com.stocksexchange.android.ui.utils.listeners.adapters.OnTabSelectedListenerAdapter
import com.stocksexchange.android.ui.views.CustomTab

/**
 * A class used for animating tabs of [TabLayout].
 *
 * @see TabLayout
 */
class CustomTabAnimator private constructor (
    private val tabLayout: TabLayout
) {


    companion object {

        /**
         * Returns an instance of the CustomTabAnimator.
         *
         * @param tabLayout The tab layout for the animator
         *
         * @return The new instance of the CustomTabAnimator
         */
        fun newInstance(tabLayout: TabLayout) = CustomTabAnimator(tabLayout)

    }




    init {
        initTabs()
        selectTab(tabLayout.selectedTabPosition)
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListenerAdapter {

            override fun onTabSelected(tab: TabLayout.Tab) {
                selectTab(tab.position)
            }

        })
    }


    private fun initTabs() {
        var customTab: CustomTab

        for (i in 0 until tabLayout.tabCount) {
            customTab = CustomTab(tabLayout.context)
            customTab.minimize(false)

            tabLayout.getTabAt(i)?.customView = customTab
        }
    }


    /**
     * Gets a tab at a specified position.
     *
     * @param tabPosition The position of the tab
     *
     * @return The tab at the specified position or null if there is not tab
     * at the specified position
     *
     * @see CustomTab
     */
    fun getTabAt(tabPosition: Int): CustomTab? {
        return if (tabPosition >= 0 && tabPosition < tabLayout.tabCount) {
            tabLayout.getTabAt(tabPosition)?.customView as CustomTab
        } else {
            null
        }
    }


    /**
     * Selects a tab at a specific position through animation.
     *
     * The new selected tab will be maximized while the old selected tab
     * will be minimized.
     *
     * @param tabPosition The position of the tab
     */
    fun selectTab(tabPosition: Int) {
        if (tabPosition < 0 || tabPosition >= tabLayout.tabCount) {
            return
        }

        var tab: CustomTab

        for (i in 0 until tabLayout.tabCount) {
            tab = tabLayout.getTabAt(i)?.customView as CustomTab

            if (i == tabPosition) {
                if (!tab.isMaximized()) {
                    tab.maximize(true)
                }
            } else {
                if (!tab.isMinimized()) {
                    tab.minimize(true)
                }
            }
        }
    }


}
