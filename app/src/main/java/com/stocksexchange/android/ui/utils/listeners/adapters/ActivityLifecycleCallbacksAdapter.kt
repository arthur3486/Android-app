package com.stocksexchange.android.ui.utils.listeners.adapters

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * An adapter for [Application.ActivityLifecycleCallbacks]. Primarily used for extending.
 */
interface ActivityLifecycleCallbacksAdapter : Application.ActivityLifecycleCallbacks {


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // Stub
    }


    override fun onActivityStarted(activity: Activity) {
        // Stub
    }


    override fun onActivityResumed(activity: Activity) {
        // Stub
    }


    override fun onActivityPaused(activity: Activity) {
        // Stub
    }


    override fun onActivityStopped(activity: Activity) {
        // Stub
    }


    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        // Stub
    }


    override fun onActivityDestroyed(activity: Activity) {
        // Stub
    }


}