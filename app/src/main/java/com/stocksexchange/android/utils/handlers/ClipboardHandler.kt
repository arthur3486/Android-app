package com.stocksexchange.android.utils.handlers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri

/**
 * A handler used for copying text to the clipboard.
 */
class ClipboardHandler(context: Context) {


    private val context: Context = context.applicationContext




    /**
     * Copies the text to the clipboard.
     *
     * @param text The text to copy
     */
    fun copyText(text: String) {
        copyToClipboard(ClipData.newPlainText(text, text))
    }


    /**
     * Copies the URI to the clipboard.
     *
     * @param uri The uri to copy
     */
    fun copyUri(uri: Uri) {
        copyToClipboard(ClipData.newUri(context.contentResolver, uri.toString(), uri))
    }


    private fun copyToClipboard(clipData: ClipData) {
        val clipboardManager = (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        clipboardManager.primaryClip = clipData
    }


}