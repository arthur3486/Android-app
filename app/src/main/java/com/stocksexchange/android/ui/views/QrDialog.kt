package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.utils.extensions.getLayerDrawable
import com.stocksexchange.android.ui.utils.extensions.getStateListDrawable

/**
 * A dialog used for showing QR-related info.
 */
class QrDialog(context: Context) {


    // Views related
    private val mMaterialDialog: MaterialDialog = MaterialDialog.Builder(context)
        .customView(R.layout.qr_dialog_layout, false)
        .build()


    private val mTitleTv: TextView = mMaterialDialog.customView!!.findViewById(R.id.titleTv)
    private val mHashTv: TextView = mMaterialDialog.customView!!.findViewById(R.id.hashTv)

    private val mCopyHashBtnTv: TextView = mMaterialDialog.customView!!.findViewById(R.id.copyHashBtnTv)
    private val mSaveImageBtnTv: TextView = mMaterialDialog.customView!!.findViewById(R.id.saveImageBtnTv)

    private val mQrCodeIv: ImageView = mMaterialDialog.customView!!.findViewById(R.id.qrCodeIv)




    init {
        mCopyHashBtnTv.background = context.getLayerDrawable(
            R.drawable.secondary_button_background,
            R.color.colorBlueAccent, R.color.colorQrDialogBackground
        )
        mSaveImageBtnTv.background = context.getStateListDrawable(
            R.drawable.button_bg_state_pressed,
            R.color.colorBlueAccent,
            R.drawable.button_bg_state_released,
            R.color.colorBlueAccent
        )
    }


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
     * Sets a text of the title.
     *
     * @param text The text to set
     */
    fun setTitle(text: String) {
        mTitleTv.text = text
    }


    /**
     * Gets a title text.
     *
     * @return The title's text
     */
    fun getTitle(): String {
        return mTitleTv.text.toString()
    }


    /**
     * Sets a value of the hash.
     *
     * @param value The value to set
     */
    fun setHash(value: String) {
        mHashTv.text = value
    }


    /**
     * Gets a hash value.
     *
     * @return The hash's value
     */
    fun getHash(): String {
        return mHashTv.text.toString()
    }


    /**
     * Sets an image of the QR code.
     *
     * @param image The image to set
     */
    fun setQrImage(image: Bitmap) {
        mQrCodeIv.setImageBitmap(image)
    }


    /**
     * Sets a listener to be invoked when a user
     * wants to copy a hash value.
     *
     * @param listener The listener to be invoked
     */
    fun setCopyHashButtonClickListener(listener: ((View) -> Unit)) {
        mCopyHashBtnTv.setOnClickListener { listener.invoke(it) }
    }


    /**
     * Sets a listener to be invoked when a user
     * wants to save an image of the QR code.
     *
     * @param listener The listener to be invoked
     */
    fun setSaveImageButtonClickListener(listener: ((View) -> Unit)) {
        mSaveImageBtnTv.setOnClickListener { listener.invoke(it) }
    }


}