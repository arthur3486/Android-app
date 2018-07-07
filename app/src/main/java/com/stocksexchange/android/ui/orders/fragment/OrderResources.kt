package com.stocksexchange.android.ui.orders.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.OrderTypes
import com.stocksexchange.android.ui.base.adapters.recyclerview.resources.ItemResources
import com.stocksexchange.android.ui.utils.DoubleFormatter
import com.stocksexchange.android.ui.utils.TimeFormatter
import com.stocksexchange.android.ui.utils.extensions.getCompatColor
import com.stocksexchange.android.ui.utils.extensions.getLayerDrawable
import com.stocksexchange.android.ui.utils.extensions.getLocale

class OrderResources(
    val drawables: List<Drawable?>,
    val colors: List<Int>,
    val strings: List<String>,
    val timeFormatter: TimeFormatter,
    val doubleFormatter: DoubleFormatter
) : ItemResources {


    companion object {

        const val STRING_TYPE_BUY = 0
        const val STRING_TYPE_SELL = 1
        const val STRING_RESULT_SPENT = 2
        const val STRING_RESULT_RECEIVED = 3

        const val DRAWABLE_TYPE_RED = 0
        const val DRAWABLE_TYPE_GREEN = 1

        const val COLOR_TYPE_RED = 0
        const val COLOR_TYPE_GREEN = 1
        const val COLOR_LINK_NORMAL_BACKGROUND = 2
        const val COLOR_LINK_SELECTED_BACKGROUND = 3


        fun newInstance(context: Context): OrderResources {
            val strings = listOf(
                context.getString(R.string.trade_type_buy),
                context.getString(R.string.trade_type_sell),
                context.getString(R.string.action_spent),
                context.getString(R.string.action_received)
            )

            val drawables = listOf(
                context.getLayerDrawable(R.drawable.secondary_button_background, R.color.colorRedAccent, R.color.colorCardView),
                context.getLayerDrawable(R.drawable.secondary_button_background, R.color.colorGreenAccent, R.color.colorCardView)
            )

            val colors = listOf(
                context.getCompatColor(R.color.colorRedAccent),
                context.getCompatColor(R.color.colorGreenAccent),
                context.getCompatColor(R.color.colorLinkNormalBackground),
                context.getCompatColor(R.color.colorLinkSelectedBackground)
            )

            val timeFormatter = TimeFormatter.getInstance(context)
            val doubleFormatter = DoubleFormatter.getInstance(context.getLocale())

            return OrderResources(
                drawables,
                colors,
                strings,
                timeFormatter,
                doubleFormatter
            )
        }

    }


    var orderType: OrderTypes = OrderTypes.ACTIVE

    var currencyMarkets: Map<String, CurrencyMarket> = mapOf()


}