package com.stocksexchange.android.database.mappings

import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.database.model.DatabaseUser

fun User.mapToDatabaseUser(): DatabaseUser {
    return DatabaseUser(
        userName = userName,
        email = email,
        hash = hash,
        sessions = sessions,
        funds = funds,
        holdFunds = holdFunds,
        walletAddresses = walletAddresses,
        walletPublicKeys = walletPublicKeys,
        assetsPortfolio = assetsPortfolio,
        openOrders = openOrders
    )
}


fun DatabaseUser.mapToUser(): User {
    return User(
        userName = userName,
        email = email,
        hash = hash,
        sessions = sessions,
        funds = funds,
        holdFunds = holdFunds,
        walletAddresses = walletAddresses,
        walletPublicKeys = walletPublicKeys,
        assetsPortfolio = assetsPortfolio,
        openOrders = openOrders
    )
}