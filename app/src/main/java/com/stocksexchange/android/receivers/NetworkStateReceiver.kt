package com.stocksexchange.android.receivers

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.stocksexchange.android.ui.utils.extensions.isNetworkAvailable

/**
 * A receiver to get notified whenever the network
 * state changes.
 */
class NetworkStateReceiver(
    private val listener: Listener? = null
) : BaseBroadcastReceiver() {


    /**
     * A flag indicating whether we are connected to
     * the network or not.
     */
    private var isConnected: Boolean = false




    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val isNetworkAvailable = context.isNetworkAvailable()

            if(isConnected == isNetworkAvailable) {
                return
            }

            if(isNetworkAvailable) {
                if(!isConnected) {
                    isConnected = true
                    listener?.onConnected()
                }
            } else {
                if(isConnected) {
                    isConnected = false
                    listener?.onDisconnected()
                }
            }
        }
    }


    interface Listener {

        fun onConnected()

        fun onDisconnected()

    }


}