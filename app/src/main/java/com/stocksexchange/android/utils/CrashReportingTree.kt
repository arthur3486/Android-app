package com.stocksexchange.android.utils

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.stocksexchange.android.datastores.exceptions.NotFoundException
import timber.log.Timber

/**
 * A [Timber] tree used for reporting in-app errors to the Crashlytics library.
 */
class CrashReportingTree : Timber.Tree() {


    override fun log(priority: Int, tag: String?, message: String, error: Throwable?) {
        if((priority == Log.VERBOSE) ||
            (priority == Log.DEBUG) ||
            (priority == Log.ASSERT) ||
            (error is NotFoundException)) {
            return
        }

        if(error != null) {
            Crashlytics.logException(error)
        }
    }


}