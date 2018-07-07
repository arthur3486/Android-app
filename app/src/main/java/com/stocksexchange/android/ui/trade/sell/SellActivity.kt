package com.stocksexchange.android.ui.trade.sell

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.api.model.OrderTradeTypes
import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.ui.base.activities.BaseTradeActivity
import com.stocksexchange.android.ui.utils.extensions.getStateListDrawable
import org.jetbrains.anko.intentFor

class SellActivity : BaseTradeActivity<SellPresenter>(), SellContract.View {


    companion object {

        fun newInstance(context: Context, currencyMarket: CurrencyMarket,
                        currencyMarketSummary: CurrencyMarketSummary, user: User): Intent {
            return context.intentFor<SellActivity>(
                EXTRA_ORDER_TRADE_TYPE to OrderTradeTypes.SELL,
                EXTRA_CURRENCY_MARKET to currencyMarket,
                EXTRA_CURRENCY_MARKET_SUMMARY to currencyMarketSummary,
                EXTRA_USER to user
            )
        }

    }




    override fun initPresenter(): SellPresenter = SellPresenter(this)


    override fun getAtPrice(): Double = mCurrencyMarket.dailySellMinPrice


    override fun getAtPriceInputHintStringRes(): Int = R.string.action_bid


    override fun getTradeButtonText(): String {
        return getString(
            R.string.trade_activity_sell_button_template,
            mCurrencyMarket.currency,
            mCurrencyMarket.market
        )
    }


    override fun getTradeButtonBackground(): Drawable {
        return getStateListDrawable(
            R.drawable.button_bg_state_pressed,
            R.color.colorRedAccentDark,
            R.drawable.button_bg_state_released,
            R.color.colorRedAccent
        )
    }


    override fun calculateTransactionFee(amount: Double, price: Double): Double {
        return (amount * price * getFeePercent() / 100.0)
    }


    override fun getTransactionFeeCoinName(): String {
        return mCurrencyMarket.market
    }


    override fun getFeePercent(): Double {
        return mCurrencyMarketSummary.sellFeePercent
    }


    override fun calculateUserDeduction(amount: Double, price: Double): Double {
        return amount
    }


    override fun getUserDeductionCoinName(): String {
        return mCurrencyMarket.currency
    }


    override fun calculateUserAddition(amount: Double, price: Double): Double {
        return (amount * price - calculateTransactionFee(amount, price))
    }


    override fun getUserAdditionCoinName(): String {
        return mCurrencyMarket.market
    }


}