package com.stocksexchange.android.datastores.users

import com.stocksexchange.android.api.StocksExchangeService
import com.stocksexchange.android.api.exceptions.InvalidCredentialsException
import com.stocksexchange.android.api.model.PrivateApiMethods
import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.api.utils.extractDataDirectly
import com.stocksexchange.android.api.utils.generateNonce
import com.stocksexchange.android.utils.handlers.CredentialsHandler
import com.stocksexchange.android.model.Result
import ru.gildor.coroutines.retrofit.awaitResult
import ru.gildor.coroutines.retrofit.Result as RetrofitResult

class UsersServerDataStore(
    private val stocksExchangeService: StocksExchangeService,
    private val credentialsHandler: CredentialsHandler
) : UsersDataStore {


    override suspend fun saveSignedInUser(signedInUser: User) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteSignedInUser() {
        throw UnsupportedOperationException()
    }


    override suspend fun getSignedInUser(): Result<User> {
        return if(credentialsHandler.hasValidCredentials()) {
            stocksExchangeService.getUser(
                PrivateApiMethods.GET_INFO.methodName,
                generateNonce()
            ).awaitResult().extractDataDirectly()
        } else {
            Result.Failure(InvalidCredentialsException())
        }
    }


}