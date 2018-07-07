package com.stocksexchange.android.repositories

/**
 * An interface for representing a repository cache.
 */
interface RepositoryCache {

    /**
     * Validates the cache and forces the repositories
     * to fetch data from it on subsequent requests.
     */
    fun validate()

    /**
     * Invalidates the cache and forces the repositories
     * to fetch new data on subsequent requests.
     */
    fun invalidate()

}