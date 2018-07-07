package com.stocksexchange.android.ui.settings

import com.stocksexchange.android.R
import com.stocksexchange.android.model.DecimalSeparators
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.android.utils.handlers.UserDataClearingHandler
import com.stocksexchange.android.events.SettingsEvent
import com.stocksexchange.android.events.UserEvent
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.ui.utils.DoubleFormatter
import org.greenrobot.eventbus.EventBus
import org.koin.standalone.inject

class SettingsPresenter(
    model: SettingsModel,
    view: SettingsContract.View
) : BasePresenter<SettingsModel, SettingsContract.View>(model, view), SettingsContract.ActionListener,
    SettingsModel.ActionListener {


    private val mStringProvider: StringProvider by inject()
    private val mUserDataClearingHandler: UserDataClearingHandler by inject()




    init {
        model.setActionListener(this)
    }


    constructor(view: SettingsContract.View): this(SettingsModel(), view)


    override fun start() {
        super.start()

        if(mView.isDataSetEmpty()) {
            mModel.loadSettingsAsync()
        }
    }


    override fun stop() {
        super.stop()

        mView.hideSignOutConfirmationDialog()
        mView.hideDecimalSeparatorsDialog()
        mView.hideRestoreDefaultsConfirmationDialog()
        mView.hideDeviceMetricsDialog()
    }


    override fun onSettingItemsLoaded(settingItems: MutableList<Any>) {
        mView.setItems(settingItems)
    }


    override fun onSettingSwitchClicked(setting: Setting, isChecked: Boolean) {
        setting.isChecked = isChecked
        onSettingItemClicked(setting)
    }


    override fun onSettingItemClicked(setting: Setting) {
        when(setting.id) {

            SettingsModel.SETTING_ID_SIGN_OUT -> onSignOutItemClicked()
            SettingsModel.SETTING_ID_DECIMAL_SEPARATOR -> onDecimalSeparatorItemClicked()
            SettingsModel.SETTING_ID_RESTORE_DEFAULTS -> onRestoreDefaultsItemClicked()
            SettingsModel.SETTING_ID_IS_SOUND_ENABLED -> onSoundsItemClicked(setting)
            SettingsModel.SETTING_ID_IS_VIBRATION_ENABLED -> onVibrationItemClicked(setting)
            SettingsModel.SETTING_ID_IS_PHONE_LED_ENABLED -> onPhoneLedItemClicked(setting)
            SettingsModel.SETTING_ID_NOTIFICATION_RINGTONE -> onNotificationRingtoneItemClicked()
            SettingsModel.SETTING_ID_DEVICE_METRICS -> onDeviceMetricsClicked()

        }
    }


    override fun onSignOutItemClicked() {
        mView.showSignOutConfirmationDialog()
    }


    override fun onDecimalSeparatorItemClicked() {
        mView.showDecimalSeparatorsDialog()
    }


    override fun onRestoreDefaultsItemClicked() {
        mView.showRestoreDefaultsConfirmationDialog()
    }


    override fun onSoundsItemClicked(setting: Setting) {
        mModel.updateSettingsAsync { settings, settingsRepository ->
            settingsRepository.save(settings.copy(isSoundEnabled = setting.isChecked))
        }
    }


    override fun onVibrationItemClicked(setting: Setting) {
        mModel.updateSettingsAsync { settings, settingsRepository ->
            settingsRepository.save(settings.copy(isVibrationEnabled = setting.isChecked))
        }
    }


    override fun onPhoneLedItemClicked(setting: Setting) {
        mModel.updateSettingsAsync { settings, settingsRepository ->
            settingsRepository.save(settings.copy(isPhoneLedEnabled = setting.isChecked))
        }
    }


    override fun onNotificationRingtoneItemClicked() {
        if(!mView.checkRingtonePermissions()) {
            return
        }

        mView.launchRingtonePickerActivity()
    }


    override fun onDeviceMetricsClicked() {
        mView.showDeviceMetricsDialog()
    }


    override fun onSignOutConfirmed() {
        mView.showProgressBar()

        mUserDataClearingHandler.clearUserData {
            EventBus.getDefault().postSticky(UserEvent.signOut(this))

            mView.hideProgressBar()
            mView.finishActivity()
        }
    }


    override fun onDecimalSeparatorPicked(separator: String) {
        val newDecimalSeparator = when(separator) {
            mStringProvider.getString(R.string.period) -> DecimalSeparators.PERIOD
            else -> DecimalSeparators.COMMA
        }

        mModel.updateSettingsAsync { settings, settingsRepository ->
            if(newDecimalSeparator == settings.decimalSeparator) {
                return@updateSettingsAsync
            }

            val newSettings = settings.copy(decimalSeparator = newDecimalSeparator)
            settingsRepository.save(newSettings)

            updateDoubleFormatterSeparator(newSettings.decimalSeparator.separator)
            mView.updateSettingWith(mModel.getItemForId(SettingsModel.SETTING_ID_DECIMAL_SEPARATOR, newSettings))

            EventBus.getDefault().postSticky(SettingsEvent.changeDecimalSeparator(this))
        }
    }


    private fun updateDoubleFormatterSeparator(separator: Char) {
        DoubleFormatter.getInstance(mView.getLocale()).setDecimalSeparator(separator)
    }


    override fun onRestoreDefaultsConfirmed() {
        val locale = mView.getLocale()

        mModel.updateSettingsAsync { _, settingsRepository ->
            val defaultSettings = Settings.getDefaultSettings(locale)
            settingsRepository.save(defaultSettings)

            updateDoubleFormatterSeparator(defaultSettings.decimalSeparator.separator)
            mModel.loadSettingsAsync()

            EventBus.getDefault().postSticky(SettingsEvent.restoreDefaults(this))
        }
    }


    override fun onNotificationRingtonePicked(ringtone: String) {
        mModel.updateSettingsAsync { settings, settingsRepository ->
            val newSettings = settings.copy(notificationRingtone = ringtone)
            settingsRepository.save(newSettings)

            mView.updateSettingWith(mModel.getItemForId(SettingsModel.SETTING_ID_NOTIFICATION_RINGTONE, newSettings))
        }
    }


}