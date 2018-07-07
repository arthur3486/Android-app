package com.stocksexchange.android

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.stocksexchange.android.utils.CrashReportingTree
import com.stocksexchange.android.ui.utils.BackgroundManager
import com.stocksexchange.android.ui.utils.SocketConnection
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin
import org.koin.log.EmptyLogger
import timber.log.Timber

/**
 * An application controller responsible for maintaining
 * global application state.
 */
class AppController : MultiDexApplication() {


    /**
     * Represents socket connection related functionality.
     */
    private var socketConnection: SocketConnection? = null




    override fun onCreate() {
        super.onCreate()

        initDi()
        initLogger()
        initSocketConnection()
        initBackgroundManager()
        initFirebaseAnalytics()
        initCrashlytics()
    }


    /**
     * Initializes the Koin dependency injection.
     */
    private fun initDi() {
        startKoin(
            application = this,
            modules = applicationModules,
            logger = EmptyLogger()
        )
    }


    /**
     * Initializes the advanced logger.
     */
    private fun initLogger() {
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }


    /**
     * Initializes the web socket connection with a server.
     */
    private fun initSocketConnection() {
        socketConnection = SocketConnection()
    }


    /**
     * Initializes the background manager to get notified
     * about visibility changes.
     */
    private fun initBackgroundManager() {
        val listener = object : BackgroundManager.Listener {

            override fun onBecameForeground() {
                socketConnection?.connect()
            }


            override fun onBecameBackground() {
                socketConnection?.disconnect()
            }

        }

        BackgroundManager.getInstance(this).registerListener(listener)
    }


    /**
     * Initializes the Google analytics.
     */
    private fun initFirebaseAnalytics() {
        FirebaseAnalytics.getInstance(this)
    }


    /**
     * Initializes the Firebase crashlytics.
     */
    private fun initCrashlytics() {
        Fabric.with(this, Crashlytics())
    }


}