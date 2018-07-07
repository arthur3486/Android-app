package com.stocksexchange.android.ui.views

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.stocksexchange.android.R

/**
 * A dialog used for showing device specific metrics.
 */
class DeviceMetricsDialog(context: Context) {


    // Views related
    private val mMaterialDialog: MaterialDialog = MaterialDialog.Builder(context)
        .customView(R.layout.device_metrics_dialog_layout, false)
        .build()


    private val mDeviceNameOtv: OptionTextView = mMaterialDialog.customView!!.findViewById(R.id.deviceNameOtv)
    private val mDensityOtv: OptionTextView = mMaterialDialog.customView!!.findViewById(R.id.densityOtv)
    private val mDensityInDpOtv: OptionTextView = mMaterialDialog.customView!!.findViewById(R.id.densityInDpOtv)
    private val mScreenWidthInPxOtv: OptionTextView = mMaterialDialog.customView!!.findViewById(R.id.screenWidthInPxOtv)
    private val mScreenWidthInDpOtv: OptionTextView = mMaterialDialog.customView!!.findViewById(R.id.screenWidthInDpOtv)
    private val mScreenHeightInPxOtv: OptionTextView = mMaterialDialog.customView!!.findViewById(R.id.screenHeightInPxOtv)
    private val mScreenHeightInDpOtv: OptionTextView = mMaterialDialog.customView!!.findViewById(R.id.screenHeightInDpOtv)
    private val mSmallestWidthInDpOtv: OptionTextView = mMaterialDialog.customView!!.findViewById(R.id.smallestWidthInDpOtv)




    /**
     * Shows the dialog.
     */
    fun show() {
        mMaterialDialog.show()
    }


    /**
     * Dismisses the dialog.
     */
    fun dismiss() {
        mMaterialDialog.dismiss()
    }


    /**
     * Sets a device name.
     *
     * @param deviceName The device name to set
     */
    fun setDeviceName(deviceName: String) {
        mDeviceNameOtv.setValueText(deviceName)
    }


    /**
     * Sets a density.
     *
     * @param density The density to set
     */
    fun setDensity(density: Float) {
        mDensityOtv.setValueText(density.toString())
    }


    /**
     * Sets a density in DPI.
     *
     * @param densityInDp The density in DPI to set
     */
    fun setDensityInDp(densityInDp: Int) {
        mDensityInDpOtv.setValueText(densityInDp.toString())
    }


    /**
     * Sets a width in pixels.
     *
     * @param widthInPx The width in pixels to set
     */
    fun setScreenWidthInPx(widthInPx: Int) {
        mScreenWidthInPxOtv.setValueText(widthInPx.toString())
    }


    /**
     * Sets a width in DPI.
     *
     * @param widthInDp The width in DPI to set
     */
    fun setScreenWidthInDp(widthInDp: Float) {
        mScreenWidthInDpOtv.setValueText(widthInDp.toString())
    }


    /**
     * Sets a height in pixels.
     *
     * @param heightInPx The height in pixels to set
     */
    fun setScreenHeightInPx(heightInPx: Int) {
        mScreenHeightInPxOtv.setValueText(heightInPx.toString())
    }


    /**
     * Sets a height in DPI.
     *
     * @param heightInDp The height in DPI to set
     */
    fun setScreenHeightInDp(heightInDp: Float) {
        mScreenHeightInDpOtv.setValueText(heightInDp.toString())
    }


    /**
     * Sets a smallest width in DPI.
     *
     * @param smallestWidthInDp The smallest width in DPI to set
     */
    fun setSmallestWidthInDp(smallestWidthInDp: Int) {
        mSmallestWidthInDpOtv.setValueText(smallestWidthInDp.toString())
    }


}