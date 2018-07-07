package com.stocksexchange.android.ui.base.mvp.presenters

import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.CurrencyMarketSummary
import com.stocksexchange.android.api.model.OrderResponse
import com.stocksexchange.android.utils.exceptions.NoInternetException
import com.stocksexchange.android.model.HttpCodes.*
import com.stocksexchange.android.model.OrderCreationParameters
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.android.ui.base.mvp.model.BaseTradeModel
import com.stocksexchange.android.ui.base.mvp.views.TradeView
import com.stocksexchange.android.ui.utils.DoubleFormatter
import org.koin.standalone.inject
import retrofit2.HttpException

/**
 * A base trading presenter to build presenters on.
 */
abstract class BaseTradePresenter<out M, out V>(
    model: M,
    view: V
) : BasePresenter<M, V>(model, view),
    BaseTradeModel.BaseTradeActionListener where
        M : BaseTradeModel<*>,
        V : TradeView {


    protected val mStringProvider: StringProvider by inject()




    open fun onAmountInputChanged() {
        updateTradeDetails()
    }


    open fun onAtPriceInputChanged() {
        updateTradeDetails()
    }


    private fun updateTradeDetails() {
        mView.updateTradeDetails(mView.getAmountInput(), mView.getAtPriceInput())

        val isAmountValidated = validateAmountInput()
        val isAtPriceValidated = validateAtPriceInput()

        if(isAmountValidated && isAtPriceValidated) {
            mView.enableTradeButton()
        } else {
            mView.disableTradeButton()
        }
    }


    private fun validateAmountInput(): Boolean {
        if(mView.getCurrencyMarket().minOrderAmount > mView.getAmountInput()) {
            mView.setAmountInputError(mStringProvider.getString(
                R.string.error_amount_too_small,
                mView.getMinOrderAmount()
            ))

            return false
        }

        if(isUserBalanceInsufficient(mView.getAmountInput())) {
            mView.setAmountInputError(mStringProvider.getString(
                R.string.error_balance_too_small,
                getBalanceTooSmallStringArg(mView.getCurrencyMarketSummary())
            ))

            return false
        }

        mView.setAmountInputError("")
        return true
    }


    abstract fun isUserBalanceInsufficient(amount: Double): Boolean


    abstract fun getBalanceTooSmallStringArg(summary: CurrencyMarketSummary): String


    private fun validateAtPriceInput(): Boolean {
        return if(mView.getAtPriceInput() <= 0.0) {
            mView.setAtPriceInputError(mStringProvider.getString(R.string.error_price_is_not_positive))
            false
        } else {
            mView.setAtPriceInputError("")
            true
        }
    }


    open fun onTradeButtonClicked() {
        if(mModel.mIsRequestFired) {
            return
        }

        sendRequest()
    }


    private fun sendRequest() {
        val formatter = DoubleFormatter.getInstance(mView.getLocale())
        val currentDecimalSeparator = formatter.getDecimalSeparator()
        formatter.setDecimalSeparator('.')

        mModel.performTradeRequest(OrderCreationParameters(
            mView.getCurrencyMarket().name,
            mView.getOrderTradeType().name,
            formatter.formatPrice(mView.getAmountInput()),
            formatter.formatPrice(mView.getAtPriceInput())
        ))

        formatter.setDecimalSeparator(currentDecimalSeparator)
    }


    override fun onTradeRequestSent() {
        mView.showProgressBar()
    }


    override fun onTradeResponseReceived() {
        mView.hideProgressBar()
    }


    override fun onTradeRequestSucceeded(orderResponse: OrderResponse) {
        mView.updateUserFunds(orderResponse.funds)
        mView.updateBalance()
        mView.showToast(mStringProvider.getString(R.string.order_created))
    }


    override fun onTradeRequestFailed(error: Throwable) {
        mView.showToast(when(error) {
            is NoInternetException -> mStringProvider.getNetworkCheckMessage()
            is HttpException -> {
                when(error.code()) {
                    TOO_MANY_REQUESTS.code -> mStringProvider.getTooManyRequestsMessage()
                    in INTERNAL_SERVER_ERROR.code..NETWORK_CONNECT_TIMEOUT.code -> mStringProvider.getServerUnresponsiveMessage()

                    else -> mStringProvider.getSomethingWentWrongMessage()
                }
            }

            else -> mStringProvider.getSomethingWentWrongMessage()
        })
    }


    override fun toString(): String {
        return "${super.toString()}_${mView.getOrderTradeType().name}"
    }


}