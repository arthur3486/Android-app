package com.stocksexchange.android.ui.utils

import android.app.Activity
import android.app.Application
import android.os.Handler
import com.stocksexchange.android.ui.utils.listeners.adapters.ActivityLifecycleCallbacksAdapter
import timber.log.Timber

/**
 * A manager responsible for observing the status of the application and for notifying
 * whether the application is in foreground or background.
 */
class BackgroundManager(application: Application) : ActivityLifecycleCallbacksAdapter {


    companion object {

        @Volatile
        private var INSTANCE : BackgroundManager? = null

        private const val BACKGROUND_TRANSITION_DELAY = 3000L


        fun getInstance(application: Application): BackgroundManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildBackgroundManager(application).also { INSTANCE = it }
            }
        }


        private fun buildBackgroundManager(application: Application): BackgroundManager {
            return BackgroundManager(application)
        }

    }


    /**
     * A flag indicating whether we are currently in the background or not.
     * Default is true to notify the first time.
     */
    private var isInBackground: Boolean = true

    /**
     * A runnable to execute to transition into a background mode.
     */
    private var backgroundTransitionRunnable: Runnable? = null

    /**
     * A handler used for executing the [backgroundTransitionRunnable].
     */
    private val backgroundDelayHandler: Handler = Handler()

    /**
     * A list of registered listeners to invoke on foreground/background change.
     */
    private val listeners: MutableList<Listener> = mutableListOf()




    init {
        application.registerActivityLifecycleCallbacks(this)
    }


    override fun onActivityResumed(activity: Activity) {
        if(backgroundTransitionRunnable != null) {
            backgroundDelayHandler.removeCallbacks(backgroundTransitionRunnable)
            backgroundTransitionRunnable = null
        }

        if(isInBackground) {
            isInBackground = false
            notifyOnBecameForeground()
        }
    }


    override fun onActivityPaused(activity: Activity) {
        if(!isInBackground && (backgroundTransitionRunnable == null)) {
            backgroundTransitionRunnable = Runnable {
                isInBackground = true
                backgroundTransitionRunnable = null

                notifyOnBecameBackground()
            }
            backgroundDelayHandler.postDelayed(backgroundTransitionRunnable, BACKGROUND_TRANSITION_DELAY)
        }
    }


    private fun notifyOnBecameForeground() {
        Timber.i("Application went to foreground!")

        for(listener in listeners) {
            listener.onBecameForeground()
        }
    }


    private fun notifyOnBecameBackground() {
        Timber.i("Application went to background!")

        for(listener in listeners) {
            listener.onBecameBackground()
        }
    }


    /**
     * Registers a listener to invoke on foreground/background change.
     *
     * @param listener The listener to register
     */
    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }


    /**
     * Unregisters a listener to stop getting notified about foreground/background change.
     *
     * @param listener The listener to unregister
     */
    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }


    /**
     * An interface for notifying about foreground/background changes.
     */
    interface Listener {

        fun onBecameForeground()

        fun onBecameBackground()

    }


}