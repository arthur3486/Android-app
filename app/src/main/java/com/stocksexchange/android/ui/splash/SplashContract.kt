package com.stocksexchange.android.ui.splash

import java.util.*

interface SplashContract {


    interface View {

        fun installSecurityProvider()

        fun launchDashboard()

        fun getLocale(): Locale

    }


    interface ActionListener {

        fun onSecurityProviderInstalled()

    }


}