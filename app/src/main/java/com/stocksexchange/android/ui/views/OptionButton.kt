package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.stocksexchange.android.R
import com.stocksexchange.android.utils.helpers.containsBits
import kotlinx.android.synthetic.main.option_button_layout.view.*

/**
 * A button with borders functionality.
 */
class OptionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    companion object {

        const val DEFAULT_TEXT_SIZE = 14f
        const val DEFAULT_TEXT_COLOR = Color.BLACK

        const val BORDER_NONE = 0
        const val BORDER_LEFT = 1
        const val BORDER_TOP = 2
        const val BORDER_RIGHT = 4
        const val BORDER_BOTTOM = 8
        const val BORDER_ALL = 15

    }




    init {
        View.inflate(context, R.layout.option_button_layout, this)
        isClickable = true

        context.theme.obtainStyledAttributes(attrs, R.styleable.OptionButton, defStyleAttr, 0).run {
            try {
                // Title related
                setTitleText(getString(R.styleable.OptionButton_title) ?: "")
                setTitleSize(getDimension(R.styleable.OptionButton_titleSize, DEFAULT_TEXT_SIZE), TypedValue.COMPLEX_UNIT_PX)
                setTitleColor(getColor(R.styleable.OptionButton_titleColor, DEFAULT_TEXT_COLOR))

                // Text related
                setSubtitleText(getString(R.styleable.OptionButton_subtitle) ?: "")
                setSubtitleSize(getDimension(R.styleable.OptionButton_subtitleSize, DEFAULT_TEXT_SIZE), TypedValue.COMPLEX_UNIT_PX)
                setSubtitleColor(getColor(R.styleable.OptionButton_subtitleColor, DEFAULT_TEXT_COLOR))

                // Borders related
                setBorders(getInteger(R.styleable.OptionButton_borders, BORDER_NONE))
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
    fun setTitleText(text: CharSequence) {
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
     * Sets a color of the title.
     *
     * @param color The color to set
     */
    fun setTitleColor(color: Int) {
        titleTv.setTextColor(color)
    }


    /**
     * Sets a text of the subtitle.
     *
     * @param text The text to set
     */
    fun setSubtitleText(text: CharSequence) {
        subtitleTv.text = text
    }


    /**
     * Sets a text size of the subtitle.
     *
     * @param textSize The text size to set
     * @param unit The text size's unit. Default is SP.
     */
    fun setSubtitleSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_SP) {
        subtitleTv.setTextSize(unit, textSize)
    }


    /**
     * Sets a color of the subtitle.
     *
     * @param color The color to set
     */
    fun setSubtitleColor(color: Int) {
        subtitleTv.setTextColor(color)
    }


    /**
     * Sets borders of this button.
     *
     * @param borders The borders to set
     */
    fun setBorders(borders: Int) {
        if(borders == BORDER_NONE) {
            return
        }

        setBackgroundResource(when {
            containsBits(borders, BORDER_ALL) -> R.drawable.option_button_all_borders_drawable
            containsBits(borders, BORDER_LEFT or BORDER_TOP or BORDER_BOTTOM) -> R.drawable.option_button_left_top_bottom_borders_drawable
            containsBits(borders, BORDER_TOP or BORDER_RIGHT or BORDER_BOTTOM) -> R.drawable.option_button_top_right_bottom_borders_drawable
            containsBits(borders, BORDER_BOTTOM or BORDER_TOP) -> R.drawable.option_button_vertical_borders_drawable
            containsBits(borders, BORDER_LEFT or BORDER_TOP) -> R.drawable.option_button_left_top_borders_drawable
            containsBits(borders, BORDER_TOP or BORDER_RIGHT) -> R.drawable.option_button_top_right_borders_drawable

            else -> R.drawable.option_button_bottom_border_drawable
        })
    }


}