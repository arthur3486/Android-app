package com.stocksexchange.android.datastores.users

import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.database.daos.UserDao
import com.stocksexchange.android.datastores.exceptions.InvalidUserNameException
import com.stocksexchange.android.datastores.exceptions.UserNotFoundException
import com.stocksexchange.android.database.mappings.mapToDatabaseUser
import com.stocksexchange.android.database.mappings.mapToUser
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.handlers.PreferenceHandler
import com.stocksexchange.android.utils.helpers.executeBackgroundOperation
import com.stocksexchange.android.utils.helpers.performBackgroundOperation

class UsersDatabaseDataStore(
    private val usersDao: UserDao,
    private val preferenceHandler: PreferenceHandler
) : UsersDataStore {


    override suspend fun saveSignedInUser(signedInUser: User) {
        executeBackgroundOperation {
            usersDao.insert(signedInUser.mapToDatabaseUser())
        }
    }


    override suspend fun deleteSignedInUser() {
        executeBackgroundOperation {
            val userName = preferenceHandler.getUserName()

            if(userName.isBlank()) {
                return@executeBackgroundOperation
            }

            usersDao.deleteUser(userName)
        }
    }


    override suspend fun getSignedInUser(): Result<User> {
        return performBackgroundOperation {
            val userName = preferenceHandler.getUserName()

            if(userName.isBlank()) {
                throw InvalidUserNameException(userName)
            }

            usersDao.get(userName)?.mapToUser() ?: throw UserNotFoundException()
        }
    }


}