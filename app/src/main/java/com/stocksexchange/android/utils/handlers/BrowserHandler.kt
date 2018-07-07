package com.stocksexchange.android.utils.handlers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.stocksexchange.android.R
import com.stocksexchange.android.REQUEST_CODE_COPY_LINK
import com.stocksexchange.android.REQUEST_CODE_SHARE_VIA
import com.stocksexchange.android.receivers.CustomTabsReceiver
import com.stocksexchange.android.ui.utils.extensions.getCompatColor
import com.stocksexchange.android.utils.providers.CustomTabsProvider

/**
 * A helper class used for providing browser-related functionality.
 */
class BrowserHandler(private val customTabsProvider: CustomTabsProvider) {


    /**
     * Launches a browser (either Chrome Custom Tabs or a standard web-view
     * if Chrome is not installed on the device) to view a specific url.
     *
     * @param url The url to view
     */
    fun launchBrowser(context: Context, url: String) {
        val uri = Uri.parse(url)

        if(customTabsProvider.hasSupportForCustomTabs()) {
            val intentBuilder = CustomTabsIntent.Builder()
            intentBuilder.setToolbarColor(context.getCompatColor(R.color.colorPrimary))
            intentBuilder.setSecondaryToolbarColor(context.getCompatColor(R.color.colorPrimaryDark))
            intentBuilder.setShowTitle(true)
            intentBuilder.addMenuItem(
                context.getString(R.string.menu_item_share_via),
                CustomTabsReceiver.init(context, uri, REQUEST_CODE_SHARE_VIA)
            )
            intentBuilder.addMenuItem(
                context.getString(R.string.menu_item_copy_link),
                CustomTabsReceiver.init(context, uri, REQUEST_CODE_COPY_LINK)
            )

            val customTabsIntent = intentBuilder.build()
            customTabsIntent.intent.`package` = customTabsProvider.getPackageNameToUse()
            customTabsIntent.launchUrl(context, uri)
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri

            context.startActivity(intent)
        }
    }


}