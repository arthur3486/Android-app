package com.stocksexchange.android.datastores.users

import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.model.Result

interface UsersDataStore {

    suspend fun saveSignedInUser(signedInUser: User)

    suspend fun deleteSignedInUser()

    suspend fun getSignedInUser(): Result<User>

}