package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.utils.extensions.setHorizontalPadding
import com.stocksexchange.android.ui.utils.extensions.setVerticalPadding
import kotlinx.android.synthetic.main.info_view_layout.view.*
import org.jetbrains.anko.dimen

/**
 * A view container showing an informational view
 * in case there is no data or error has occurred.
 */
class InfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    init {
        View.inflate(context, R.layout.info_view_layout, this)

        orientation = VERTICAL

        setHorizontalPadding(dimen(R.dimen.info_view_horizontal_padding))
        setVerticalPadding(dimen(R.dimen.info_view_vertical_padding))
    }


    /**
     * Sets a drawable of the icon.
     *
     * @param drawable The drawable to set
     */
    fun setIcon(drawable: Drawable?) {
        iconIv.setImageDrawable(drawable)
    }


    /**
     * Sets a text of the caption
     *
     * @param text The text to set
     */
    fun setCaption(text: String) {
        captionTv.text = text
    }


    /**
     * Sets a text resource id of the caption
     *
     * @param resId The resource id for the string
     */
    fun setCaption(@StringRes resId: Int) {
        setCaption(context.getString(resId))
    }


}