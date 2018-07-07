package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import com.stocksexchange.android.R
import kotlinx.android.synthetic.main.option_text_view_layout.view.*

/**
 * A container of two TextViews separated by a colon.
 *
 * todo(28.06.2018) Needs to be renamed in the future.
 */
class OptionTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    companion object {

        const val DEFAULT_TEXT_SIZE = 14f
        const val DEFAULT_TEXT_COLOR = Color.BLACK

    }




    init {
        View.inflate(context, R.layout.option_text_view_layout, this)
        orientation = HORIZONTAL

        context.theme.obtainStyledAttributes(attrs, R.styleable.OptionTextView, defStyleAttr, 0).run {
            try {
                // Title related
                setTitleText(getString(R.styleable.OptionTextView_title) ?: "")
                setTitleSize(getDimension(R.styleable.OptionTextView_titleSize, DEFAULT_TEXT_SIZE), TypedValue.COMPLEX_UNIT_PX)
                setTitleColor(getColor(R.styleable.OptionTextView_titleColor, DEFAULT_TEXT_COLOR))

                // Text related
                setValueText(getString(R.styleable.OptionTextView_value) ?: "")
                setValueSize(getDimension(R.styleable.OptionTextView_valueSize, DEFAULT_TEXT_SIZE), TypedValue.COMPLEX_UNIT_PX)
                setValueColor(getColor(R.styleable.OptionTextView_valueColor, DEFAULT_TEXT_COLOR))
            } finally {
                recycle()
            }
        }
    }


    /**
     * Sets a text of the title.
     *
     * @param text The text to set
     */
    fun setTitleText(text: String) {
        titleTv.text = text
    }


    /**
     * Sets a text size of the title.
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
     * Sets a text of the value.
     *
     * @param text The text to set
     */
    fun setValueText(text: String) {
        valueTv.text = text
    }


    /**
     * Sets a text size of the value.
     *
     * @param textSize The text size to set
     * @param unit The text size's unit. Default is SP.
     */
    fun setValueSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_SP) {
        valueTv.setTextSize(unit, textSize)
    }


    /**
     * Sets a color of the value.
     *
     * @param color The color to set
     */
    fun setValueColor(color: Int) {
        valueTv.setTextColor(color)
    }


}