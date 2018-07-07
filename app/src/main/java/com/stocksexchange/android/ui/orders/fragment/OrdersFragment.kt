package com.stocksexchange.android.ui.orders.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.util.DiffUtil
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.*
import com.stocksexchange.android.cache.ObjectCache
import com.stocksexchange.android.database.mappings.mapToNameMarketMap
import com.stocksexchange.android.model.OrderModes
import com.stocksexchange.android.model.OrderParameters
import com.stocksexchange.android.model.SortTypes
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.ui.base.fragments.BaseListDataLoadingFragment
import com.stocksexchange.android.ui.currencymarketpreview.CurrencyMarketPreviewActivity
import com.stocksexchange.android.ui.utils.diffcallbacks.OrdersDiffCallback
import com.stocksexchange.android.ui.utils.extensions.setPaddingBottom
import com.stocksexchange.android.ui.utils.extensions.setPaddingTop
import com.stocksexchange.android.ui.views.InfoView
import kotlinx.android.synthetic.main.orders_fragment_layout.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.dimen
import org.koin.android.ext.android.get

class OrdersFragment : BaseListDataLoadingFragment<
    OrdersPresenter,
    List<Order>,
    Order,
    OrdersRecyclerViewAdapter
>(), OrdersContract.View {


    companion object {

        private const val SAVED_STATE_ORDER_PARAMETERS = "order_parameters"
        private const val SAVED_STATE_CURRENCY_MARKETS_MAP = "currency_markets_map"


        fun newActiveInstance() = newInstance("ALL", OrderTypes.ACTIVE, OrderModes.STANDARD)

        fun newCompletedInstance() = newInstance("ALL", OrderTypes.COMPLETED, OrderModes.STANDARD)

        fun newCancelledInstance() = newInstance("ALL", OrderTypes.CANCELLED, OrderModes.STANDARD)

        fun newPublicInstance(marketName: String) = newInstance(marketName, OrderTypes.PUBLIC, OrderModes.STANDARD)

        fun newSearchInstance(type: OrderTypes): OrdersFragment = newInstance("ALL", type, OrderModes.SEARCH)

        /**
         * Creates a new fragment with a particular type and returns it.
         *
         * @param type The order type
         *
         * @return The new instance of this fragment
         */
        fun newInstance(marketName: String, type: OrderTypes, mode: OrderModes): OrdersFragment {
            val fragment = OrdersFragment()

            fragment.orderParameters = fragment.orderParameters.copy(
                marketName = marketName,
                mode = mode,
                type = type
            )

            return fragment
        }

    }


    interface ProgressBarListener {

        fun showProgressBar()

        fun hideProgressBar()

    }


    /**
     * A listener used for interacting with a progress bar.
     */
    var mProgressBarListener: ProgressBarListener? = null

    /**
     * Parameters for orders data loading.
     */
    private var orderParameters: OrderParameters = OrderParameters(
        "ALL", "", OrderModes.STANDARD, OrderTypes.ACTIVE,
        OrderTradeTypes.ALL, OrderOwnerTypes.OWN, SortTypes.DESC, 100
    )

    /**
     * A map with keys equal to markets' names and values equal to
     * markets themselves.
     */
    private var currencyMarketsMap: Map<String, CurrencyMarket> = mapOf()




    override fun initPresenter(): OrdersPresenter = OrdersPresenter(this)


    override fun initAdapter() {
        mAdapter = OrdersRecyclerViewAdapter(ctx, mItems)
        mAdapter.setOrderType(orderParameters.type)
        mAdapter.mOnMarketNameClickListener = { _, _, market, _ -> mPresenter?.onMarketNameClicked(market) }
        mAdapter.mOnCancelBtnClickListener = { _, order, _ -> mPresenter?.onCancelButtonClicked(order) }

        if(orderParameters.type != OrderTypes.PUBLIC) {
            if(currencyMarketsMap.isEmpty()) {
                launch(UI) {
                    val result = get<CurrencyMarketsRepository>().getAll()

                    if(result.isSuccessful()) {
                        currencyMarketsMap = result.getSuccessfulResult().value.mapToNameMarketMap()
                        mAdapter.setCurrencyMarkets(currencyMarketsMap)
                    }
                }
            } else {
                mAdapter.setCurrencyMarkets(currencyMarketsMap)
            }
        }
    }


    override fun adjustView(view: View) {
        val isPublicOrders = (orderParameters.type == OrderTypes.PUBLIC)
        val canCenterView = (isPublicOrders || (orderParameters.mode == OrderModes.SEARCH))
        val layoutParams: FrameLayout.LayoutParams

        when(view.id) {

            R.id.progressBar -> {
                layoutParams = (view.layoutParams as FrameLayout.LayoutParams)

                if(canCenterView) {
                    layoutParams.gravity = Gravity.CENTER
                    layoutParams.setMargins(0, 0, 0, 0)
                } else {
                    layoutParams.setMargins(0, dimen(R.dimen.fragment_progress_bar_margin_top), 0, 0)
                }

                view.layoutParams = layoutParams
            }

            R.id.infoView -> {
                layoutParams = (view.layoutParams as FrameLayout.LayoutParams)

                if(canCenterView) {
                    layoutParams.gravity = Gravity.CENTER
                }

                view.layoutParams = layoutParams

                if(isPublicOrders) {
                    view.setPaddingTop(dimen(R.dimen.currency_market_preview_fragment_info_view_padding_top))
                    view.setPaddingBottom(dimen(R.dimen.currency_market_preview_fragment_info_view_padding_bottom))
                }
            }

        }
    }


    override fun showSecondaryProgressBar() {
        mProgressBarListener?.showProgressBar()
    }


    override fun hideSecondaryProgressBar() {
        mProgressBarListener?.hideProgressBar()
    }


    override fun addData(data: List<Order>) {
        val mutableData = data.toMutableList()

        if(isDataSourceEmpty()) {
            mAdapter.setItems(mutableData)
        } else {
            val result = DiffUtil.calculateDiff(OrdersDiffCallback(mItems, mutableData))
            mAdapter.setItems(mutableData, false)
            result.dispatchUpdatesTo(mAdapter)
        }
    }


    override fun addOrderChronologically(order: Order) {
        mAdapter.addItem(
            order,
                mAdapter.getChronologicalPositionForOrder(order, orderParameters.sortType)
        )
    }


    override fun setSearchQuery(query: String) {
        orderParameters = orderParameters.copy(searchQuery = query)
    }


    override fun containsOrder(order: Order): Boolean {
        return mAdapter.containsOrder(order)
    }


    override fun deleteOrder(orderId: Long) {
        mAdapter.deleteOrder(orderId)
    }


    override fun launchCurrencyMarketPreviewActivity(currencyMarket: CurrencyMarket) {
        startActivity(CurrencyMarketPreviewActivity.newInstance(ctx, currencyMarket))
    }


    override fun shouldDisableRVAnimations(): Boolean {
        return false
    }


    override fun getOrderParameters(): OrderParameters{
        return orderParameters
    }


    override fun getInfoViewIcon(iconProvider: InfoViewIconProvider): Drawable? {
        return iconProvider.getIconForOrderType(orderParameters.type)
    }


    override fun getEmptyViewCaption(): String {
        return when(orderParameters.mode) {

            OrderModes.SEARCH -> {
                val searchQuery = orderParameters.searchQuery

                if(searchQuery.isNotBlank()) {
                    getString(R.string.orders_fragment_search_no_orders_found_template, searchQuery)
                } else {
                    getString(R.string.orders_fragment_search_initial_message)
                }
            }

            OrderModes.STANDARD -> {
                getString(when(orderParameters.type) {
                    OrderTypes.ACTIVE -> R.string.error_no_active_orders_available
                    OrderTypes.COMPLETED -> R.string.error_no_completed_orders_available
                    OrderTypes.CANCELLED -> R.string.error_no_cancelled_orders_available
                    OrderTypes.PUBLIC -> R.string.error_no_public_orders_available
                })
            }

        }
    }


    override fun getSearchQuery(): String = orderParameters.searchQuery


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getContentLayoutResourceId(): Int = R.layout.orders_fragment_layout


    override fun onBackPressed() {
        mPresenter?.onBackPressed()
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: Bundle?) {
        savedState?.apply {
            orderParameters = getParcelable(SAVED_STATE_ORDER_PARAMETERS)
        }

        currencyMarketsMap = (ObjectCache.remove(
            "${mPresenter!!}_$SAVED_STATE_CURRENCY_MARKETS_MAP",
            mapOf<String, CurrencyMarket>()
        ) as Map<String, CurrencyMarket>)

        super.onRestoreState(savedState)
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putParcelable(SAVED_STATE_ORDER_PARAMETERS, orderParameters)

        ObjectCache.put("${mPresenter!!}_$SAVED_STATE_CURRENCY_MARKETS_MAP", currencyMarketsMap)
    }


    override fun onRecycle(isChangingConfigurations: Boolean) {
        super.onRecycle(isChangingConfigurations)

        if(!isChangingConfigurations) {
            ObjectCache.remove("${mPresenter!!}_$SAVED_STATE_CURRENCY_MARKETS_MAP")
        }
    }


}