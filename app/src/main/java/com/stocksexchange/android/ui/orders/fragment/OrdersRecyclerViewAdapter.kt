package com.stocksexchange.android.ui.orders.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.api.model.OrderTypes
import com.stocksexchange.android.model.SortTypes
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseRecyclerViewAdapter

class OrdersRecyclerViewAdapter(
    context: Context,
    items: MutableList<Order>
) : BaseRecyclerViewAdapter<Order, OrderViewHolder, OrderResources>(context, items) {


    /**
     * Resources for the adapter.
     */
    private val mResources: OrderResources = OrderResources.newInstance(context)

    /**
     * A listener used for notifying whenever a market name is clicked.
     */
    var mOnMarketNameClickListener: ((View, Order, CurrencyMarket?, Int) -> Unit)? = null

    /**
     * A listener used for notifying whenever a cancel button is clicked.
     */
    var mOnCancelBtnClickListener: ((View, Order, Int) -> Unit)? = null




    override fun onCreateViewHolder(parent: ViewGroup, inflater: LayoutInflater,
                                    resources: OrderResources?, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            inflater.inflate(OrderViewHolder.MAIN_LAYOUT_ID, parent, false),
            resources
        )
    }


    override fun onBindViewHolder(viewHolder: OrderViewHolder, itemModel: Order,
                                  resources: OrderResources?, viewType: Int) {
        viewHolder.bind(itemModel, resources)
    }


    override fun assignListeners(viewHolder: OrderViewHolder, itemModel: Order,
                                 position: Int, viewType: Int) {
        viewHolder.setOnMarketNameClickListener(mResources, position, itemModel, mOnMarketNameClickListener)
        viewHolder.setOnCancelBtnClickListener(position, itemModel, mOnCancelBtnClickListener)
    }


    fun containsOrder(order: Order): Boolean {
        for(item in getItems()) {
            if(item.id == order.id) {
                return true
            }
        }

        return false
    }


    fun deleteOrder(orderId: Long) {
        val items = getItems()

        for(index in items.indices) {
            if(items[index].id == orderId) {
                deleteItem(index)
                break
            }
        }
    }


    fun setOrderType(orderType: OrderTypes) {
        mResources.orderType = orderType
    }


    fun setCurrencyMarkets(currencyMarkets: Map<String, CurrencyMarket>) {
        mResources.currencyMarkets = currencyMarkets
    }


    fun getChronologicalPositionForOrder(order: Order, sortTypes: SortTypes): Int {
        val items = getItems()
        return items.indices.firstOrNull {
            items[it].compareTo(order) == (if(sortTypes == SortTypes.ASC) 1 else -1)
        } ?: itemCount
    }


    override fun getResources(): OrderResources? {
        return mResources
    }


}