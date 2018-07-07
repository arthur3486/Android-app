package com.stocksexchange.android.cache

/**
 * A factory for returning a specific [Cache] implementation.
 */
class CacheFactory {


    companion object {


        /**
         * Retrieves a cache based on the input parameters.
         *
         * @param isConcurrent Whether the cache should be concurrent or not
         *
         * @return The cache according to the input parameters
         */
        fun <Key, Value> getCache(isConcurrent: Boolean): Cache<Key, Value> {
            val cache = MemoryCache<Key, Value>()

            return if(isConcurrent) {
                ConcurrentCache(cache)
            } else {
                cache
            }
        }


    }


}