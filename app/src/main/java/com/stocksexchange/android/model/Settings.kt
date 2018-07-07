package com.stocksexchange.android.model

import com.stocksexchange.android.utils.helpers.getDefaultNotificationRingtone
import com.stocksexchange.android.utils.helpers.getLocaleDecimalSeparator
import java.io.Serializable
import java.util.*

/**
 * A model class holding app's settings.
 */
data class Settings(
    val id: Int,
    val isSoundEnabled: Boolean,
    val isVibrationEnabled: Boolean,
    val isPhoneLedEnabled: Boolean,
    val notificationRingtone: String,
    val decimalSeparator: DecimalSeparators
) : Serializable {


    companion object {

        const val SETTINGS_ID = 1


        /**
         * Retrieves default settings.
         *
         * @param locale The device's locale
         *
         * @return The default settings
         */
        fun getDefaultSettings(locale: Locale): Settings {
            val isSoundEnabled = true
            val isVibrationEnabled = true
            val isPhoneLedEnabled = true
            val notificationRingtone = getDefaultNotificationRingtone()
            val decimalSeparator = DecimalSeparators.getEnumForSeparator(getLocaleDecimalSeparator(locale).toString())

            return Settings(
                SETTINGS_ID,
                isSoundEnabled,
                isVibrationEnabled,
                isPhoneLedEnabled,
                notificationRingtone,
                decimalSeparator
            )
        }

    }


}