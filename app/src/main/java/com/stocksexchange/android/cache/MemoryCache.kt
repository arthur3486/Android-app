package com.stocksexchange.android.cache

/**
 * A cache that resides inside the memory of the device.
 */
class MemoryCache<in Key, Value> : Cache<Key, Value> {


    /**
     * A hash-map consisting of the specified keys and values.
     */
    private val cacheMap: HashMap<Key, Value> = HashMap()




    override fun put(key: Key, value: Value): Value? {
        return cacheMap.put(key, value)
    }


    override fun get(key: Key, defaultValue: Value?): Value? {
        return if(contains(key)) {
            cacheMap[key]
        } else {
            defaultValue
        }
    }


    override fun remove(key: Key, defaultValue: Value?): Value? {
        return if(contains(key)) {
            cacheMap.remove(key)
        } else {
            defaultValue
        }
    }


    override fun clear() {
        cacheMap.clear()
    }


    override fun contains(key: Key): Boolean {
        return cacheMap.contains(key)
    }


    override fun isEmpty(): Boolean {
        return cacheMap.isEmpty()
    }


}