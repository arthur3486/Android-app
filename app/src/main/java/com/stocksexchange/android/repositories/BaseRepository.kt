package com.stocksexchange.android.repositories

/**
 * A base repository to extend from.
 */
abstract class BaseRepository<out T : BaseRepositoryCache> : Repository {


    /**
     * A cache for storing repository related data.
     */
    abstract val cache: T




    override fun refresh() {
        cache.invalidate()
    }


}