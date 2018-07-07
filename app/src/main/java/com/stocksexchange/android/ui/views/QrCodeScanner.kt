package com.stocksexchange.android.ui.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.stocksexchange.android.utils.BarcodeTrackerFactory
import org.jetbrains.anko.forEachChild
import java.io.IOException

/**
 * A view container for scanning QR codes.
 */
@SuppressLint("MissingPermission")
class QrCodeScanner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {


    private var mSurfaceAvailable = false
    private var mStartedByUser = false

    private var mBarcodeCallback: ((Barcode) -> Unit)? = null
    private val mSurfaceCallback = object: SurfaceHolder.Callback {

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            // Do nothing
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            mSurfaceAvailable = true
            refreshCamera()
            requestLayout()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            mSurfaceAvailable = false
        }

    }

    private val mSurfaceView = SurfaceView(context)
    private var mCameraSource: CameraSource? = null




    init {
        val holder = mSurfaceView.holder
        holder.addCallback(mSurfaceCallback)
        holder.setSizeFromLayout()

        addView(mSurfaceView)
    }


    /**
     * Starts the scanner.
     */
    fun start() {
        mStartedByUser = true
        startCameraSource()
    }


    /**
     * Stops the scanner.
     */
    fun stop() {
        mCameraSource?.stop()
    }


    /**
     * Releases the scanner.
     */
    fun release() {
        releaseCameraSource()
    }


    /**
     * A callback to be invoked whenever a QR code
     * has been detected.
     *
     * @param handler The handle to invoke
     */
    fun onBarcodeDetected(handler: (Barcode) -> Unit) {
        mBarcodeCallback = handler
    }


    private fun startCameraSource() {
        if(mStartedByUser) try {
            if(mSurfaceAvailable) {
                mCameraSource?.start(mSurfaceView.holder)
            }
        } catch(exception: IOException) {
            releaseCameraSource()
        }
    }


    private fun releaseCameraSource() {
        try {
            mCameraSource?.release()
        } catch(throwable: Throwable) {
            // Do nothing
        }
    }


    private fun refreshCamera() {
        releaseCameraSource()
        mCameraSource = createCameraSource()
        startCameraSource()
    }


    private fun onBarcodeDetected(barcode: Barcode) {
        (context as? Activity)?.runOnUiThread {
            mBarcodeCallback?.invoke(barcode)
        }
    }


    private fun createCameraSource() : CameraSource {
        return CameraSource.Builder(context.applicationContext,
            BarcodeDetector.Builder(context).build().apply {
                setProcessor(MultiProcessor.Builder(
                    BarcodeTrackerFactory(::onBarcodeDetected)
                ).build())
            })
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1600, 1024)
            .setRequestedFps(15.0f)
            .setAutoFocusEnabled(true)
            .build()
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val layoutWidth = (right - left)
        val layoutHeight = (bottom - top)

        // Calculating surface view layout to avoid deformed picture
        val (width, height) = mCameraSource
            ?.takeUnless { it.previewSize == null }
            ?.let { cameraSource ->
                val (previewWidth, previewHeight) = with(cameraSource.previewSize) { height to width }

                return@let if((previewHeight * layoutWidth / previewWidth) > layoutHeight) {
                    layoutWidth to (previewHeight * layoutWidth / previewWidth)
                } else (previewWidth * layoutHeight / previewHeight) to layoutHeight
            } ?: (layoutWidth to layoutHeight)

        forEachChild {
            it.layout(0, 0, width, height)
        }

        startCameraSource()
    }


}