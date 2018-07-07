package com.stocksexchange.android.utils.helpers

import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.Result

/**
 * A helper function for handling the results of the repository.
 *
 * @param result The result to check and handle
 * @param onSuccess The callback to invoke if the result is successful
 * @param onFailure The callback to invoke if the result is unsuccessful
 *
 * @return true if the result is successful; false otherwise
 */
suspend fun <T : Any> handleRepositoryResult(
    result: RepositoryResult<T>,
    onSuccess: suspend ((Result.Success<T>) -> Unit) = {},
    onFailure: suspend ((Result.Failure) -> Unit) = {}
): Boolean {
    return if(result.isSuccessful()) {
        onSuccess(result.getSuccessfulResult())
        true
    } else {
        onFailure(result.getErroneousResult())
        false
    }
}