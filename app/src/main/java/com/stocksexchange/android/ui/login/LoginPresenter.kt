package com.stocksexchange.android.ui.login

import com.stocksexchange.android.R
import com.stocksexchange.android.utils.exceptions.NoInternetException
import com.stocksexchange.android.utils.handlers.CredentialsHandler
import com.stocksexchange.android.model.HttpCodes.*
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.android.utils.handlers.QrCodeHandler
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import org.koin.standalone.inject
import retrofit2.HttpException

class LoginPresenter(
    model: LoginModel,
    view: LoginContract.View
) : BasePresenter<LoginModel, LoginContract.View>(model, view), LoginContract.ActionListener, LoginModel.ActionListener {


    private val mStringProvider: StringProvider by inject()
    private val mCredentialsHandler: CredentialsHandler by inject()
    private val mQrCodeHandler: QrCodeHandler by inject()




    init {
        model.setActionListener(this)
    }


    constructor(view: LoginContract.View): this(LoginModel(), view)


    override fun onQrCodeScanned(qrCode: String) {
        if(mModel.mIsRequestFired) {
            return
        }

        if(mQrCodeHandler.isQrCodeValid(qrCode)) {
            saveQrCode(qrCode)

            mModel.performSignInRequest()
        } else {
            mView.showToast(mStringProvider.getString(R.string.error_malformed_qr_code))
        }
    }


    private fun saveQrCode(qrCode: String) {
        val keys = mQrCodeHandler.parseQrCode(qrCode)

        if(keys != null) {
            mCredentialsHandler.saveKeys(keys)
        }
    }


    override fun onSignInRequestSent() {
        mView.showProgress()
        mView.disableQrCodeScanner()
    }


    override fun onSignInResponseReceived() {
        mView.hideProgress()
        mView.enableQrCodeScanner()
    }


    override fun onSignInRequestSucceeded() {
        mView.launchDashboard()
    }


    override fun onSignInRequestFailed(error: Throwable) {
        mView.showToast(when(error) {
            is NoInternetException -> mStringProvider.getNetworkCheckMessage()
            is HttpException -> {
                when(error.code()) {
                    NOT_FOUND.code -> mStringProvider.getUserNotFoundMessage()
                    TOO_MANY_REQUESTS.code -> mStringProvider.getTooManyRequestsMessage()
                    in INTERNAL_SERVER_ERROR.code..NETWORK_CONNECT_TIMEOUT.code -> mStringProvider.getServerUnresponsiveMessage()

                    else -> mStringProvider.getSomethingWentWrongMessage()
                }
            }

            else -> mStringProvider.getSomethingWentWrongMessage()
        })
    }


}
