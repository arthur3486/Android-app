package com.stocksexchange.android.ui.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingSection
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseRecyclerViewAdapter
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseViewHolder

class SettingsRecyclerViewAdapter(
    context: Context,
    items: MutableList<Any>
) : BaseRecyclerViewAdapter<Any, BaseViewHolder<Any, SettingResources>, SettingResources>(context, items) {


    companion object {

        private const val VIEW_TYPE_SECTION = 0
        private const val VIEW_TYPE_ITEM = 1

    }


    /**
     * Resources for the adapter.
     */
    private val mResources: SettingResources = SettingResources.newInstance()

    /**
     * A listener used for notifying whenever a setting item is clicked.
     */
    var mOnSettingItemClickListener: ((SettingItemViewHolder, Setting, Int) -> Unit)? = null

    /**
     * A listener used for notifying whenever a switch is clicked.
     */
    var mOnSwitchClickListener: ((SettingItemViewHolder, Setting, Int, Boolean) -> Unit)? = null




    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, inflater: LayoutInflater,
                                    resources: SettingResources?, viewType: Int): BaseViewHolder<Any, SettingResources> {
        return (if(viewType == VIEW_TYPE_SECTION) {
            SettingSectionViewHolder(inflater.inflate(SettingSectionViewHolder.MAIN_LAYOUT_ID, parent, false), resources)
        } else {
            SettingItemViewHolder(inflater.inflate(SettingItemViewHolder.MAIN_LAYOUT_ID, parent, false), resources)
        }) as BaseViewHolder<Any, SettingResources>
    }


    override fun onBindViewHolder(viewHolder: BaseViewHolder<Any, SettingResources>, itemModel: Any,
                                  resources: SettingResources?, viewType: Int) {
        if(viewType == VIEW_TYPE_SECTION) {
            (viewHolder as SettingSectionViewHolder).bind((itemModel as SettingSection), resources)
        } else {
            (viewHolder as SettingItemViewHolder).bind((itemModel as Setting), resources)
        }
    }


    override fun assignListeners(viewHolder: BaseViewHolder<Any, SettingResources>, itemModel: Any,
                                 position: Int, viewType: Int) {
        if(viewType == VIEW_TYPE_ITEM) {
            val settingItemViewHolder = (viewHolder as SettingItemViewHolder)
            val setting = (itemModel as Setting)

            settingItemViewHolder.setOnItemClickListener(
                position,
                setting,
                mOnSettingItemClickListener
            )
            settingItemViewHolder.setOnSwitchClickListener(
                position,
                setting,
                mOnSwitchClickListener
            )
        }
    }


    fun getSettingIndex(setting: Setting): Int {
        val items = getItems()

        for(index in items.indices) {
            if(items[index] is Setting && (items[index] as Setting).id == setting.id) {
                return index
            }
        }

        return itemCount
    }


    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is SettingSection -> VIEW_TYPE_SECTION

            else -> VIEW_TYPE_ITEM
        }
    }


    override fun getResources(): SettingResources? {
        return mResources
    }


}