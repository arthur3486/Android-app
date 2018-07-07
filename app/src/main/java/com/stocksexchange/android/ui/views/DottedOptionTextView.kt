package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.Color
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.utils.extensions.getDottedLineDrawable
import kotlinx.android.synthetic.main.dotted_option_text_view_layout.view.*

/**
 * A dotted TextViews container in a key-value format.
 *
 * todo(28.06.2018) Needs to be renamed in the future.
 */
class DottedOptionTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {


    companion object {

        const val DEFAULT_TEXT_SIZE = 14f
        const val DEFAULT_TEXT_COLOR = Color.BLACK

    }




    init {
        View.inflate(context, R.layout.dotted_option_text_view_layout, this)

        context.theme.obtainStyledAttributes(attrs, R.styleable.DottedOptionTextView, defStyleAttr, 0).run {
            try {
                // Title related
                setTitleText(getString(R.styleable.DottedOptionTextView_title) ?: "")
                setTitleSize(getDimension(R.styleable.DottedOptionTextView_titleSize, DEFAULT_TEXT_SIZE), TypedValue.COMPLEX_UNIT_PX)
                setTitleColor(getColor(R.styleable.DottedOptionTextView_titleColor, DEFAULT_TEXT_COLOR))

                // Text related
                setValueText(getString(R.styleable.DottedOptionTextView_value) ?: "")
                setValueSize(getDimension(R.styleable.DottedOptionTextView_valueSize, DEFAULT_TEXT_SIZE), TypedValue.COMPLEX_UNIT_PX)
                setValueColor(getColor(R.styleable.DottedOptionTextView_valueColor, DEFAULT_TEXT_COLOR))

                // Separator related
                setSeparatorColor(getColor(R.styleable.DottedOptionTextView_separatorColor, DEFAULT_TEXT_COLOR))
            } finally {
                recycle()
            }
        }
    }


    /**
     * Sets the text of the title.
     *
     * @param text The text to set
     */
    fun setTitleText(text: CharSequence) {
        titleTv.text = text
    }


    /**
     * Sets the text size of the title.
     *
     * @param textSize The text size to set
     * @param unit The text size's unit. Default is SP.
     */
    fun setTitleSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_SP) {
        titleTv.setTextSize(unit, textSize)
    }


    /**
     * Sets the color of the title.
     *
     * @param color The color to set
     */
    fun setTitleColor(color: Int) {
        titleTv.setTextColor(color)
    }


    /**
     * Sets the text of the value.
     *
     * @param text The text to set
     */
    fun setValueText(text: CharSequence) {
        valueTv.text = text
    }


    /**
     * Sets the text size of the value.
     *
     * @param textSize The text size to set
     * @param unit The text size's unit. Default is SP.
     */
    fun setValueSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_SP) {
        valueTv.setTextSize(unit, textSize)
    }


    /**
     * Sets the color of the value.
     *
     * @param color The color to set
     */
    fun setValueColor(color: Int) {
        valueTv.setTextColor(color)
    }


    /**
     * Sets the movement method of the value.
     *
     * @param method The movement method to set
     */
    fun setValueMovementMethod(method: MovementMethod) {
        valueTv.movementMethod = method
    }


    /**
     * Sets a color of the separator.
     *
     * @param color The color to set
     */
    fun setSeparatorColor(color: Int) {
        separatorIv.setImageDrawable(context.getDottedLineDrawable(color, true))
    }


}