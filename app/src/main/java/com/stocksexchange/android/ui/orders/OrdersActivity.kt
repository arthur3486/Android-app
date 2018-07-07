package com.stocksexchange.android.ui.orders

import android.content.Context
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.stocksexchange.android.ORDERS_ACTIVITY_TAB_COUNT
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.OrderTypes
import com.stocksexchange.android.ui.base.activities.BaseViewPagerActivity
import com.stocksexchange.android.ui.orders.fragment.OrdersFragment
import com.stocksexchange.android.ui.orders.search.OrdersSearchActivity
import com.stocksexchange.android.ui.utils.extensions.makeGone
import com.stocksexchange.android.ui.utils.extensions.makeVisible
import kotlinx.android.synthetic.main.orders_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.jetbrains.anko.intentFor

class OrdersActivity : BaseViewPagerActivity<OrdersViewPagerAdapter, OrdersActivityPresenter>(),
    OrdersActivityContract.View {


    companion object {

        /**
         * Don't forget to update [ORDERS_ACTIVITY_TAB_COUNT]
         * constant in case tabs are added or removed.
         */
        private const val TAB_ACTIVE_ORDERS = 0
        private const val TAB_COMPLETED_ORDERS = 1
        private const val TAB_CANCELLED_ORDERS = 2


        fun newInstance(context: Context): Intent {
            return context.intentFor<OrdersActivity>()
        }

    }




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.horizontal_sliding_right_to_left_enter_animation,
            R.anim.default_window_exit_animation
        )
    }


    override fun initPresenter(): OrdersActivityPresenter = OrdersActivityPresenter(this)


    override fun initToolbar() {
        super.initToolbar()

        toolbar.actionBtnIv.setImageResource(R.drawable.ic_search)
        toolbar.actionBtnIv.setOnClickListener { mPresenter?.onActionButtonClicked() }
        toolbar.actionBtnIv.makeVisible()
    }


    override fun populateAdapter() {
        val fragment = mAdapter.getFragment(TAB_ACTIVE_ORDERS)

        val activeOrdersFragment = if(fragment == null) {
            OrdersFragment.newActiveInstance()
        } else {
            (fragment as OrdersFragment)
        }

        activeOrdersFragment.mProgressBarListener = progressBarListener

        mAdapter.addFragment(activeOrdersFragment)
        mAdapter.addFragment(mAdapter.getFragment(TAB_COMPLETED_ORDERS) ?: OrdersFragment.newCompletedInstance())
        mAdapter.addFragment(mAdapter.getFragment(TAB_CANCELLED_ORDERS) ?: OrdersFragment.newCancelledInstance())
    }


    override fun initTabLayoutTabs() {
        mTabAnimator.getTabAt(TAB_ACTIVE_ORDERS)?.setTitle(getString(R.string.orders_activity_tab_active_orders_title))
        mTabAnimator.getTabAt(TAB_COMPLETED_ORDERS)?.setTitle(getString(R.string.orders_activity_tab_completed_orders_title))
        mTabAnimator.getTabAt(TAB_CANCELLED_ORDERS)?.setTitle(getString(R.string.orders_activity_tab_cancelled_orders_title))
    }


    override fun launchSearchActivity() {
        startActivity(OrdersSearchActivity.newInstance(
            when(tabLayout.selectedTabPosition) {
                TAB_ACTIVE_ORDERS -> OrderTypes.ACTIVE
                TAB_COMPLETED_ORDERS -> OrderTypes.COMPLETED
                else -> OrderTypes.CANCELLED
            },
            this
        ))
    }


    override fun getToolbarTitle(): String = getString(R.string.orders)


    override fun getViewPager(): ViewPager = viewPager


    override fun getViewPagerAdapter(): OrdersViewPagerAdapter {
        return OrdersViewPagerAdapter(supportFragmentManager)
    }


    override fun getTabLayout(): TabLayout = tabLayout


    override fun getContentLayoutResourceId(): Int = R.layout.orders_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.default_window_enter_animation,
            R.anim.horizontal_sliding_right_to_left_exit_animation
        )
    }


    private val progressBarListener = object : OrdersFragment.ProgressBarListener {

        override fun showProgressBar() {
            toolbar.actionBtnIv.makeGone()
            toolbar.progressBar.makeVisible()
        }

        override fun hideProgressBar() {
            toolbar.progressBar.makeGone()
            toolbar.actionBtnIv.makeVisible()
        }

    }


}