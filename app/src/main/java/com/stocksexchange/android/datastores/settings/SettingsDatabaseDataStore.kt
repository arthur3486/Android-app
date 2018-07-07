package com.stocksexchange.android.datastores.settings

import com.stocksexchange.android.database.daos.SettingsDao
import com.stocksexchange.android.database.mappings.mapToDatabaseSettings
import com.stocksexchange.android.datastores.exceptions.SettingsNotFoundException
import com.stocksexchange.android.database.mappings.mapToSettings
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.helpers.executeBackgroundOperation
import com.stocksexchange.android.utils.helpers.performBackgroundOperation

class SettingsDatabaseDataStore(
    private val settingsDao: SettingsDao
) : SettingsDataStore {


    override suspend fun save(settings: Settings) {
        executeBackgroundOperation {
            settingsDao.insert(settings.mapToDatabaseSettings())
        }
    }


    override suspend fun get(): Result<Settings> {
        return performBackgroundOperation {
            settingsDao.get(Settings.SETTINGS_ID)
                ?.mapToSettings()
                ?: throw SettingsNotFoundException()
        }
    }


}