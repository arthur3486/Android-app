package com.stocksexchange.android.ui.utils

import com.google.gson.Gson
import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.api.model.SocketResponseTypes
import com.stocksexchange.android.api.utils.fromJson
import com.stocksexchange.android.model.HttpCodes
import com.stocksexchange.android.utils.helpers.fromJsonStringToJsonObject
import com.stocksexchange.android.events.CurrencyMarketSocketEvent
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber
import java.io.EOFException

/**
 * Contains socket-related functionality and a few helper methods.
 */
class SocketConnection : KoinComponent {


    companion object {

        private const val MAX_RECONNECTION_ATTEMPTS = 3

    }


    /**
     * An integer holding a number of reconnection attempts that
     * has been performed.
     */
    private var reconnectionAttempts: Int = 0

    /**
     * A status of the connection.
     */
    private var status: Statuses = Statuses.INVALID

    /**
     * An instance of [Gson].
     */
    private val gson: Gson by inject()

    /**
     * An instance of [OkHttpClient].
     */
    private val client: OkHttpClient by inject()

    /**
     * A currently opened web socket.
     */
    private var webSocket: WebSocket? = null




    /**
     * An enumeration of all possible statuses a web socket can have.
     */
    enum class Statuses {

        INVALID,
        SENT_CONNECTION_REQUEST,
        OPENED,
        CLOSING,
        CLOSED,
        FAILED

    }


    /**
     * Tries to establish a web-socket connection. Does nothing
     * if the connection is already established.
     */
    fun connect() {
        if((status == Statuses.OPENED) || (status == Statuses.SENT_CONNECTION_REQUEST)) {
            return
        }

        webSocket = client.newWebSocket(
            Request.Builder().url(BuildConfig.STOCKS_EXCHANGE_SOCKET_URL).build(),
            listener
        )
        status = Statuses.SENT_CONNECTION_REQUEST

        Timber.i("Sent a socket connection request.")
    }


    /**
     * Closes a web-socket connection. Does nothing if the status
     * of the connection is not opened.
     */
    fun disconnect() {
        if(status != Statuses.OPENED) {
            return
        }

        webSocket?.cancel()

        Timber.i("Closed a socket connection.")
    }


    private fun onMessageArrived(json: String) {
        val jsonObject = fromJsonStringToJsonObject(json)

        if((jsonObject == null) || !jsonObject.has("type")) {
            return
        }

        when(jsonObject.get("type").asString) {

            SocketResponseTypes.LAST_PRICE_AND_VOLUME.type -> {
                EventBus.getDefault().post(CurrencyMarketSocketEvent.newInstance(
                    gson.fromJson(json), this
                ))
            }

        }
    }


    private fun onErrorArrived(throwable: Throwable, response: Response?) {
        // Reconnecting on origin timeout error and EOFException
        if((reconnectionAttempts != MAX_RECONNECTION_ATTEMPTS) &&
            (((response != null) && (response.code() == HttpCodes.ORIGIN_TIMEOUT.code)) || (throwable is EOFException))) {
            connect()
            reconnectionAttempts++
        }
    }


    private val listener: WebSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Timber.i("onOpen(response: $response)")
            status = Statuses.OPENED
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Timber.i("onMessage(text: $text)")
            onMessageArrived(text)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Timber.i("onClosing(code: $code, reason: $reason)")
            status = Statuses.CLOSING
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Timber.i("onClosed(webSocket: $webSocket, code: $code, reason: $reason)")
            status = Statuses.CLOSED
        }

        override fun onFailure(webSocket: WebSocket, throwable: Throwable, response: Response?) {
            Timber.i("onFailure(throwable: $throwable, response: $response)")
            status = Statuses.FAILED
            onErrorArrived(throwable, response)
        }

    }


}