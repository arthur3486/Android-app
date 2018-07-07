package com.stocksexchange.android.ui.dashboard

import android.support.v4.app.FragmentManager
import com.stocksexchange.android.ui.base.adapters.viewpager.BaseViewPagerAdapter
import com.stocksexchange.android.ui.utils.interfaces.Sortable

/**
 * An adapter used in [DashboardActivity].
 */
class DashboardViewPagerAdapter(
    fragmentManager: FragmentManager
) : BaseViewPagerAdapter(fragmentManager), Sortable {


    override fun sort(payload: Any) {
        mFragmentList.forEach {
            if(it is Sortable) {
                it.sort(payload)
            }
        }
    }


}