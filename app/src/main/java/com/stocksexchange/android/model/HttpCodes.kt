package com.stocksexchange.android.model

/**
 * An enumeration of all relevant HTTP codes used by this app.
 */
enum class HttpCodes(val code: Int) {

    NOT_FOUND(404),
    TOO_MANY_REQUESTS(429),

    INTERNAL_SERVER_ERROR(500),
    ORIGIN_TIMEOUT(524),
    NETWORK_CONNECT_TIMEOUT(599);

}