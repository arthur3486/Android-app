package com.stocksexchange.android.repositories.users

import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.datastores.users.UsersDataStore
import com.stocksexchange.android.datastores.users.UsersDatabaseDataStore
import com.stocksexchange.android.utils.exceptions.NoInternetException
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.providers.ConnectionProvider
import com.stocksexchange.android.utils.handlers.PreferenceHandler
import com.stocksexchange.android.repositories.BaseRepository
import com.stocksexchange.android.utils.helpers.handleRepositoryResult

class UsersRepositoryImpl(
        private val serverDataStore: UsersDataStore,
        private val databaseDataStore: UsersDatabaseDataStore,
        private val preferenceHandler: PreferenceHandler,
        private val connectionProvider: ConnectionProvider
) : BaseRepository<UsersCache>(), UsersRepository {


    override val cache: UsersCache = UsersCache()




    override suspend fun saveSignedInUser(signedInUser: User) {
        databaseDataStore.saveSignedInUser(signedInUser)
        saveSignedInUserToCache(signedInUser)
    }


    override suspend fun deleteSignedInUser() {
        databaseDataStore.deleteSignedInUser()

        if(cache.hasSignedInUser()) {
            cache.removeSignedInUser()
        }

        preferenceHandler.removeUserName()
    }


    override suspend fun getSignedInUser(): RepositoryResult<User> {
        val result = RepositoryResult<User>()

        if(!cache.hasSignedInUser() || cache.isInvalidated) {
            val onSuccess: suspend ((Result.Success<User>) -> Unit)

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.getSignedInUser()
                onSuccess = {
                    saveSignedInUser(it.value)
                    saveSignedInUserToPreferences(it.value)
                }
            } else {
                result.serverResult = Result.Failure(NoInternetException())

                result.databaseResult = databaseDataStore.getSignedInUser()
                onSuccess = { saveSignedInUserToCache(it.value) }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getSignedInUser())
        }

        return result
    }


    override suspend fun hasSignedInUser(): Boolean {
        return (databaseDataStore.getSignedInUser() is Result.Success)
    }


    private fun saveSignedInUserToCache(user: User) {
        cache.saveSignedInUser(user)
    }


    private fun saveSignedInUserToPreferences(user: User) {
        preferenceHandler.saveUserName(user.userName)
    }


}