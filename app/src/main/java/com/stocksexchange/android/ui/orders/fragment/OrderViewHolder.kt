package com.stocksexchange.android.ui.orders.fragment

import android.annotation.SuppressLint
import android.text.SpannableString
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.Order
import com.stocksexchange.android.api.model.OrderTypes
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseViewHolder
import com.stocksexchange.android.ui.orders.fragment.OrderResources.Companion.COLOR_LINK_NORMAL_BACKGROUND
import com.stocksexchange.android.ui.orders.fragment.OrderResources.Companion.COLOR_LINK_SELECTED_BACKGROUND
import com.stocksexchange.android.ui.orders.fragment.OrderResources.Companion.COLOR_TYPE_GREEN
import com.stocksexchange.android.ui.orders.fragment.OrderResources.Companion.COLOR_TYPE_RED
import com.stocksexchange.android.ui.orders.fragment.OrderResources.Companion.DRAWABLE_TYPE_GREEN
import com.stocksexchange.android.ui.orders.fragment.OrderResources.Companion.DRAWABLE_TYPE_RED
import com.stocksexchange.android.ui.orders.fragment.OrderResources.Companion.STRING_RESULT_RECEIVED
import com.stocksexchange.android.ui.orders.fragment.OrderResources.Companion.STRING_RESULT_SPENT
import com.stocksexchange.android.ui.orders.fragment.OrderResources.Companion.STRING_TYPE_BUY
import com.stocksexchange.android.ui.orders.fragment.OrderResources.Companion.STRING_TYPE_SELL
import com.stocksexchange.android.ui.utils.extensions.getLayerDrawable
import com.stocksexchange.android.ui.utils.extensions.makeGone
import com.stocksexchange.android.ui.utils.extensions.makeVisible
import com.stocksexchange.android.ui.utils.text.CustomLinkMovementMethod
import com.stocksexchange.android.ui.utils.text.SelectorSpan
import com.stocksexchange.android.ui.views.DottedOptionTextView

class OrderViewHolder(
    itemView: View,
    resources: OrderResources?
) : BaseViewHolder<Order, OrderResources>(itemView, resources) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.order_item_layout

    }


    val mTypeTv: TextView = itemView.findViewById(R.id.typeTv)
    val mMarketTv: TextView = itemView.findViewById(R.id.marketTv)
    val mCancelBtn: TextView = itemView.findViewById(R.id.cancelBtn)

    val mIdDotv: DottedOptionTextView = itemView.findViewById(R.id.idDotv)
    val mAmountDotv: DottedOptionTextView = itemView.findViewById(R.id.amountDotv)
    val mPriceDotv: DottedOptionTextView = itemView.findViewById(R.id.priceDotv)
    val mResultDotv: DottedOptionTextView = itemView.findViewById(R.id.resultDotv)
    val mIncomeDotv: DottedOptionTextView = itemView.findViewById(R.id.incomeDotv)
    val mDateDotv: DottedOptionTextView = itemView.findViewById(R.id.dateDotv)

    val mTopBarFl: FrameLayout = itemView.findViewById(R.id.topBarFl)





    @SuppressLint("SetTextI18n")
    override fun bind(itemModel: Order, resources: OrderResources?) {
        if(itemModel.isBuyTrade()) {
            mTypeTv.background = resources!!.drawables[DRAWABLE_TYPE_GREEN]
            mTypeTv.setTextColor(resources.colors[COLOR_TYPE_GREEN])
            mTypeTv.text = resources.strings[STRING_TYPE_BUY]
        } else {
            mTypeTv.background = resources!!.drawables[DRAWABLE_TYPE_RED]
            mTypeTv.setTextColor(resources.colors[COLOR_TYPE_RED])
            mTypeTv.text = resources.strings[STRING_TYPE_SELL]
        }

        when(resources.orderType) {

            OrderTypes.ACTIVE -> {
                calculateId(itemModel)
                calculateAmount(itemModel, resources)
                calculateIncome(itemModel, resources)

                mResultDotv.makeGone()
            }

            OrderTypes.COMPLETED -> {
                calculateId(itemModel)
                calculateAmount(itemModel, resources)
                calculateResult(itemModel, resources)

                mIncomeDotv.makeGone()
            }

            OrderTypes.CANCELLED -> {
                calculateId(itemModel)
                calculateAmount(itemModel, resources)

                mResultDotv.makeGone()
                mIncomeDotv.makeGone()
            }

            OrderTypes.PUBLIC -> {
                calculateAmount(itemModel, resources)

                mIdDotv.makeGone()
                mResultDotv.makeGone()
                mIncomeDotv.makeGone()
            }

        }

        if((resources.orderType == OrderTypes.ACTIVE) || (resources.orderType == OrderTypes.PUBLIC)) {
            mPriceDotv.setValueText("${resources.doubleFormatter.formatPrice(itemModel.rate)} ${itemModel.market}")
        } else {
            val rates = itemModel.ratesMap.keys

            if(rates.isNotEmpty()) {
                mPriceDotv.setValueText("${resources.doubleFormatter.formatPrice(rates.first())} ${itemModel.market}")
            } else {
                mPriceDotv.makeGone()
            }
        }

        mDateDotv.setValueText(resources.timeFormatter.formatDate(itemModel.timestamp))

        if(resources.orderType == OrderTypes.ACTIVE) {
            mCancelBtn.makeVisible()
            mCancelBtn.background = itemView.context.getLayerDrawable(
                R.drawable.secondary_button_background,
                R.color.colorBlueAccent,
                R.color.colorCardView
            )
        } else {
            mCancelBtn.makeGone()
        }
    }


    private fun calculateId(itemModel: Order) {
        mIdDotv.setValueText(itemModel.id.toString())
    }


    private fun calculateAmount(itemModel: Order, resources: OrderResources?) {
        val formatter = resources!!.doubleFormatter

        if(resources.orderType == OrderTypes.COMPLETED) {
            if(itemModel.isBuyTrade()) {
                mAmountDotv.setValueText("${formatter.formatBalance(itemModel.buyAmount)} ${itemModel.currency}")
            } else {
                mAmountDotv.setValueText("${formatter.formatBalance(itemModel.sellAmount)} ${itemModel.currency}")
            }
        } else if(resources.orderType == OrderTypes.CANCELLED) {
            mAmountDotv.setValueText("${formatter.formatBalance(itemModel.originalAmount)} ${itemModel.currency}")
        } else {
            mAmountDotv.setValueText("${formatter.formatBalance(itemModel.amount)} ${itemModel.currency}")
        }
    }


    private fun calculateResult(itemModel: Order, resources: OrderResources?) {
        val formatter = resources!!.doubleFormatter

        if(resources.orderType == OrderTypes.COMPLETED) {
            if(itemModel.isBuyTrade()) {
                mResultDotv.setTitleText(resources.strings[STRING_RESULT_SPENT])
                mResultDotv.setValueText("${formatter.formatBalance(itemModel.sellAmount)} ${itemModel.market}")
            } else {
                mResultDotv.setTitleText(resources.strings[STRING_RESULT_RECEIVED])
                mResultDotv.setValueText("${formatter.formatBalance(itemModel.buyAmount)} ${itemModel.market}")
            }
        }
    }


    private fun calculateIncome(itemModel: Order, resources: OrderResources?) {
        mIncomeDotv.setValueText(resources!!.doubleFormatter.formatPrice(itemModel.amount * itemModel.rate))
    }


    fun setOnMarketNameClickListener(resources: OrderResources?, position: Int,
                                     itemModel: Order, listener: ((View, Order, CurrencyMarket?, Int) -> Unit)?) {
        val marketName = "${itemModel.currency} / ${itemModel.market}"

        if(resources!!.orderType != OrderTypes.PUBLIC) {
            val spannableString = SpannableString(marketName)
            val currencyMarket = resources.currencyMarkets[itemModel.marketName]

            spannableString.setSpan(
                getClickableSpanForEntry(resources, itemModel, currencyMarket, position, listener),
                0, spannableString.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            mMarketTv.movementMethod = CustomLinkMovementMethod()
            mMarketTv.text = spannableString
        } else {
            mMarketTv.text = marketName
        }
    }


    fun setOnCancelBtnClickListener(position: Int, itemModel: Order, listener: ((View, Order, Int) -> Unit)?) {
        mCancelBtn.setOnClickListener { listener?.invoke(it, itemModel, position) }
    }


    private fun getClickableSpanForEntry(resources: OrderResources?, itemModel: Order,
                                         currencyMarket: CurrencyMarket?, position: Int,
                                         listener: ((View, Order, CurrencyMarket?, Int) -> Unit)?): SelectorSpan {
        return object : SelectorSpan(
            resources!!.colors[COLOR_LINK_NORMAL_BACKGROUND],
            resources.colors[COLOR_LINK_SELECTED_BACKGROUND]
        ) {

            override fun onClick(widget: View) {
                listener?.invoke(widget, itemModel, currencyMarket, position)
            }

        }
    }


}