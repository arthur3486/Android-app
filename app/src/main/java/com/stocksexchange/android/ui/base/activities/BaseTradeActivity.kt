package com.stocksexchange.android.ui.base.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.api.model.OrderTradeTypes
import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.ui.base.mvp.presenters.BaseTradePresenter
import com.stocksexchange.android.ui.base.mvp.views.TradeView
import com.stocksexchange.android.ui.utils.DoubleFormatter
import com.stocksexchange.android.ui.utils.extensions.*
import com.stocksexchange.android.ui.utils.listeners.adapters.TextWatcherAdapter
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import kotlinx.android.synthetic.main.trade_activity_layout.*
import org.jetbrains.anko.ctx
import java.util.*

/**
 * A base trading activity to extend from.
 */
abstract class BaseTradeActivity<P : BaseTradePresenter<*, *>> : BaseActivity<P>(),
    TradeView {


    companion object {

        internal const val EXTRA_ORDER_TRADE_TYPE = "order_trade_type"
        internal const val EXTRA_CURRENCY_MARKET = "currency_market"
        internal const val EXTRA_CURRENCY_MARKET_SUMMARY = "currency_market_summary"
        internal const val EXTRA_USER = "user"

        internal const val SAVED_STATE_ORDER_TRADE_TYPE = "order_trade_type"
        internal const val SAVED_STATE_CURRENCY_MARKET = "currency_market"
        internal const val SAVED_STATE_CURRENCY_MARKET_SUMMARY = "currency_market_summary"
        internal const val SAVED_STATE_USER = "user"

        private const val INPUT_MAX_LENGTH = 10

    }


    /**
     * An order trade type used for determining whether the user
     * is trying to buy or sell.
     */
    protected lateinit var mOrderTradeType: OrderTradeTypes

    /**
     * A currency market the user wishes to exchange.
     */
    protected lateinit var mCurrencyMarket: CurrencyMarket

    /**
     * A summary about the currency market.
     */
    protected lateinit var mCurrencyMarketSummary: CurrencyMarketSummary

    /**
     * A signed-in user.
     */
    protected lateinit var mUser: User




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.horizontal_sliding_right_to_left_enter_animation,
            R.anim.default_window_exit_animation
        )
    }


    override fun init() {
        initToolbar()
        initOptionButtons()
        initAmountInput()
        initAtPriceInput()
        initDetailsContainer()
        initTradeButton()
    }


    private fun initToolbar() {
        returnBackBtnIv.setOnClickListener { onBackPressed() }

        toolbar.titleTv.setText(R.string.trade)
        toolbar.progressBar.setColor(R.color.colorProgressBar)
    }


    private fun initOptionButtons() {
        userCurrencyOb.setTitleText(mCurrencyMarketSummary.currencyName)
        userMarketOb.setTitleText(mCurrencyMarketSummary.marketName)

        updateBalance()
    }


    private fun initAmountInput() {
        amountOet.setInputContainerColor(getCompatColor(R.color.colorOptionEditTextInput))
        amountOet.setInputCursorDrawable(getColoredCompatDrawable(
            R.drawable.edit_text_cursor_drawable,
            R.color.colorPrimaryText
        ))
        amountOet.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        amountOet.setInputFilters(arrayOf(InputFilter.LengthFilter(INPUT_MAX_LENGTH)))
        amountOet.setInputHint(getString(R.string.trade_activity_amount_edit_text_hint, getMinOrderAmount()))
        amountOet.setLabelText(mCurrencyMarket.currency)
        amountOet.setKeyListener(getDecimalNumberKeyListener(DoubleFormatter.getInstance(getLocale()).getDecimalSeparator()))
        amountOet.addTextChangedListener(object : TextWatcherAdapter {

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                mPresenter?.onAmountInputChanged()
            }

        })
    }


    private fun initAtPriceInput() {
        val atPrice = getAtPrice()
        val atPriceString = DoubleFormatter.getInstance(getLocale()).formatPrice(
            if(atPrice == 0.0) mCurrencyMarket.lastPrice else atPrice
        )

        atPriceOet.setInputContainerColor(getCompatColor(R.color.colorOptionEditTextInput))
        atPriceOet.setInputCursorDrawable(getColoredCompatDrawable(
            R.drawable.edit_text_cursor_drawable,
            R.color.colorPrimaryText
        ))
        atPriceOet.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        atPriceOet.setInputFilters(arrayOf(InputFilter.LengthFilter(INPUT_MAX_LENGTH)))

        if(atPrice != 0.0) {
            atPriceOet.setInputHint(String.format(
                "%s: %s",
                getString(getAtPriceInputHintStringRes()),
                atPriceString
            ))
        } else {
            atPriceOet.setInputHint(String.format(
                "%s: %s",
                getString(R.string.last_price),
                atPriceString
            ))
        }
        atPriceOet.setInputText(atPriceString)
        atPriceOet.setLabelText(mCurrencyMarket.market)
        atPriceOet.setKeyListener(getDecimalNumberKeyListener(DoubleFormatter.getInstance(getLocale()).getDecimalSeparator()))
        atPriceOet.addTextChangedListener(object : TextWatcherAdapter {

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                mPresenter?.onAtPriceInputChanged()
            }

        })
    }


    /**
     * Should return at price value.
     */
    abstract fun getAtPrice(): Double


    /**
     * Should return at price EditText hint string resource.
     */
    abstract fun getAtPriceInputHintStringRes(): Int


    private fun initDetailsContainer() {
        updateTradeDetails(getAmountInput(), getAtPriceInput())
    }


    private fun initTradeButton() {
        tradeBtn.text = getTradeButtonText()
        tradeBtn.background = getTradeButtonBackground()
        tradeBtn.setOnClickListener {
            mPresenter?.onTradeButtonClicked()
        }

        disableTradeButton()
    }


    /**
     * Should return text for the trade button.
     */
    abstract fun getTradeButtonText(): String


    /**
     * Should return background for the trade button.
     */
    abstract fun getTradeButtonBackground(): Drawable


    override fun showProgressBar() {
        toolbar.progressBar.makeVisible()
    }


    override fun hideProgressBar() {
        toolbar.progressBar.makeGone()
    }


    override fun enableTradeButton() {
        tradeBtn.enable(true)
    }


    override fun disableTradeButton() {
        tradeBtn.disable(true)
    }


    override fun setAmountInputError(error: String) {
        amountOet.setErrorText(error)
    }


    override fun setAtPriceInputError(error: String) {
        atPriceOet.setErrorText(error)

    }


    override fun updateUserFunds(funds: Map<String, String>) {
        mUser = mUser.copy(funds = funds)
    }


    override fun updateBalance() {
        val formatter = DoubleFormatter.getInstance(getLocale())

        userCurrencyOb.setSubtitleText(formatter.formatBalance(getUserCurrencyBalance()))
        userMarketOb.setSubtitleText(formatter.formatBalance(getUserMarketBalance()))
    }


    override fun updateTradeDetails(amount: Double, price: Double) {
        updateTransactionFee(amount, price)
        updateUserDeduction(amount, price)
        updateUserAddition(amount, price)
    }


    private fun updateTransactionFee(amount: Double, price: Double) {
        val transactionFee = calculateTransactionFee(amount, price)
        val doubleFormatter = DoubleFormatter.getInstance(getLocale())

        feeOtv.setValueText(String.format(
            "%s %s (%s)",
            if(transactionFee == 0.0) "0" else doubleFormatter.formatTransactionFee(transactionFee),
            getTransactionFeeCoinName(),
            doubleFormatter.formatFeePercent(getFeePercent())
        ))
    }


    /**
     * Should calculate the transaction fee.
     *
     * @param amount The amount to calculate fee on
     * @param price The price at which the amount is going to be bought
     * or sold
     */
    abstract fun calculateTransactionFee(amount: Double, price: Double): Double


    /**
     * Should return a name of the coin for the transaction fee.
     */
    abstract fun getTransactionFeeCoinName(): String


    /**
     * Should return a percent fee for the transaction.
     */
    abstract fun getFeePercent(): Double


    private fun updateUserDeduction(amount: Double, price: Double) {
        val userDeduction = calculateUserDeduction(amount, price)
        val doubleFormatter = DoubleFormatter.getInstance(getLocale())

        userDeductionOtv.setValueText(String.format(
            "%s %s",
            if(userDeduction == 0.0) "0" else doubleFormatter.formatAmount(userDeduction),
            getUserDeductionCoinName()
        ))
    }


    /**
     * Should calculate the user deduction.
     *
     * @param amount The amount to calculate user deduction on
     * @param price The price at which the amount is going to be bought
     * or sold
     */
    abstract fun calculateUserDeduction(amount: Double, price: Double): Double


    /**
     * Should return a name of the coin for the user deduction.
     */
    abstract fun getUserDeductionCoinName(): String


    private fun updateUserAddition(amount: Double, price: Double) {
        val userAddition = calculateUserAddition(amount, price)
        val doubleFormatter = DoubleFormatter.getInstance(getLocale())

        userAdditionOtv.setValueText(String.format(
            "%s %s",
            if(userAddition == 0.0) "0" else doubleFormatter.formatAmount(userAddition),
            getUserAdditionCoinName()
        ))
    }


    /**
     * Should calculate the user addition.
     *
     * @param amount The amount to calculate user addition on
     * @param price The price at which the amount is going to be bought
     * or sold
     */
    abstract fun calculateUserAddition(amount: Double, price: Double): Double


    /**
     * Should return a name of the coin for the user addition.
     */
    abstract fun getUserAdditionCoinName(): String


    override fun getMinOrderAmount(): String {
        return DoubleFormatter.getInstance(getLocale()).formatMinOrderAmount(mCurrencyMarket.minOrderAmount)
    }


    override fun getAmountInput(): Double {
        return amountOet.getInputText().convertToDouble()
    }


    override fun getAtPriceInput(): Double {
        return atPriceOet.getInputText().convertToDouble()
    }


    override fun getUserCurrencyBalance(): Double {
        return mUser.funds.getWithDefault(mCurrencyMarket.currency, "0").convertToDouble()
    }


    override fun getUserMarketBalance(): Double {
        return mUser.funds.getWithDefault(mCurrencyMarket.market, "0").convertToDouble()
    }


    override fun getOrderTradeType(): OrderTradeTypes {
        return mOrderTradeType
    }


    override fun getCurrencyMarket(): CurrencyMarket {
        return mCurrencyMarket
    }


    override fun getCurrencyMarketSummary(): CurrencyMarketSummary {
        return mCurrencyMarketSummary
    }


    override fun getLocale(): Locale {
        return ctx.getLocale()
    }


    override fun getContentLayoutResourceId(): Int = R.layout.trade_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.default_window_enter_animation,
            R.anim.horizontal_sliding_right_to_left_exit_animation
        )
    }


    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        if(savedState != null) {
            mOrderTradeType = (savedState.getSerializable(SAVED_STATE_ORDER_TRADE_TYPE) as OrderTradeTypes)
            mCurrencyMarket = (savedState.getSerializable(SAVED_STATE_CURRENCY_MARKET) as CurrencyMarket)
            mCurrencyMarketSummary = (savedState.getParcelable(SAVED_STATE_CURRENCY_MARKET_SUMMARY))
            mUser = savedState.getParcelable(SAVED_STATE_USER)
        } else {
            mOrderTradeType = (intent?.getSerializableExtra(EXTRA_ORDER_TRADE_TYPE) as OrderTradeTypes)
            mCurrencyMarket = (intent?.getSerializableExtra(EXTRA_CURRENCY_MARKET) as CurrencyMarket)
            mCurrencyMarketSummary = (intent?.getParcelableExtra(EXTRA_CURRENCY_MARKET_SUMMARY) as CurrencyMarketSummary)
            mUser = (intent?.getParcelableExtra(EXTRA_USER) as User)
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putSerializable(SAVED_STATE_ORDER_TRADE_TYPE, mOrderTradeType)
        savedState.putSerializable(SAVED_STATE_CURRENCY_MARKET, mCurrencyMarket)
        savedState.putParcelable(SAVED_STATE_CURRENCY_MARKET_SUMMARY, mCurrencyMarketSummary)
        savedState.putParcelable(SAVED_STATE_USER, mUser)
    }


}