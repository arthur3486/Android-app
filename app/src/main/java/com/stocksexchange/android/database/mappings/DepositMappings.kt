package com.stocksexchange.android.database.mappings

import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.database.model.DatabaseDeposit

fun Deposit.mapToDatabaseDeposit(): DatabaseDeposit {
    return DatabaseDeposit(
        currency = currency,
        address = address,
        publicKey = publicKey,
        paymentId = paymentId
    )
}


fun DatabaseDeposit.mapToDeposit(): Deposit {
    return Deposit(
        currency = currency,
        address = address,
        publicKey = publicKey,
        paymentId = paymentId
    )
}