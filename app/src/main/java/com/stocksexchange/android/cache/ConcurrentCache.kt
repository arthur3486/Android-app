package com.stocksexchange.android.cache

/**
 * A cache that is safe to use in a multi-threading environments
 * since all method calls are synchronized.
 */
class ConcurrentCache<in Key, Value>(
    private val cache: Cache<Key, Value>
) : Cache<Key, Value> {


    /**
     * An object serving as a lock for the methods.
     */
    private val lock: Any = Any()




    override fun put(key: Key, value: Value): Value? {
        synchronized(lock) {
            return cache.put(key, value)
        }
    }


    override fun get(key: Key, defaultValue: Value?): Value? {
        synchronized(lock) {
            return cache.get(key, defaultValue)
        }
    }


    override fun remove(key: Key, defaultValue: Value?): Value? {
        synchronized(lock) {
            return cache.remove(key, defaultValue)
        }
    }


    override fun clear() {
        synchronized(lock) {
            cache.clear()
        }
    }


    override fun contains(key: Key): Boolean {
        synchronized(lock) {
            return cache.contains(key)
        }
    }


    override fun isEmpty(): Boolean {
        synchronized(lock) {
            return cache.isEmpty()
        }
    }


}