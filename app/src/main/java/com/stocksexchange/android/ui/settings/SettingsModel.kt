package com.stocksexchange.android.ui.settings

import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.model.DecimalSeparators
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingSection
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.providers.RingtoneProvider
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.android.repositories.settings.SettingsRepository
import com.stocksexchange.android.repositories.users.UsersRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import org.koin.standalone.inject

class SettingsModel : BaseModel() {


    companion object {

        const val SETTING_ID_SIGN_OUT = 0
        const val SETTING_ID_DECIMAL_SEPARATOR = 1
        const val SETTING_ID_RESTORE_DEFAULTS = 2
        const val SETTING_ID_IS_SOUND_ENABLED = 3
        const val SETTING_ID_IS_VIBRATION_ENABLED = 4
        const val SETTING_ID_IS_PHONE_LED_ENABLED = 5
        const val SETTING_ID_NOTIFICATION_RINGTONE = 6
        const val SETTING_ID_DEVICE_METRICS = 7

    }


    private var mActionListener: ActionListener? = null

    private val mSettingsRepository: SettingsRepository by inject()
    private val mUsersRepository: UsersRepository by inject()
    private val mStringProvider: StringProvider by inject()
    private val mRingtoneProvider: RingtoneProvider by inject()




    fun loadSettingsAsync() {
        performAsync {
            val settings = mSettingsRepository.get().getSuccessfulResult().value
            val userResult = mUsersRepository.getSignedInUser()
            val items: MutableList<Any> = mutableListOf()

            if(userResult.isSuccessful()) {
                val user = userResult.getSuccessfulResult().value

                // Account section
                items.add(SettingSection(mStringProvider.getString(R.string.account)))
                items.add(getItemForId(SETTING_ID_SIGN_OUT, settings, user))
            }

            // General section
            items.add(SettingSection(mStringProvider.getString(R.string.general)))
            items.add(getItemForId(SETTING_ID_DECIMAL_SEPARATOR, settings))
            items.add(getItemForId(SETTING_ID_RESTORE_DEFAULTS, settings))

            // Notifications section
            items.add(SettingSection(mStringProvider.getString(R.string.notifications)))
            items.add(getItemForId(SETTING_ID_IS_SOUND_ENABLED, settings))
            items.add(getItemForId(SETTING_ID_IS_VIBRATION_ENABLED, settings))
            items.add(getItemForId(SETTING_ID_IS_PHONE_LED_ENABLED, settings))
            items.add(getItemForId(SETTING_ID_NOTIFICATION_RINGTONE, settings))

            // Debug section
            if(BuildConfig.DEBUG) {
                items.add(SettingSection(mStringProvider.getString(R.string.debug)))
                items.add(getItemForId(SETTING_ID_DEVICE_METRICS, settings))
            }

            mActionListener?.onSettingItemsLoaded(items)
        }
    }


    fun updateSettingsAsync(block: suspend (Settings, SettingsRepository) -> Unit) {
        performAsync {
            val settingsResult = mSettingsRepository.get()

            if(settingsResult.isSuccessful()) {
                block(settingsResult.getSuccessfulResult().value, mSettingsRepository)
            }
        }
    }


    fun getItemForId(id: Int, settings: Settings, user: User? = null): Setting {
        return when(id) {

            SETTING_ID_SIGN_OUT -> {
                Setting(
                    id = SETTING_ID_SIGN_OUT,
                    title = mStringProvider.getString(R.string.sign_out),
                    description = mStringProvider.getString(R.string.sign_out_template, user!!.userName)
                )
            }

            SETTING_ID_DECIMAL_SEPARATOR -> {
                Setting(
                    id = SETTING_ID_DECIMAL_SEPARATOR,
                    title = mStringProvider.getString(R.string.decimal_separator),
                    description = when(settings.decimalSeparator) {
                        DecimalSeparators.PERIOD -> mStringProvider.getString(R.string.period)
                        DecimalSeparators.COMMA -> mStringProvider.getString(R.string.comma)
                    }
                )
            }

            SETTING_ID_RESTORE_DEFAULTS -> {
                Setting(
                    id = SETTING_ID_RESTORE_DEFAULTS,
                    title = mStringProvider.getString(R.string.restore_defaults)
                )
            }

            SETTING_ID_IS_SOUND_ENABLED -> {
                Setting(
                    id = SETTING_ID_IS_SOUND_ENABLED,
                    isCheckable = true,
                    isChecked = settings.isSoundEnabled,
                    title = mStringProvider.getString(R.string.sounds)
                )
            }

            SETTING_ID_IS_VIBRATION_ENABLED -> {
                Setting(
                    id = SETTING_ID_IS_VIBRATION_ENABLED,
                    isCheckable = true,
                    isChecked = settings.isVibrationEnabled,
                    title = mStringProvider.getString(R.string.vibration)
                )
            }

            SETTING_ID_IS_PHONE_LED_ENABLED -> {
                Setting(
                    id = SETTING_ID_IS_PHONE_LED_ENABLED,
                    isCheckable = true,
                    isChecked = settings.isPhoneLedEnabled,
                    title = mStringProvider.getString(R.string.phone_led)
                )
            }

            SETTING_ID_NOTIFICATION_RINGTONE -> {
                Setting(
                    id = SETTING_ID_NOTIFICATION_RINGTONE,
                    title = mStringProvider.getString(R.string.notification_ringtone),
                    description = mRingtoneProvider.getRingtoneName(settings.notificationRingtone)
                )
            }

            else -> {
                Setting(
                    id = SETTING_ID_DEVICE_METRICS,
                    title = mStringProvider.getString(R.string.device_metrics),
                    description = mStringProvider.getString(R.string.device_metrics_description)
                )
            }
        }
    }


    fun setActionListener(listener: ActionListener) {
        mActionListener = listener
    }


    interface ActionListener {

        fun onSettingItemsLoaded(settingItems: MutableList<Any>)

    }


}