package com.stocksexchange.android.model

/**
 * A model class holding different repository results.
 */
data class RepositoryResult<T : Any>(
    var cacheResult: Result<T>? = null,
    var serverResult: Result<T>? = null,
    var databaseResult: Result<T>? = null
) {


    /**
     * Checks every result and returns the successful one.
     *
     * @return The successful result
     */
    fun getSuccessfulResult(): Result.Success<T> {
        return when {
            isCacheResultSuccessful() -> (cacheResult as Result.Success<T>)
            isServerResultSuccessful() -> (serverResult as Result.Success<T>)
            else -> (databaseResult as Result.Success<T>)
        }
    }


    /**
     * Checks every result and returns the erroneous one.
     *
     * @return The erroneous one
     */
    fun getErroneousResult(): Result.Failure {
        return when {
            isCacheResultErroneous() -> (cacheResult as Result.Failure)
            isServerResultErroneous() -> (serverResult as Result.Failure)
            else -> (databaseResult as Result.Failure)
        }
    }


    /**
     * Checks whether the cache result is successful.
     *
     * @return true if successful; false otherwise
     */
    fun isCacheResultSuccessful(): Boolean {
        return (cacheResult is Result.Success)
    }


    /**
     * Checks whether the cache result is erroneous.
     *
     * @param checkNullability Whether to account for nullability check. Default is false.
     *
     * @return true if erroneous; false otherwise
     */
    fun isCacheResultErroneous(checkNullability: Boolean = false): Boolean {
        return ((checkNullability && (cacheResult == null)) || (cacheResult is Result.Failure))
    }


    /**
     * Checks whether the server result is successful.
     *
     * @return true if successful; false otherwise
     */
    fun isServerResultSuccessful(): Boolean {
        return (serverResult is Result.Success)
    }


    /**
     * Checks whether the server result is erroneous.
     *
     * @param checkNullability Whether to account for nullability check. Default is false.
     *
     * @return true if erroneous; false otherwise
     */
    fun isServerResultErroneous(checkNullability: Boolean = false): Boolean {
        return ((checkNullability && (serverResult == null)) || (serverResult is Result.Failure))
    }


    /**
     * Checks whether the database result is successful.
     *
     * @return true if successful; false otherwise
     */
    fun isDatabaseResultSuccessful(): Boolean {
        return (databaseResult is Result.Success)
    }


    /**
     * Checks whether the database result is erroneous.
     *
     * @param checkNullability Whether to account for nullability check. Default is false.
     *
     * @return true if erroneous; false otherwise
     */
    fun isDatabaseResultErroneous(checkNullability: Boolean = false): Boolean {
        return ((checkNullability && (databaseResult == null)) || (databaseResult is Result.Failure))
    }


    /**
     * If either cache, server or database result is successful,
     * then this method will return true. Otherwise, return false.
     *
     * @return true if at least one result is successful; false otherwise
     */
    fun isSuccessful(): Boolean {
        return (isCacheResultSuccessful() ||
                isServerResultSuccessful() ||
                isDatabaseResultSuccessful())
    }


    /**
     * If cache, server, and database results are erroneous,
     * then this method will return true. Otherwise, return false.
     *
     * @return true if all results are erroneous; false otherwise
     */
    fun isErroneous(): Boolean {
        return (isCacheResultErroneous(true) &&
                isServerResultErroneous(true) &&
                isDatabaseResultErroneous(true))
    }


}