package com.stocksexchange.android.api.utils

import com.stocksexchange.android.api.model.ApiResponse
import com.stocksexchange.android.api.exceptions.ApiException
import com.stocksexchange.android.model.Result
import ru.gildor.coroutines.retrofit.Result as RetrofitResult

/**
 * Fetches data from the [RetrofitResult] class and packages it
 * inside [Result].
 *
 * @return The data or error packaged inside [Result] class
 */
fun <T : Any> RetrofitResult<T>.fetchData(): Result<T> {
    return when(this) {
        is RetrofitResult.Ok -> Result.Success(value)
        is RetrofitResult.Exception -> Result.Failure(exception)
        is RetrofitResult.Error -> Result.Failure(exception)
    }
}


/**
 * Similar to the extractData(transform) but does not have an intermediate type
 * to convert from.
 *
 * @param error The function to return an appropriate exception in case
 * an error has occurred. By default, returns [ApiException].
 *
 * @return The data or error packaged inside [Result] class
 */
fun <In : ApiResponse<Out>, Out : Any> RetrofitResult<In>.extractDataDirectly(
    error: ((ApiResponse<Out>) -> Throwable) = { ApiException(it.toString()) }
): Result<Out> {
    return extractData(error) { inter -> inter }
}


/**
 * An extension function to extract data from the [RetrofitResult] and package it
 * inside the [Result] class.
 *
 * @param error The function to return an appropriate exception in case
 * an error has occurred. By default, returns [ApiException].
 * @param transform The transformation function to convert data from
 * the intermediate type to the ultimate one
 *
 * @return The data or error packaged inside [Result] class
 */
fun <In : ApiResponse<Inter>, Inter : Any, Out : Any> RetrofitResult<In>.extractData(
        error: ((ApiResponse<Inter>) -> Throwable) = { ApiException(it.toString()) },
        transform: ((Inter) -> Out)
): Result<Out> {
    return when(this) {
        is RetrofitResult.Ok -> value.takeIf { (it.success == 1) && (it.data != null) }
            ?.let { Result.Success(transform(it.data!!)) }
            ?: Result.Failure(error(value))
        is RetrofitResult.Exception -> Result.Failure(exception)
        is RetrofitResult.Error -> Result.Failure(exception)
    }
}