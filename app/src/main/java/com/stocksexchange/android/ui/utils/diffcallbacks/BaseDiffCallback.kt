package com.stocksexchange.android.ui.utils.diffcallbacks

import android.support.v7.util.DiffUtil

/**
 * A base diff utility callback to extend from.
 */
abstract class BaseDiffCallback<out T : Any>(
    protected val oldList: List<T>,
    protected val newList: List<T>
) : DiffUtil.Callback() {


    override fun getOldListSize(): Int = oldList.size


    override fun getNewListSize(): Int = newList.size


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition] == newList[newItemPosition])
    }


}