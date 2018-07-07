package com.stocksexchange.android.api.model

/**
 * An enumeration of all possible private API methods.
 */
enum class PrivateApiMethods(val methodName: String) {

    GET_INFO("GetInfo"),
    ACTIVE_ORDERS("ActiveOrders"),
    CANCEL_ORDER("CancelOrder"),
    TRADE("Trade"),
    TRADE_HISTORY("TradeHistory"),
    TRANSACTION_HISTORY("TransHistory"),
    GRAPH("Graph"),
    DEPOSIT("Deposit"),
    WITHDRAW("Withdraw"),
    GENERATE_WALLET("GenerateWallets"),
    CREATE_TICKET("Ticket"),
    REPLY_TO_TICKET("ReplyTicket"),
    GET_TICKETS("GetTickets")

}