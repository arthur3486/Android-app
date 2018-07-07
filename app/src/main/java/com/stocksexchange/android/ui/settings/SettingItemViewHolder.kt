package com.stocksexchange.android.ui.settings

import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.TextView
import com.stocksexchange.android.R
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseViewHolder
import com.stocksexchange.android.ui.utils.extensions.isVisible
import com.stocksexchange.android.ui.utils.extensions.makeGone
import com.stocksexchange.android.ui.utils.extensions.makeVisible

class SettingItemViewHolder(
    itemView: View,
    resources: SettingResources?
) : BaseViewHolder<Setting, SettingResources>(itemView, resources) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.setting_item_layout

    }


    val mTitleTv: TextView = itemView.findViewById(R.id.titleTv)
    val mDescriptionTv: TextView = itemView.findViewById(R.id.descriptionTv)

    val mSwitchView: SwitchCompat = itemView.findViewById(R.id.switchView)




    override fun bind(itemModel: Setting, resources: SettingResources?) {
        mTitleTv.text = itemModel.title

        if(itemModel.hasDescription()) {
            mDescriptionTv.text = itemModel.description
            mDescriptionTv.makeVisible()
        } else {
            mDescriptionTv.makeGone()
        }

        mSwitchView.setOnCheckedChangeListener(null)

        if(itemModel.isCheckable) {
            mSwitchView.isChecked = itemModel.isChecked
            mSwitchView.makeVisible()
        } else {
            mSwitchView.makeGone()
        }
    }


    fun setOnItemClickListener(position: Int, itemModel: Setting, listener: ((SettingItemViewHolder, Setting, Int) -> Unit)?) {
        itemView.setOnClickListener {
            if(mSwitchView.isVisible()) {
                mSwitchView.isChecked = !mSwitchView.isChecked
            } else {
                listener?.invoke(this, itemModel, position)
            }
        }
    }


    fun setOnSwitchClickListener(position: Int, itemModel: Setting,
                                 listener: ((SettingItemViewHolder, Setting, Int, Boolean) -> Unit)?) {
        mSwitchView.setOnCheckedChangeListener { _, isChecked ->
            listener?.invoke(this, itemModel, position, isChecked)
        }
    }


}