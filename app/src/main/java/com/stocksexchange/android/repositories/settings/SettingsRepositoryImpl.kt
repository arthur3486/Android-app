package com.stocksexchange.android.repositories.settings

import com.stocksexchange.android.datastores.settings.SettingsDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.repositories.BaseRepository
import com.stocksexchange.android.utils.helpers.handleRepositoryResult

class SettingsRepositoryImpl(
    private val databaseDataStore: SettingsDataStore
) : BaseRepository<SettingsCache>(), SettingsRepository {


    override val cache: SettingsCache = SettingsCache()




    override suspend fun save(settings: Settings) {
        databaseDataStore.save(settings)
        saveToCache(settings)
    }


    override suspend fun get(): RepositoryResult<Settings> {
        val result = RepositoryResult<Settings>()

        if(!cache.hasSettings()) {
            result.databaseResult = databaseDataStore.get()

            if(!handleRepositoryResult(result, { saveToCache(it.value) })) {
                return result
            }
        } else {
            result.cacheResult = Result.Success(cache.getSettings())
        }

        return result
    }


    private fun saveToCache(settings: Settings) {
        cache.saveSettings(settings)
    }


}