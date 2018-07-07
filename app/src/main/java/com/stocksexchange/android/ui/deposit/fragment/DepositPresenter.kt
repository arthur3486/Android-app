package com.stocksexchange.android.ui.deposit.fragment

import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.api.exceptions.CurrencyDisabledException
import com.stocksexchange.android.api.exceptions.InvalidCurrencyCodeException
import com.stocksexchange.android.model.DepositParameters
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.android.ui.base.mvp.presenters.BaseDataLoadingPresenter
import com.stocksexchange.android.utils.handlers.ClipboardHandler
import org.koin.standalone.inject

class DepositPresenter(
    model: DepositModel,
    view: DepositContract.View
) : BaseDataLoadingPresenter<DepositModel, DepositContract.View, Deposit, DepositParameters>(model, view),
    DepositContract.ActionListener, DepositModel.ActionListener {


    private val mStringProvider: StringProvider by inject()
    private val mClipboardHandler: ClipboardHandler by inject()




    init {
        model.setActionListener(this)
    }


    constructor(view: DepositContract.View): this(DepositModel(), view)


    override fun stop() {
        super.stop()

        mView.dismissQrDialog()
    }


    override fun getDataLoadingParams(): DepositParameters {
        return DepositParameters(mView.getCurrency())
    }


    override fun onDataLoadingFailed(error: Throwable) {
        super.onDataLoadingFailed(error)

        if(error is InvalidCurrencyCodeException) {
            mView.showToast(mStringProvider.getString(R.string.error_invalid_currency))
        }

        if(error is CurrencyDisabledException) {
            mView.showEmptyView()
            mView.showToast(mStringProvider.getString(R.string.error_currency_disabled))
        }
    }


    override fun onDepositAddressClicked(address: String) {
        copyTextToClipboard(mView.getDeposit()!!.address)
    }


    override fun onDepositAddressLongClicked(address: String) {
        mView.showQrDialog(address, R.string.deposit_address)
    }


    override fun onPublicKeyClicked(publicKey: String) {
        copyTextToClipboard(publicKey)
    }


    override fun onPublicKeyLongClicked(publicKey: String) {
        mView.showQrDialog(publicKey, R.string.public_key)
    }


    override fun onPaymentIdClicked(paymentId: String) {
        copyTextToClipboard(paymentId)
    }


    override fun onPaymentIdLongClicked(paymentId: String) {
        mView.showQrDialog(paymentId, R.string.payment_id)
    }


    override fun onCopyHashButtonClicked(hash: String) {
        copyTextToClipboard(hash)

        mView.dismissQrDialog()
    }


    override fun onSaveQrCodeImageButtonClicked() {
        mView.downloadQrCodeImage()
        mView.dismissQrDialog()
    }


    private fun copyTextToClipboard(text: String) {
        mClipboardHandler.copyText(text)

        mView.showToast(mStringProvider.getString(R.string.copied_to_clipboard))
    }


    override fun toString(): String {
        return "${super.toString()}_${mView.getCurrency()}"
    }


}