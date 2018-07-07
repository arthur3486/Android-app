package com.stocksexchange.android.ui.splash

import android.content.Intent
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller
import com.google.android.gms.security.ProviderInstaller.ProviderInstallListener
import com.stocksexchange.android.R
import com.stocksexchange.android.REQUEST_CODE_SECURITY_PROVIDER
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.dashboard.DashboardActivity
import com.stocksexchange.android.ui.utils.extensions.getLocale
import org.jetbrains.anko.ctx
import java.util.*

class SplashActivity : BaseActivity<SplashPresenter>(), SplashContract.View {


    override fun initPresenter(): SplashPresenter = SplashPresenter(this)


    override fun getContentLayoutResourceId(): Int = R.layout.splash_activity_layout


    override fun installSecurityProvider() {
        ProviderInstaller.installIfNeededAsync(this, providerInstallListener)
    }


    override fun launchDashboard() {
        startActivity(DashboardActivity.newInstance(this))
        finish()
    }


    override fun getLocale(): Locale {
        return ctx.getLocale()
    }


    private fun onSecurityProviderInstallerNotAvailable() {
        showLongToast(getString(R.string.error_security_provider))
        finish()
    }


    private val providerInstallListener: ProviderInstallListener = object : ProviderInstallListener {

        /**
         * This method is only called if the provider is successfully updated
         * (or is already up-to-date).
         */
        override fun onProviderInstalled() {
            mPresenter?.onSecurityProviderInstalled()
        }

        /**
         * This method is called if updating fails; the error code indicates
         * whether the error is recoverable.
         */
        override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
            if(GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
                // Recoverable error. Show a dialog prompting the user to
                // install/update/enable Google Play services
                GooglePlayServicesUtil.showErrorDialogFragment(
                    errorCode,
                    this@SplashActivity,
                    REQUEST_CODE_SECURITY_PROVIDER,
                    { onSecurityProviderInstallerNotAvailable() }
                )
            } else {
                // Google Play services is not available
                onSecurityProviderInstallerNotAvailable()
            }
        }

    }


}