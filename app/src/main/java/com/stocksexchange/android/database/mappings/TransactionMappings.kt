package com.stocksexchange.android.database.mappings

import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.database.model.DatabaseTransaction

fun Transaction.mapToDatabaseTransaction(): DatabaseTransaction {
    return DatabaseTransaction(
        id = id,
        transactionId = transactionId,
        type = type,
        currency = currency,
        status = status,
        amount = amount,
        fee = fee,
        address = address,
        timestamp = timestamp
    )
}


fun List<Transaction>.mapToDatabaseTransactionList(): List<DatabaseTransaction> {
    return map { it.mapToDatabaseTransaction() }
}


fun DatabaseTransaction.mapToTransaction(): Transaction {
    return Transaction(
        id = id,
        transactionId = transactionId,
        type = type,
        currency = currency,
        status = status,
        amount = amount,
        fee = fee,
        address = address,
        timestamp = timestamp
    )
}


fun List<DatabaseTransaction>.mapToTransactionList(): List<Transaction> {
    return map { it.mapToTransaction() }
}


fun Map<String, Map<Long, Transaction>>.mapToTransactionList(type: String): List<Transaction> {
    val list: MutableList<Transaction> = mutableListOf()

    map { (_, transactionMap) ->
        transactionMap.map { (id, transaction) ->
            transaction.id = id
            transaction.type = type

            list.add(transaction)
        }
    }

    return list
}