package com.stocksexchange.android.database.mappings

import com.stocksexchange.android.api.model.Currency
import com.stocksexchange.android.database.model.DatabaseCurrency

fun Currency.mapToDatabaseCurrency(): DatabaseCurrency {
    return DatabaseCurrency(
        name = name,
        longName = longName,
        minimumWithdrawalAmount = minimumWithdrawalAmount,
        minimumDepositAmount = minimumDepositAmount,
        depositFeeCurrency = depositFeeCurrency,
        depositFee = depositFee,
        withdrawalFeeCurrency = withdrawalFeeCurrency,
        withdrawalFee = withdrawalFee,
        blockExplorerUrl = blockExplorerUrl,
        isActive = isActive
    )
}


fun List<Currency>.mapToDatabaseCurrencyList(): List<DatabaseCurrency> {
    return map { it.mapToDatabaseCurrency() }
}


fun List<Currency>.mapToNameMarketMap(): Map<String, Currency> {
    val map: MutableMap<String, Currency> = mutableMapOf()

    for(currency in this) {
        map[currency.name] = currency
    }

    return map
}


fun DatabaseCurrency.mapToCurrency(): Currency {
    return Currency(
        name = name,
        longName = longName,
        minimumWithdrawalAmount = minimumWithdrawalAmount,
        minimumDepositAmount = minimumDepositAmount,
        depositFeeCurrency = depositFeeCurrency,
        depositFee = depositFee,
        withdrawalFeeCurrency = withdrawalFeeCurrency,
        withdrawalFee = withdrawalFee,
        blockExplorerUrl = blockExplorerUrl,
        isActive = isActive
    )
}


fun List<DatabaseCurrency>.mapToCurrencyList(): List<Currency> {
    return map { it.mapToCurrency() }
}