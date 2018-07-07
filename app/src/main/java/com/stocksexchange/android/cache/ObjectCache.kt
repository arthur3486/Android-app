package com.stocksexchange.android.cache

/**
 * A universal cache used for storing different items,
 */
object ObjectCache : Cache<String, Any> {


    /**
     * An inner cache where objects are actually stored.
     */
    private var cache: Cache<String, Any> = CacheFactory.getCache(true)




    override fun put(key: String, value: Any): Any? = cache.put(key, value)


    override fun get(key: String, defaultValue: Any?): Any? = cache.get(key, defaultValue)


    override fun remove(key: String, defaultValue: Any?): Any? = cache.remove(key, defaultValue)


    override fun clear() = cache.clear()


    override fun contains(key: String): Boolean = cache.contains(key)


    override fun isEmpty(): Boolean = cache.isEmpty()


}