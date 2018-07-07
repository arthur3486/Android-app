package com.stocksexchange.android.api.model

/**
 * An enumeration of all possible transaction status types.
 */
enum class TransactionStatuses(val value: String) {

    ALL("all"),

    // General
    FINISHED("finished"),

    // Deposits related
    AWAITING_CONFIRMATIONS("awaiting_confirmations"),

    // Withdrawals related
    EMAIL_SENT("email sent"),
    CANCELLED_BY_USER("canceled by user"),
    AWAITING_APPROVAL("awaiting approval"),
    APPROVED("approved"),
    NOT_CONFIRMED("not confirmed"),
    PROCESSING("processing"),
    WITHDRAWAL_ERROR("withdrawal error"),
    CANCELLED_BY_ADMIN("canceled by admin")

}