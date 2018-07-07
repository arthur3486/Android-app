package com.stocksexchange.android.repositories.settings

import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.repositories.BaseRepositoryCache

class SettingsCache : BaseRepositoryCache() {


    companion object {

        private const val KEY_SETTINGS = "settings"

    }




    fun saveSettings(settings: Settings) {
        cache.put(KEY_SETTINGS, settings)
    }


    fun getSettings(): Settings {
        return (cache.get(KEY_SETTINGS) as Settings)
    }


    fun hasSettings(): Boolean {
        return cache.contains(KEY_SETTINGS)
    }


}