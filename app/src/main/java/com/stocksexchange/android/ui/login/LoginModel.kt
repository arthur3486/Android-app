package com.stocksexchange.android.ui.login

import com.stocksexchange.android.repositories.users.UsersRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import org.koin.standalone.inject
import timber.log.Timber

class LoginModel : BaseModel() {


    var mIsRequestFired: Boolean = false
        private set

    private var mActionListener: ActionListener? = null

    private val mUsersRepository: UsersRepository by inject()




    fun performSignInRequest() {
        performSignInRequestAsync()
        onSignInRequestSent()
    }


    private fun performSignInRequestAsync() {
        performAsync {
            val result = mUsersRepository.getSignedInUser()

            Timber.i("usersRepository.getSignedInUser() = $result")

            if(result.isSuccessful()) {
                handleSuccessfulSignInResponse()
            } else {
                handleUnsuccessfulSignInResponse(result.getErroneousResult().exception)
            }
        }
    }

    private fun handleSuccessfulSignInResponse() {
        onSignInResponseReceived()

        mActionListener?.onSignInRequestSucceeded()
    }


    private fun handleUnsuccessfulSignInResponse(error: Throwable) {
        onSignInResponseReceived()

        mActionListener?.onSignInRequestFailed(error)
    }


    private fun onSignInRequestSent() {
        mIsRequestFired = true

        mActionListener?.onSignInRequestSent()
    }


    private fun onSignInResponseReceived() {
        mIsRequestFired = false

        mActionListener?.onSignInResponseReceived()
    }


    fun setActionListener(listener: ActionListener) {
        mActionListener = listener
    }


    interface ActionListener {

        fun onSignInRequestSent()

        fun onSignInResponseReceived()

        fun onSignInRequestSucceeded()

        fun onSignInRequestFailed(error: Throwable)

    }


}