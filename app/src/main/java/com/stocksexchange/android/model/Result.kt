package com.stocksexchange.android.model

/**
 * Sealed class for data store results.
 */
@Suppress("unused")
sealed class Result<out T : Any> {


    /**
     * Successful result.
     */
    class Success<out T : Any>(
        val value: T
    ) : Result<T>() {

        override fun toString(): String {
            return value.toString()
        }

    }


    /**
     * Erroneous result.
     */
    class Failure(
        val exception: Throwable
    ) : Result<Nothing>() {

        override fun toString(): String {
            return exception.toString()
        }

    }


}