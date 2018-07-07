package com.stocksexchange.android.utils.helpers

import android.media.RingtoneManager
import android.os.Build
import com.stocksexchange.android.API_VERSION
import com.stocksexchange.android.BuildConfig
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * Composes a subject for a feedback email and returns it.
 *
 * @param title The title for the email
 *
 * @return The feedback email's subject
 */
fun composeFeedbackEmailSubject(title: String): String {
    val stringBuilder = StringBuilder()

    stringBuilder.append(title).append(" v").append(BuildConfig.VERSION_NAME).append(" | ")
    stringBuilder.append("Device: ").append(Build.MODEL).append("(").append(Build.PRODUCT).append(") | ")
    stringBuilder.append("API ").append(API_VERSION)

    return stringBuilder.toString()
}


/**
 * Checks whether the source contains specified bits.
 *
 * @param source The source bits
 * @Param bits The bits to check
 */
fun containsBits(source: Int, bits: Int): Boolean {
    return ((source and bits) == bits)
}


/**
 * Returns a decimal separator according the locale.
 *
 * @param locale The locale to base the separator on
 *
 * @return The decimal separator based on the passed in locale
 */
fun getLocaleDecimalSeparator(locale: Locale): Char {
    return DecimalFormatSymbols.getInstance(locale).decimalSeparator
}


/**
 * Returns a default notification sound name.
 *
 * @return The default notification sound name
 */
fun getDefaultNotificationRingtone(): String {
    return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
}