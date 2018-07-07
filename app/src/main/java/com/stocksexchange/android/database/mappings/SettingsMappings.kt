package com.stocksexchange.android.database.mappings

import com.stocksexchange.android.database.model.DatabaseSettings
import com.stocksexchange.android.model.Settings

fun Settings.mapToDatabaseSettings(): DatabaseSettings {
    return DatabaseSettings(
        id = id,
        isSoundEnabled = isSoundEnabled,
        isVibrationEnabled = isVibrationEnabled,
        isPhoneLedEnabled = isPhoneLedEnabled,
        notificationRingtone = notificationRingtone,
        decimalSeparator = decimalSeparator
    )
}


fun DatabaseSettings.mapToSettings(): Settings {
    return Settings(
        id = id,
        isSoundEnabled = isSoundEnabled,
        isVibrationEnabled = isVibrationEnabled,
        isPhoneLedEnabled = isPhoneLedEnabled,
        notificationRingtone = notificationRingtone,
        decimalSeparator = decimalSeparator
    )
}