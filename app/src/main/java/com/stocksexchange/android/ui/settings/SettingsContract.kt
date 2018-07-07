package com.stocksexchange.android.ui.settings

import com.stocksexchange.android.model.Setting
import java.util.*

interface SettingsContract {


    interface View {

        fun showProgressBar()

        fun hideProgressBar()

        fun showSignOutConfirmationDialog()

        fun hideSignOutConfirmationDialog()

        fun showDecimalSeparatorsDialog()

        fun hideDecimalSeparatorsDialog()

        fun showRestoreDefaultsConfirmationDialog()

        fun hideRestoreDefaultsConfirmationDialog()

        fun showDeviceMetricsDialog()

        fun hideDeviceMetricsDialog()

        fun checkRingtonePermissions(): Boolean

        fun launchRingtonePickerActivity()

        fun updateSettingWith(setting: Setting)

        fun finishActivity()

        fun setItems(items: MutableList<Any>)

        fun isDataSetEmpty(): Boolean

        fun getLocale(): Locale

    }


    interface ActionListener {

        fun onSettingSwitchClicked(setting: Setting, isChecked: Boolean)

        fun onSettingItemClicked(setting: Setting)

        fun onSignOutItemClicked()

        fun onDecimalSeparatorItemClicked()

        fun onRestoreDefaultsItemClicked()

        fun onSoundsItemClicked(setting: Setting)

        fun onVibrationItemClicked(setting: Setting)

        fun onPhoneLedItemClicked(setting: Setting)

        fun onNotificationRingtoneItemClicked()

        fun onDeviceMetricsClicked()

        fun onSignOutConfirmed()

        fun onDecimalSeparatorPicked(separator: String)

        fun onRestoreDefaultsConfirmed()

        fun onNotificationRingtonePicked(ringtone: String)

    }


}