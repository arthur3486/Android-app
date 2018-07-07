package com.stocksexchange.android.receivers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.stocksexchange.android.R
import com.stocksexchange.android.REQUEST_CODE_COPY_LINK
import com.stocksexchange.android.REQUEST_CODE_SHARE_VIA
import com.stocksexchange.android.ui.utils.extensions.shareText
import com.stocksexchange.android.utils.handlers.ClipboardHandler
import org.jetbrains.anko.intentFor
import org.koin.standalone.get

/**
 * A receiver to get notified whenever actions are
 * selected in custom tabs browser.
 */
class CustomTabsReceiver : BaseBroadcastReceiver() {


    companion object {

        private const val EXTRA_REQUEST_CODE = "request_code"


        fun init(context: Context, uri: Uri, requestCode: Int): PendingIntent {
            val intent = context.intentFor<CustomTabsReceiver>(EXTRA_REQUEST_CODE to requestCode)
            intent.data = uri

            return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }

    }




    override fun onReceive(context: Context, intent: Intent) {
        val uri = intent.data

        when(intent.getIntExtra(EXTRA_REQUEST_CODE, REQUEST_CODE_SHARE_VIA)) {
            REQUEST_CODE_SHARE_VIA -> onShareViaTabClicked(context, uri.toString())
            REQUEST_CODE_COPY_LINK -> onCopyLinkTabClicked(context, uri)
        }
    }


    private fun onShareViaTabClicked(context: Context, url: String) {
        context.shareText(
            url,
            context.getString(R.string.action_share_via)
        )
    }


    private fun onCopyLinkTabClicked(context: Context, uri: Uri) {
        get<ClipboardHandler>().copyUri(uri)

        Toast.makeText(
            context,
            context.getString(R.string.copied_to_clipboard),
            Toast.LENGTH_LONG
        ).show()
    }


}