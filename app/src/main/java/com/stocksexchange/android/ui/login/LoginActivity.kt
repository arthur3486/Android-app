package com.stocksexchange.android.ui.login

import android.Manifest
import android.content.Context
import android.content.Intent
import com.stocksexchange.android.R
import com.stocksexchange.android.REQUEST_CODE_CAMERA_PERMISSION
import com.stocksexchange.android.utils.helpers.isPermissionSetGranted
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.dashboard.DashboardActivity
import com.stocksexchange.android.ui.utils.extensions.*
import kotlinx.android.synthetic.main.login_activity_layout.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop

class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.View {


    companion object {

        fun newInstance(context: Context): Intent {
            return context.intentFor<LoginActivity>().clearTop().singleTop()
        }

    }




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.fade_in_animation,
            R.anim.fade_out_animation
        )
    }


    override fun initPresenter(): LoginPresenter = LoginPresenter(this)


    override fun init() {
        initQrCodeScanner()
        initProgressBar()

        checkPermissions(
            REQUEST_CODE_CAMERA_PERMISSION,
            arrayOf(Manifest.permission.CAMERA)
        )
    }


    private fun initQrCodeScanner() {
        qrCodeScanner.onBarcodeDetected { mPresenter?.onQrCodeScanned(it.rawValue) }
    }


    private fun initProgressBar() {
        progressBar.setColor(R.color.colorProgressBar)
    }


    override fun showProgress() {
        if(!progressGrp.isVisible()) {
            progressGrp.makeVisible()
        }
    }


    override fun hideProgress() {
        if(progressGrp.isVisible()) {
            progressGrp.makeGone()
        }
    }


    override fun enableQrCodeScanner() {
        if(!qrCodeScanner.isEnabled) {
            qrCodeScanner.enable()
        }
    }


    override fun disableQrCodeScanner() {
        if(qrCodeScanner.isEnabled) {
            qrCodeScanner.disable()
        }
    }


    override fun launchDashboard() {
        startActivity(DashboardActivity.newInstance(this))
        finish()
    }


    private fun startQrCodeScanner(requestLayout: Boolean = false) {
        if(isCameraPermissionGranted()) {
            qrCodeScanner.start()

            if(requestLayout) {
                qrCodeScanner.requestLayout()
            }
        }
    }


    override fun getContentLayoutResourceId(): Int = R.layout.login_activity_layout


    private fun isCameraPermissionGranted(): Boolean {
        return checkPermission(Manifest.permission.CAMERA)
    }


    override fun onResume() {
        super.onResume()

        startQrCodeScanner()
    }


    override fun onPause() {
        super.onPause()

        if(isCameraPermissionGranted()) {
            qrCodeScanner.stop()
        }
    }


    override fun onStop() {
        super.onStop()

        if(isCameraPermissionGranted()) {
            qrCodeScanner.release()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(isPermissionSetGranted(grantResults)) {
            if(requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
                startQrCodeScanner(true)
            }
        } else {
            showToast(getString(R.string.error_permissions_not_granted))
        }
    }


}