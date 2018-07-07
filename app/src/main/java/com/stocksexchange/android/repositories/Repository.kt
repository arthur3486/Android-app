package com.stocksexchange.android.repositories

/**
 * An interface for implementing a repository.
 */
interface Repository {

    /**
     * Refreshes the repository in order to load fresh
     * data on subsequent requests.
     */
    fun refresh()

}