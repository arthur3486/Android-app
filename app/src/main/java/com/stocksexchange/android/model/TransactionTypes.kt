package com.stocksexchange.android.model

import com.stocksexchange.android.api.model.TransactionOperations

/**
 * An enumeration of all possible transaction types.
 */
enum class TransactionTypes {


    DEPOSITS,
    WITHDRAWALS;




    /**
     * Returns a [TransactionOperations] for a type.
     *
     * @return The instance of [TransactionOperations]
     */
    fun getOperation(): TransactionOperations {
        return when(this) {
            DEPOSITS -> TransactionOperations.DEPOSIT
            WITHDRAWALS -> TransactionOperations.WITHDRAWAL
        }
    }


}