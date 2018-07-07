package com.stocksexchange.android.ui.settings

import android.view.View
import android.widget.TextView
import com.stocksexchange.android.R
import com.stocksexchange.android.model.SettingSection
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseViewHolder

class SettingSectionViewHolder(
    itemView: View,
    resources: SettingResources?
) : BaseViewHolder<SettingSection, SettingResources>(itemView, resources) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.setting_section_item_layout

    }


    val titleTv: TextView = itemView.findViewById(R.id.titleTv)




    override fun bind(itemModel: SettingSection, resources: SettingResources?) {
        titleTv.text = itemModel.title
    }


}