package com.stocksexchange.android.repositories.users

import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.repositories.BaseRepositoryCache

class UsersCache : BaseRepositoryCache() {


    companion object {

        private const val KEY_SIGNED_IN_USER = "signed_in_user"
    }




    fun saveSignedInUser(signedInUser: User) {
        cache.put(KEY_SIGNED_IN_USER, signedInUser)
    }


    fun removeSignedInUser(){
        cache.remove(KEY_SIGNED_IN_USER)
    }


    fun getSignedInUser(): User {
        return (cache.get(KEY_SIGNED_IN_USER) as User)
    }


    fun hasSignedInUser(): Boolean {
        return cache.contains(KEY_SIGNED_IN_USER)
    }


}