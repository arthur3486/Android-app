package com.stocksexchange.android.utils.helpers

import com.stocksexchange.android.model.Result
import org.jetbrains.anko.coroutines.experimental.bg
import timber.log.Timber

/**
 * Executes code inside block function on a background thread
 * and reports any errors if something went wrong.
 *
 * @param onError The listener to get notified whenever an error occurred
 * @param block The block of code to run on background thread
 */
suspend fun executeBackgroundOperation(onError: ((Throwable) -> Unit)? = null, block: (() -> Unit)) {
    try {
        bg { block() }.await()
    } catch(exception: Throwable) {
        Timber.e("An error occurred while executing background operation. Error: $exception")

        onError?.invoke(exception)
    }
}


/**
 * Runs code inside block function on a background thread and returns
 * its result packaged inside [Result] class.
 *
 * @param block The block of code to run which returns a specific result
 *
 * @return The block's result packaged inside [Result] class
 */
suspend fun <T : Any> performBackgroundOperation(block: (() -> T)): Result<T> {
    return try {
        bg { Result.Success(block()) }.await()
    } catch(exception: Throwable) {
        Timber.e("An error occurred while performing background operation. Error: $exception")

        Result.Failure(exception)
    }
}