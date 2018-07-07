package com.stocksexchange.android.ui.orders.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.OrderTypes
import com.stocksexchange.android.ui.base.activities.BaseSearchActivity
import com.stocksexchange.android.ui.orders.fragment.OrdersFragment
import com.stocksexchange.android.ui.utils.extensions.makeGone
import com.stocksexchange.android.ui.utils.extensions.makeVisible
import kotlinx.android.synthetic.main.search_toolbar_layout.*
import kotlinx.android.synthetic.main.search_toolbar_layout.view.*
import org.jetbrains.anko.intentFor

class OrdersSearchActivity : BaseSearchActivity<OrdersFragment, OrdersSearchPresenter>(),
    OrdersSearchContract.View {


    companion object {

        private const val EXTRA_ORDER_TYPE = "order_type"

        private const val SAVED_STATE_ORDER_TYPE = "order_type"


        fun newInstance(orderType: OrderTypes, context: Context): Intent {
            return context.intentFor<OrdersSearchActivity>(
                EXTRA_ORDER_TYPE to orderType
            )
        }

    }


    /**
     * An order type to instantiate [OrderTypes] with.
     */
    private lateinit var mOrderType: OrderTypes




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.kitkat_scaling_window_b_enter_animation,
            R.anim.kitkat_scaling_window_a_exit_animation
        )
    }


    override fun initPresenter(): OrdersSearchPresenter = OrdersSearchPresenter(this)


    override fun performSearch(query: String) {
        mFragment.onPerformSearch(query)
    }


    override fun cancelSearch() {
        mFragment.onCancelSearch()
    }


    override fun getInputType(): Int {
        return (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS)
    }


    override fun getInputHint(): String {
        return getString(
            R.string.orders_search_activity_hint_template,
            getString(when(mOrderType) {
                OrderTypes.ACTIVE -> R.string.active
                OrderTypes.COMPLETED -> R.string.completed_order_type
                OrderTypes.CANCELLED -> R.string.cancelled_order_type
                OrderTypes.PUBLIC -> R.string.public_order_type
            })
        )
    }


    override fun getActivityFragment(): OrdersFragment {
        val fragment = OrdersFragment.newSearchInstance(mOrderType)
        fragment.mProgressBarListener = progressBarListener

        return fragment
    }


    override fun getContentLayoutResourceId(): Int = R.layout.orders_search_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.kitkat_scaling_window_a_enter_animation,
            R.anim.kitkat_scaling_window_b_exit_animation
        )
    }


    private val progressBarListener = object : OrdersFragment.ProgressBarListener {

        override fun showProgressBar() {
            toolbar.clearInputBtnIv.makeGone()
            toolbar.progressBar.makeVisible()
        }

        override fun hideProgressBar() {
            toolbar.progressBar.makeGone()
            toolbar.clearInputBtnIv.makeVisible()
        }

    }


    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        if(savedState != null) {
            mOrderType = (savedState.getSerializable(SAVED_STATE_ORDER_TYPE) as OrderTypes)
        } else {
            mOrderType = (intent.getSerializableExtra(SAVED_STATE_ORDER_TYPE) as? OrderTypes) ?: OrderTypes.ACTIVE
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putSerializable(SAVED_STATE_ORDER_TYPE, mOrderType)
    }


}