package com.stocksexchange.android.ui.help

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stocksexchange.android.model.HelpItem
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseRecyclerViewAdapter

class HelpRecyclerViewAdapter(
    context: Context,
    items: MutableList<HelpItem>
): BaseRecyclerViewAdapter<HelpItem, HelpItemViewHolder, HelpItemResources>(context, items) {


    /**
     * Resources for the adapter.
     */
    private val mResources: HelpItemResources = HelpItemResources.newInstance(context)

    /**
     * A listener used for notifying whenever a particular item is clicked.
     */
    var mOnItemClickListener: ((HelpItemViewHolder, HelpItem, Int) -> Unit)? = null




    override fun onCreateViewHolder(parent: ViewGroup, inflater: LayoutInflater,
                                    resources: HelpItemResources?, viewType: Int): HelpItemViewHolder {
        return HelpItemViewHolder(inflater.inflate(HelpItemViewHolder.MAIN_LAYOUT_ID, parent, false), resources)
    }


    override fun onBindViewHolder(viewHolder: HelpItemViewHolder, itemModel: HelpItem, resources: HelpItemResources?, viewType: Int) {
        viewHolder.bind(itemModel, resources)
    }


    override fun assignListeners(viewHolder: HelpItemViewHolder, itemModel: HelpItem, position: Int, viewType: Int) {
        viewHolder.setOnItemClickListener(position, itemModel, mOnItemClickListener)
    }


    override fun getResources(): HelpItemResources? {
        return mResources
    }



}