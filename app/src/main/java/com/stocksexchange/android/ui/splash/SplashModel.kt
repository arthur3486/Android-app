package com.stocksexchange.android.ui.splash

import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.repositories.settings.SettingsRepository
import com.stocksexchange.android.repositories.users.UsersRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.ui.utils.DoubleFormatter
import org.koin.standalone.inject
import java.util.*

class SplashModel : BaseModel() {


    private val mSettingsRepository: SettingsRepository by inject()
    private val mUsersRepository: UsersRepository by inject()




    fun initServices(locale: Locale, onCompletion: () -> Unit) {
        performAsync {
            initSettings(locale)
            initDoubleFormatter(locale)
            updateUserIfNecessary()

            onCompletion.invoke()
        }
    }


    private suspend fun initSettings(locale: Locale) {
        val result = mSettingsRepository.get()

        if(result.isErroneous()) {
            mSettingsRepository.save(Settings.getDefaultSettings(locale))
        }
    }


    private suspend fun initDoubleFormatter(locale: Locale) {
        val result = mSettingsRepository.get()

        if(result.isSuccessful()) {
            val settings = result.getSuccessfulResult().value

            val doubleFormatter = DoubleFormatter.getInstance(locale)
            doubleFormatter.setDecimalSeparator(settings.decimalSeparator.separator)
        }
    }


    private suspend fun updateUserIfNecessary() {
        if(mUsersRepository.hasSignedInUser()) {
            mUsersRepository.getSignedInUser()
        }
    }


}