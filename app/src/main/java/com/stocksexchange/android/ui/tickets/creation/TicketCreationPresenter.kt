package com.stocksexchange.android.ui.tickets.creation

import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.TicketCategories
import com.stocksexchange.android.api.model.TicketCreationResponse
import com.stocksexchange.android.utils.exceptions.NoInternetException
import com.stocksexchange.android.model.HttpCodes.*
import com.stocksexchange.android.model.TicketCreationParameters
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.android.events.TicketEvent
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import org.greenrobot.eventbus.EventBus
import org.koin.standalone.inject
import retrofit2.HttpException

class TicketCreationPresenter(
    model: TicketCreationModel,
    view: TicketCreationContract.View
) : BasePresenter<TicketCreationModel, TicketCreationContract.View>(model, view), TicketCreationContract.ActionListener,
    TicketCreationModel.ActionListener {


    private val stringProvider: StringProvider by inject()




    init {
        model.setActionListener(this)
    }


    constructor(view: TicketCreationContract.View): this(TicketCreationModel(), view)


    override fun stop() {
        super.stop()

        mView.dismissCategoryDialog()
    }


    override fun onActionButtonClicked() {
        if(mModel.isRequestFired) {
            return
        }

        val category = when(mView.getCategoryButtonText()) {
            stringProvider.getString(R.string.orders) -> TicketCategories.ORDER_PROBLEM
            stringProvider.getString(R.string.deposits) -> TicketCategories.DEPOSIT_PROBLEM
            stringProvider.getString(R.string.withdrawals) -> TicketCategories.WITHDRAWAL_PROBLEM
            stringProvider.getString(R.string.dividends) -> TicketCategories.DIVIDEND_PROBLEM

            else -> TicketCategories.OTHER
        }

        //todo
        val params = TicketCreationParameters(
            category,
            if(category == TicketCategories.OTHER) null else "BTC",
            mView.getSubject(),
            mView.getMessage()
        )

        mModel.performTicketCreationRequest(params)
    }


    override fun onTicketCreationRequestSent() {
        mView.showProgressBar()
    }


    override fun onTicketCreationResponseReceived() {
        mView.hideProgressBar()
    }


    override fun onTicketCreationRequestSucceeded(response: TicketCreationResponse) {
        EventBus.getDefault().postSticky(TicketEvent.createTicket(this))

        mView.finishActivity()
    }


    override fun onTicketCreationRequestFailed(error: Throwable) {
        mView.showToast(when(error) {
            is NoInternetException -> stringProvider.getNetworkCheckMessage()
            is HttpException -> {
                when(error.code()) {
                    TOO_MANY_REQUESTS.code -> stringProvider.getTooManyRequestsMessage()
                    in INTERNAL_SERVER_ERROR.code..NETWORK_CONNECT_TIMEOUT.code -> stringProvider.getServerUnresponsiveMessage()

                    else -> stringProvider.getSomethingWentWrongMessage()
                }
            }

            else -> stringProvider.getSomethingWentWrongMessage()
        })
    }


    override fun onSubjectEtTextChanged(text: String) {
        mView.updateSendButtonState()
    }


    override fun onCategoryButtonClicked() {
        mView.showCategoryDialog()
    }


    override fun onCategoryPicked(category: String) {
        mView.updateCategoryButtonText(when(category) {

            stringProvider.getString(R.string.ticket_category_order_problem) -> {
                stringProvider.getString(R.string.orders)
            }

            stringProvider.getString(R.string.ticket_category_deposit_problem) -> {
                stringProvider.getString(R.string.deposits)
            }

            stringProvider.getString(R.string.ticket_category_withdrawal_problem) -> {
                stringProvider.getString(R.string.withdrawals)
            }

            stringProvider.getString(R.string.ticket_category_dividend_problem) -> {
                stringProvider.getString(R.string.dividends)
            }

            else -> {
                stringProvider.getString(R.string.other)
            }

        })

        mView.updateSendButtonState()
    }


    override fun onScrollViewClicked() {
        mView.requestMessageEtFocus()
        mView.showKeyboard()
    }


    override fun onMessageEtTextChanged(text: String) {
        mView.updateSendButtonState()
    }


}