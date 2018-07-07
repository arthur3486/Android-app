package com.stocksexchange.android.ui.help

import android.content.Context
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.adapters.recyclerview.resources.ItemResources

data class HelpItemResources(
    val animationDuration: Long
) : ItemResources {


    companion object {

        fun newInstance(context: Context): HelpItemResources {
            val animationDuration = context.resources.getInteger(R.integer.help_item_answer_tv_animation_duration).toLong()

            return HelpItemResources(animationDuration)
        }

    }


}