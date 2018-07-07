package com.stocksexchange.android.ui.base.mvp.model

import android.support.annotation.CallSuper
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * A base model class to build model classes on.
 */
abstract class BaseModel : Model {


    /**
     * A list of asynchronous jobs currently active.
     */
    protected var mAsyncJobs: MutableList<Job?> = mutableListOf()




    @CallSuper
    override fun start() {
        // Stub
    }


    @CallSuper
    override fun stop() {
        mAsyncJobs.forEach { it?.cancel() }
    }


    /**
     * Runs [block] function parameter off the main thread.
     *
     * @param block The block of code to run asynchronously
     */
    fun performAsync(block: suspend (() -> Unit)) {
        val asyncJob = launch(UI) {
            block()
        }

        mAsyncJobs.add(asyncJob)
    }


    override fun onRestoreState(savedState: MutableMap<String, Any>) {
        // Stub
    }


    override fun onSaveState(savedState: MutableMap<String, Any>) {
        // Stub
    }


}