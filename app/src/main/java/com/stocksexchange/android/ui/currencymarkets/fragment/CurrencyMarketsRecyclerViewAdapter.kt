package com.stocksexchange.android.ui.currencymarkets.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseRecyclerViewAdapter

class CurrencyMarketsRecyclerViewAdapter(
    context: Context,
    items: MutableList<CurrencyMarket>
) : BaseRecyclerViewAdapter<CurrencyMarket, CurrencyMarketViewHolder, CurrencyMarketResources>(context, items) {


    /**
     * Resources for the adapter.
     */
    private val mResources: CurrencyMarketResources = CurrencyMarketResources.newInstance(context)


    /**
     * A listener used for notifying whenever a particular item is clicked.
     */
    var mOnItemClickListener: ((View, CurrencyMarket, Int) -> Unit)? = null




    override fun onCreateViewHolder(parent: ViewGroup, inflater: LayoutInflater,
                                    resources: CurrencyMarketResources?, viewType: Int): CurrencyMarketViewHolder {
        return CurrencyMarketViewHolder(
            inflater.inflate(CurrencyMarketViewHolder.MAIN_LAYOUT_ID, parent, false),
            resources
        )
    }


    override fun onBindViewHolder(viewHolder: CurrencyMarketViewHolder, itemModel: CurrencyMarket,
                                  resources: CurrencyMarketResources?, viewType: Int) {
        viewHolder.bind(itemModel, resources)
    }


    override fun assignListeners(viewHolder: CurrencyMarketViewHolder, itemModel: CurrencyMarket,
                                 position: Int, viewType: Int) {
        viewHolder.setOnItemClickListener(position, itemModel, mOnItemClickListener)
    }


    /**
     * Sorts the data set using the given comparator and notifies
     * the adapter.
     *
     * @param comparator The comparator to sort the data set with
     */
    fun sort(comparator: CurrencyMarketComparator?) {
        if(comparator != null) {
            getItems().sortWith(comparator)

            notifyDataSetChanged()
        }
    }


    /**
     * Returns a currency market index for the specified id or null if
     * it is not present.
     *
     * @param id The id to get the market for
     *
     * @return The currency market index or null if it is absent
     */
    fun getMarketIndexForMarketId(id: Long): Int? {
        val items = getItems()
        return items.indices.firstOrNull { items[it].id == id }
    }


    /**
     * Retrieves a sorted position for the specified currency market.
     *
     * @param currencyMarket The currency market to get the position for
     * @param comparator The comparator to compare two currency markets
     *
     * @return The sorted position for the market
     */
    fun getChronologicalPositionForCurrencyMarket(currencyMarket: CurrencyMarket, comparator: CurrencyMarketComparator?): Int {
        if(comparator == null) {
            return itemCount
        }

        val items = getItems()
        return items.indices.firstOrNull { comparator.compare(items[it], currencyMarket) == -1 } ?: itemCount
    }


    override fun getResources(): CurrencyMarketResources? {
        return mResources
    }


}