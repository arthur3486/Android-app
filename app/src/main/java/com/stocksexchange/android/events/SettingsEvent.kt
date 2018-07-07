package com.stocksexchange.android.events

import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.helpers.tag

/**
 * An event to send to notify subscribers about
 * [Settings] model class updates.
 */
class SettingsEvent private constructor(
    type: Int,
    sourceTag: String,
    val action: Actions
) : BaseEvent<Void?>(type, null, sourceTag) {


    companion object {


        fun changeDecimalSeparator(source: Any): SettingsEvent {
            return changeDecimalSeparator(tag(source))
        }


        fun changeDecimalSeparator(sourceTag: String): SettingsEvent {
            return SettingsEvent(TYPE_INVALID, sourceTag, Actions.DECIMAL_SEPARATOR_CHANGED)
        }


        fun restoreDefaults(source: Any): SettingsEvent {
            return restoreDefaults(tag(source))
        }


        fun restoreDefaults(sourceTag: String): SettingsEvent {
            return SettingsEvent(TYPE_INVALID, sourceTag, Actions.DEFAULTS_RESTORED)
        }


    }


    enum class Actions {

        DECIMAL_SEPARATOR_CHANGED,
        DEFAULTS_RESTORED

    }


}