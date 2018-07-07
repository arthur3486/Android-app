package com.stocksexchange.android.ui.base.adapters.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.View
import com.stocksexchange.android.ui.base.adapters.recyclerview.resources.ItemResources

/**
 * A base view holder to extend from.
 */
abstract class BaseViewHolder<in IM, in IR>(itemView: View, resources: IR?) : RecyclerView.ViewHolder(itemView) where
    IM : Any,
    IR : ItemResources {


    /**
     * Performs the data binding.
     *
     * @param itemModel The model for the view holder
     * @param resources The adapter resources
     */
    abstract fun bind(itemModel: IM, resources: IR?)


}