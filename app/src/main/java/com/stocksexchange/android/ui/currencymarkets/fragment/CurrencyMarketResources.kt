package com.stocksexchange.android.ui.currencymarkets.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.adapters.recyclerview.resources.ItemResources
import com.stocksexchange.android.ui.utils.DoubleFormatter
import com.stocksexchange.android.ui.utils.extensions.getColoredCompatDrawable
import com.stocksexchange.android.ui.utils.extensions.getCompatColor
import com.stocksexchange.android.ui.utils.extensions.getLocale

data class CurrencyMarketResources(
    val lastPriceTvStyleResId: Int,
    val volumeTemplate: String,
    val colors: List<Int>,
    val drawables: List<Drawable?>,
    val formatter: DoubleFormatter
) : ItemResources {


    companion object {

        const val COLOR_RED_ACCENT = 0
        const val COLOR_GREEN_ACCENT = 1
        const val COLOR_BLUE_ACCENT = 2

        const val DAILY_CHANGE_DRAWABLE_RED = 0
        const val DAILY_CHANGE_DRAWABLE_GREEN = 1
        const val DAILY_CHANGE_DRAWABLE_BLUE = 2


        fun newInstance(context: Context): CurrencyMarketResources {
            val lastPriceTvStyleResId = R.style.CurrencyMarketLastPrice
            val volumeTemplate = context.getString(R.string.currency_market_item_volume_template)
            val locale = context.getLocale()

            val colors = listOf(
                context.getCompatColor(R.color.colorRedAccent),
                context.getCompatColor(R.color.colorGreenAccent),
                context.getCompatColor(R.color.colorBlueAccent)
            )
            val drawables = listOf(
                context.getColoredCompatDrawable(R.drawable.rounded_bg_drawable, R.color.colorRedAccent),
                context.getColoredCompatDrawable(R.drawable.rounded_bg_drawable, R.color.colorGreenAccent),
                context.getColoredCompatDrawable(R.drawable.rounded_bg_drawable, R.color.colorBlueAccent)
            )
            val formatter = DoubleFormatter.getInstance(locale)

            return CurrencyMarketResources(
                lastPriceTvStyleResId,
                volumeTemplate,
                colors,
                drawables,
                formatter
            )
        }

    }


}