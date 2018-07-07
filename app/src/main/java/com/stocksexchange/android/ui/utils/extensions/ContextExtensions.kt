package com.stocksexchange.android.ui.utils.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.support.annotation.AnimRes
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.content.res.AppCompatResources
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.stocksexchange.android.AT_LEAST_NOUGAT
import com.stocksexchange.android.R
import org.jetbrains.anko.dimen
import java.util.*

/**
 * Gets a layout inflater from a context.
 *
 * @return The instance of [LayoutInflater]
 */
fun Context.getLayoutInflater(): LayoutInflater {
    return LayoutInflater.from(this)
}


/**
 * Gets a compatibility drawable by using AppCompat library.
 *
 * @param id The id of the drawable
 *
 * @return The instance of [Drawable]
 */
fun Context.getCompatDrawable(@DrawableRes id: Int): Drawable? {
    return AppCompatResources.getDrawable(this, id)
}


/**
 * Gets a colored compatibility drawable.
 *
 * @param drawableId The id for the drawable
 * @param colorId The id for the color
 *
 * @return The colored drawable
 */
fun Context.getColoredCompatDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable? {
    return getCompatDrawable(drawableId)?.apply { setColor(getCompatColor(colorId)) }
}


/**
 * Gets a colored compatibility drawable.
 *
 * @param drawable The drawable to color
 * @param colorId The id for the color
 *
 * @return The colored drawable
 */
fun Context.getColoredCompatDrawable(drawable: Drawable?, @ColorRes colorId: Int): Drawable? {
    return drawable?.apply { setColor(getCompatColor(colorId)) }
}


/**
 * Gets a compatibility color.
 *
 * @param id The id for the color
 *
 * @return The compatibility color
 */
fun Context.getCompatColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}


/**
 * Gets a dimension from a context.
 *
 * @param id The id for the dimensions
 *
 * @return The dimension from the specified id
 */
fun Context.getDimension(@DimenRes id: Int): Float {
    return resources.getDimension(id)
}


/**
 * Gets an [Animation] instance from a context.
 *
 * @param resId The id for the animation
 *
 * @return The instance of [Animation] class
 */
fun Context.loadAnimation(@AnimRes resId: Int): Animation {
    return AnimationUtils.loadAnimation(this, resId)
}


/**
 * Gets a state list drawable.
 *
 * @param pressedStateDrawableId The id for the pressed state drawable
 * @param pressedStateBackgroundColor The background color for the pressed state
 * @param normalStateDrawableId The id for the normal state drawable
 * @param normalStateBackgroundColor The background color for the normal state
 *
 * @return The instance of [StateListDrawable]
 */
fun Context.getStateListDrawable(
    @DrawableRes pressedStateDrawableId: Int,
    @ColorRes pressedStateBackgroundColor: Int,
    @DrawableRes normalStateDrawableId: Int,
    @ColorRes normalStateBackgroundColor: Int
): StateListDrawable {
    val stateListDrawable = StateListDrawable()

    // Pressed state
    val pressedStateDrawable = (getCompatDrawable(pressedStateDrawableId) as GradientDrawable)
    pressedStateDrawable.setColor(getCompatColor(pressedStateBackgroundColor))

    // Normal state
    val normalStateDrawable = (getCompatDrawable(normalStateDrawableId) as GradientDrawable)
    normalStateDrawable.setColor(getCompatColor(normalStateBackgroundColor))

    // Adding the actual states
    stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressedStateDrawable)
    stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), pressedStateDrawable)
    stateListDrawable.addState(intArrayOf(), normalStateDrawable)

    return stateListDrawable
}


/**
 * Gets a layer drawable.
 *
 * @param drawableId The id for the drawable to fetch
 * @param backgroundColor The color for the background
 * @param foregroundColor The color for the foreground
 *
 * @return The layer drawable
 */
fun Context.getLayerDrawable(
    @DrawableRes drawableId: Int,
    @ColorRes backgroundColor: Int,
    @ColorRes foregroundColor: Int
): Drawable {
    val layerDrawable = (getCompatDrawable(drawableId) as LayerDrawable)

    // Background
    val backgroundDrawable = (layerDrawable.findDrawableByLayerId(R.id.background) as GradientDrawable)
    backgroundDrawable.setColor(getCompatColor(backgroundColor))

    // Foreground
    val foregroundDrawable = (layerDrawable.findDrawableByLayerId(R.id.foreground) as GradientDrawable)
    foregroundDrawable.setColor(getCompatColor(foregroundColor))

    return layerDrawable
}


/**
 * Gets a dotted-line drawable.
 *
 * @param color The color for the dots
 * @param alreadyDecoded Whether the color is already decoded or not
 *
 * @return The dotted-line drawable
 */
fun Context.getDottedLineDrawable(color: Int, alreadyDecoded: Boolean = false): Drawable {
    val drawable = (getCompatDrawable(R.drawable.dotted_line_drawable) as GradientDrawable)
    drawable.setStroke(
        dimen(R.dimen.dotted_line_separator_stroke_width),
        if(alreadyDecoded) color else getCompatColor(color),
        getDimension(R.dimen.dotted_line_separator_dash_width),
        getDimension(R.dimen.dotted_line_separator_dash_gap)
    )

    return drawable
}


/**
 * Shares a text with a specified chooser title.
 *
 * @param text The text to share
 * @param chooserTitle The title for the chooser
 */
fun Context.shareText(text: String, chooserTitle: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, text)

    startActivity(Intent.createChooser(intent, chooserTitle))
}


/**
 * Gets a valid locale from a context.
 */
@SuppressWarnings("NewApi")
fun Context.getLocale(): Locale {
    return if(AT_LEAST_NOUGAT) {
        resources.configuration.locales[0]
    } else {
        resources.configuration.locale
    }
}


/**
 * Checks whether the network is available or not.
 *
 * @return true if available; false otherwise
 */
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    return connectivityManager.activeNetworkInfo?.isAvailable ?: false
}


/**
 * Returns a key listener that supports only numbers and the decimal
 * separator suited for the current locale and the keyboard.
 *
 * Note: Due to the fact that Samsung keyboards simply do not
 * support showing comma as decimal separator (as of 13.05.2018),
 * we need to check whether the device's keyboard is from Samsung
 * and if such hardcode dot as a separator because otherwise
 * user won't have an ability to input a separator.
 *
 * @param separator The desirable separator to set
 *
 * @return The key listener tailored for decimal numbers
 */
fun Context.getDecimalNumberKeyListener(separator: Char): DigitsKeyListener {
    val keyboardName = Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
    val adjustedSeparator = if(keyboardName.toLowerCase().contains("samsung")) {
        '.'
    } else {
        separator
    }

    return DigitsKeyListener.getInstance("1234567890$adjustedSeparator")
}