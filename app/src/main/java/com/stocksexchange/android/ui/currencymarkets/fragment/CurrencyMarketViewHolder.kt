package com.stocksexchange.android.ui.currencymarkets.fragment

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextSwitcher
import android.widget.TextView
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseViewHolder
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketResources.Companion.COLOR_BLUE_ACCENT
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketResources.Companion.COLOR_GREEN_ACCENT
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketResources.Companion.COLOR_RED_ACCENT
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketResources.Companion.DAILY_CHANGE_DRAWABLE_BLUE
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketResources.Companion.DAILY_CHANGE_DRAWABLE_GREEN
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketResources.Companion.DAILY_CHANGE_DRAWABLE_RED
import com.stocksexchange.android.ui.utils.extensions.loadAnimation
import com.stocksexchange.android.ui.utils.extensions.setTextAppearanceCompat

class CurrencyMarketViewHolder(
    itemView: View,
    resources: CurrencyMarketResources?
) : BaseViewHolder<CurrencyMarket, CurrencyMarketResources>(itemView, resources) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.currency_market_item_layout

    }


    val mStatusBarView: View = itemView.findViewById(R.id.statusBarView)

    val mCurrencyTv: TextView = itemView.findViewById(R.id.currencyTv)
    val mMarketTv: TextView = itemView.findViewById(R.id.marketTv)
    val mVolumeTv: TextView = itemView.findViewById(R.id.volumeTv)
    val mDailyChangeTv: TextView = itemView.findViewById(R.id.dailyChangeTv)

    val mLastPriceTs: TextSwitcher = itemView.findViewById(R.id.lastPriceTs)




    init {
        mLastPriceTs.setFactory {
            val textView = TextView(itemView.context)
            textView.setTextAppearanceCompat(resources!!.lastPriceTvStyleResId)

            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER
            textView.layoutParams = layoutParams

            textView
        }
        mLastPriceTs.inAnimation = itemView.context.loadAnimation(R.anim.pop_in_animation)
        mLastPriceTs.outAnimation = itemView.context.loadAnimation(R.anim.pop_out_animation)
    }


    override fun bind(itemModel: CurrencyMarket, resources: CurrencyMarketResources?) {
        mCurrencyTv.text = itemModel.currency
        mMarketTv.text = itemModel.market

        mVolumeTv.text = resources!!.volumeTemplate.format(resources.formatter.formatVolume(itemModel.lastVolume))
        mLastPriceTs.setText(resources.formatter.formatPrice(itemModel.lastPrice))
        mDailyChangeTv.text = resources.formatter.formatDailyChange(itemModel.dailyChange)

        when {
            (itemModel.dailyChange > 0.0) -> {
                mStatusBarView.setBackgroundColor(resources.colors[COLOR_GREEN_ACCENT])
                mDailyChangeTv.background = resources.drawables[DAILY_CHANGE_DRAWABLE_GREEN]
            }
            (itemModel.dailyChange < 0.0) -> {
                mStatusBarView.setBackgroundColor(resources.colors[COLOR_RED_ACCENT])
                mDailyChangeTv.background = resources.drawables[DAILY_CHANGE_DRAWABLE_RED]
            }
            else -> {
                mStatusBarView.setBackgroundColor(resources.colors[COLOR_BLUE_ACCENT])
                mDailyChangeTv.background = resources.drawables[DAILY_CHANGE_DRAWABLE_BLUE]
            }
        }
    }


    fun setOnItemClickListener(position: Int, itemModel: CurrencyMarket, listener: ((View, CurrencyMarket, Int) -> Unit)?) {
        itemView.setOnClickListener { listener?.invoke(it, itemModel, position) }
    }


}