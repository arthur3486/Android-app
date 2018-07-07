package com.stocksexchange.android.ui.wallets.fragment

import android.content.Context
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.adapters.recyclerview.resources.ItemResources
import com.stocksexchange.android.ui.utils.DoubleFormatter
import com.stocksexchange.android.ui.utils.extensions.getCompatColor
import com.stocksexchange.android.ui.utils.extensions.getLocale

class WalletsResources(
    val strings: List<String>,
    val colors: List<Int>,
    val formatter: DoubleFormatter
) : ItemResources {


    companion object {

        const val STRING_ACTIVE = 0
        const val STRING_DISABLED = 1

        const val COLOR_GREEN = 0
        const val COLOR_RED = 1


        fun newInstance(context: Context): WalletsResources {
            val strings = listOf(
                context.getString(R.string.state_active),
                context.getString(R.string.state_disabled)
            )
            val colors = listOf(
                context.getCompatColor(R.color.colorGreenAccent),
                context.getCompatColor(R.color.colorRedAccent)
            )

            val priceFormatter = DoubleFormatter.getInstance(context.getLocale())

            return WalletsResources(strings, colors, priceFormatter)
        }

    }


}