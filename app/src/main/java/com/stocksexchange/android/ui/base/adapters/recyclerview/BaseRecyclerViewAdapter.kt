package com.stocksexchange.android.ui.base.adapters.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stocksexchange.android.ui.base.adapters.recyclerview.resources.ItemResources
import com.stocksexchange.android.ui.utils.extensions.getLayoutInflater
import com.stocksexchange.android.ui.utils.listeners.OnDataSetChangeListener

/**
 * A base recycler view adapter holding the common functionality
 * among all adapters.
 */
abstract class BaseRecyclerViewAdapter<IT, VH, IR>(
    private val mContext: Context,
    private var mItems: MutableList<IT>
) : RecyclerView.Adapter<VH>() where
        IT : Any,
        VH : BaseViewHolder<IT, IR>,
        IR : ItemResources {


    /**
     * A layout inflater used by this adapter to inflate views.
     */
    private val mLayoutInflater: LayoutInflater = mContext.getLayoutInflater()

    /**
     * A listener to invoke whenever data set is changed (item added, removed, updated, and so on).
     */
    var mOnDataSetChangeListener: OnDataSetChangeListener<MutableList<IT>, IT>? = null




    /**
     * Retrieves the context.
     *
     * @return The android context
     */
    protected fun getContext(): Context {
        return mContext
    }


    /**
     * Retrieves the items this adapter holds..
     *
     * @return The adapter's items
     */
    protected fun getItems(): MutableList<IT> {
        return mItems
    }


    /**
     * Retrieves the layout inflater.
     *
     * @return The layout inflater
     */
    protected fun getLayoutInflater(): LayoutInflater {
        return mLayoutInflater
    }


    /**
     * Adds an item to the adapter.
     *
     * @param item The item itself
     * @param position The position to place the item within the data set
     * @param notifyAboutChange true to notify adapter; false otherwise
     */
    open fun addItem(item: IT, position: Int = itemCount, notifyAboutChange: Boolean = true) {
        if(position !in 0..itemCount) {
            return
        }

        mItems.add(position, item)

        if(notifyAboutChange) {
            notifyItemInserted(position)
        }

        notifyItemAdded(item)
    }


    /**
     * Updates an item at a specific position within the adapter.
     *
     * @param position The position of the item within the adapter
     */
    open fun updateItem(position: Int) {
        if(position !in 0 until itemCount) {
            return
        }

        notifyItemChanged(position)
        notifyItemUpdated(getItem(position)!!)
    }


    open fun updateItems(startPosition: Int, itemCount: Int) {
        if((startPosition in 0 until itemCount) && (itemCount > 0)) {
            notifyItemRangeChanged(startPosition, itemCount)
        }
    }


    /**
     * Updates an item within the adapter with a new item.
     *
     * @param item The new item
     * @param position The position of the old item within a data set
     * @param notifyAboutChange true to notify adapter; false otherwise
     */
    open fun updateItemWith(item: IT, position: Int = indexOf(item), notifyAboutChange: Boolean = true) {
        if(position !in 0 until itemCount) {
            return
        }

        val oldItem = getItem(position)

        mItems[position] = item

        if(notifyAboutChange) {
            notifyItemChanged(position)
        }

        notifyItemReplaced(oldItem!!, item)
    }


    /**
     * Adds or updates an item within the data set.
     *
     * @param item The new item
     * @param position The item's position
     * @param notifyAboutChange true to notify adapter; false otherwise
     */
    open fun addOrUpdateItem(item: IT, position: Int = itemCount, notifyAboutChange: Boolean = true) {
        if(position !in 0..itemCount) {
            return
        }

        if(contains(item)) {
            updateItemWith(item = item, notifyAboutChange = notifyAboutChange)
        } else {
            addItem(item, position, notifyAboutChange)
        }
    }


    /**
     * Removes an item from within the data set.
     *
     * @param item The item to remove
     */
    open fun deleteItem(item: IT) {
        deleteItem(indexOf(item))
    }


    /**
     * Removes an item from within the data set.
     *
     * @param position The item's position
     * @param notifyAboutChange true to notify adapter; false otherwise
     */
    open fun deleteItem(position: Int, notifyAboutChange: Boolean = true) {
        if(position !in 0 until itemCount) {
            return
        }

        val removedItem = mItems.removeAt(position)

        if(notifyAboutChange) {
            notifyItemRemoved(position)
        }

        notifyItemDeleted(removedItem)
    }


    /**
     * Determines whether the adapter contains the item or not.
     *
     * @param item The item to determine whether it is included in the data set
     *
     * @return true if the adapter contains the item; false otherwise
     */
    open fun contains(item: IT): Boolean {
        return mItems.contains(item)
    }


    /**
     * Returns an index of this item within the data set.
     *
     * @param item The item to find the index of
     *
     * @return The index of the item
     */
    open fun indexOf(item: IT): Int {
        return mItems.indexOf(item)
    }


    /**
     * Clears the adapter's data set.
     */
    open fun clear() {
        if(mItems.isEmpty()) {
            return
        }

        mItems.clear()

        notifyDataSetChanged()
        notifyDataSetCleared(mItems)
    }


    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder(parent, mLayoutInflater, getResources(), viewType)
    }


    /**
     * An improved version of [onBindViewHolder] with a couple of additional parameters passed.
     *
     * @param parent The parent to attach the root view of the view holder to
     * @param inflater The layout inflater
     * @param resources The adapter resources
     * @param viewType The view type to help to determine the view type for the view holder
     *
     * @return The view holder for the specified view type
     */
    abstract fun onCreateViewHolder(parent: ViewGroup, inflater: LayoutInflater, resources: IR?, viewType: Int): VH


    final override fun onBindViewHolder(viewHolder: VH, position: Int) {
        val itemModel = getItem(position)!!
        val viewType = getItemViewType(position)

        onBindViewHolder(viewHolder, itemModel, getResources(), viewType)
        assignListeners(viewHolder, itemModel, position, viewType)
    }


    /**
     * An improved version of [onBindViewHolder] with a couple of
     * additional parameters passed.
     *
     * @param viewHolder The view holder to bind data to
     * @param itemModel The model object to populate the view holder with
     * @param resources The adapter resources
     * @param viewType The view type of the item
     */
    abstract fun onBindViewHolder(viewHolder: VH, itemModel: IT, resources: IR?, viewType: Int)


    /**
     * Responsible for assigning listeners to view holders.
     *
     * @param viewHolder The view holder to assign listener to
     * @param itemModel The item model associated with the view holder
     * @param position The position of the item within the adapter's data set
     * @param viewType The view type associated with the item
     */
    protected open fun assignListeners(viewHolder: VH, itemModel: IT, position: Int, viewType: Int) {
        // Stub
    }


    /**
     * Notifies a data set listener that an item has been added.
     *
     * @param item The added item
     */
    private fun notifyItemAdded(item: IT) {
        mOnDataSetChangeListener?.onItemAdded(mItems, item)
    }


    /**
     * Notifies a data set listener that an item has been updated.
     *
     * @param item The updated item
     */
    private fun notifyItemUpdated(item: IT) {
        mOnDataSetChangeListener?.onItemUpdated(mItems, item)
    }


    /**
     * Notifies a data set listener that an item has been replaced with a new one.
     *
     * @param oldItem The replaced item
     * @param newItem The new item
     */
    private fun notifyItemReplaced(oldItem: IT, newItem: IT) {
        mOnDataSetChangeListener?.onItemReplaced(mItems, oldItem, newItem)
    }


    /**
     * Notifies a data set listener that an item has been deleted.
     *
     * @param item The deleted item
     */
    private fun notifyItemDeleted(item: IT) {
        mOnDataSetChangeListener?.onItemDeleted(mItems, item)
    }


    /**
     * Notifies a data set listener that a data set has been replaced with a new one.
     *
     * @param newDataSet The new data set
     */
    private fun notifyDataSetReplaced(newDataSet: MutableList<IT>) {
        mOnDataSetChangeListener?.onDataSetReplaced(newDataSet)
    }


    /**
     * Notifies a data set listener that a data set has been cleared.
     *
     * @param dataSet The cleared data set
     */
    private fun notifyDataSetCleared(dataSet: MutableList<IT>) {
        mOnDataSetChangeListener?.onDataSetCleared(dataSet)
    }


    /**
     * Sets items as adapter's data set.
     *
     * @param items The items for the adapter
     * @param notifyAboutChange true to notify adapter; false otherwise
     */
    open fun setItems(items: MutableList<IT>, notifyAboutChange: Boolean = true) {
        mItems.clear()
        mItems.addAll(items)

        if(notifyAboutChange) {
            notifyDataSetChanged()
        }

        notifyDataSetReplaced(items)
    }


    /**
     * Retrieves an item from the data set with a specified position.
     *
     * @param position The position to fetch the item at
     *
     * @return The item at the position or null
     */
    open fun getItem(position: Int): IT? {
        return if(position in 0 until itemCount) {
            mItems[position]
        } else {
            null
        }
    }


    /**
     * Returns the adapter data set's size.
     *
     * @return The adapter's item count
     */
    override fun getItemCount(): Int {
        return mItems.size
    }


    /**
     * Retrieves resources for the adapter.
     *
     * @return The resources associated with the adapter
     */
    open fun getResources(): IR? {
        return null
    }


}