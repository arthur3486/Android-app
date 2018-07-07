package com.stocksexchange.android.datastores.settings

import com.stocksexchange.android.model.Result
import com.stocksexchange.android.model.Settings

interface SettingsDataStore {

    suspend fun save(settings: Settings)

    suspend fun get(): Result<Settings>

}