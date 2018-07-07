package com.stocksexchange.android.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.stocksexchange.android.database.model.DatabaseSettings.Companion.TABLE_NAME
import com.stocksexchange.android.model.DecimalSeparators
import com.stocksexchange.android.model.Settings

/**
 * A Room database model class of [Settings] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseSettings(
    @PrimaryKey @ColumnInfo(name = ID) var id: Int,
    @ColumnInfo(name = IS_SOUND_ENABLED) var isSoundEnabled: Boolean,
    @ColumnInfo(name = IS_VIBRATION_ENABLED) var isVibrationEnabled: Boolean,
    @ColumnInfo(name = IS_PHONE_LED_ENABLED) var isPhoneLedEnabled: Boolean,
    @ColumnInfo(name = NOTIFICATION_RINGTONE) var notificationRingtone: String,
    @ColumnInfo(name = DECIMAL_SEPARATOR) var decimalSeparator: DecimalSeparators
) {

    companion object {

        const val TABLE_NAME = "settings"

        const val ID = "id"
        const val IS_SOUND_ENABLED = "is_sound_enabled"
        const val IS_VIBRATION_ENABLED = "is_vibration_enabled"
        const val IS_PHONE_LED_ENABLED = "is_phone_led_enabled"
        const val NOTIFICATION_RINGTONE = "notification_ringtone"
        const val DECIMAL_SEPARATOR = "decimal_separator"

    }


    constructor(): this(-1, false, false, false, "", DecimalSeparators.PERIOD)

}