package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.KeyListener
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.utils.extensions.getContent
import com.stocksexchange.android.ui.utils.extensions.isEmpty
import com.stocksexchange.android.ui.utils.extensions.setCursorDrawable
import kotlinx.android.synthetic.main.option_edit_text_layout.view.*

/**
 * An input container with labeling functionality.
 */
class OptionEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {


    companion object {

        const val DEFAULT_TEXT_SIZE = 14f
        const val DEFAULT_TEXT_COLOR = Color.BLACK

    }




    init {
        View.inflate(context, R.layout.option_edit_text_layout, this)

        context.theme.obtainStyledAttributes(attrs, R.styleable.OptionEditText, defStyleAttr, 0).run {
            try {
                // Title related
                setTitleText(getString(R.styleable.OptionEditText_title) ?: "")
                setTitleSize(getDimension(R.styleable.OptionEditText_titleSize, DEFAULT_TEXT_SIZE), TypedValue.COMPLEX_UNIT_PX)
                setTitleColor(getColor(R.styleable.OptionEditText_titleColor, DEFAULT_TEXT_COLOR))

                // Input related
                setInputText(getString(R.styleable.OptionEditText_inputText) ?: "")
                setInputHint(getString(R.styleable.OptionEditText_inputHint) ?: "")
                setInputSize(getDimension(R.styleable.OptionEditText_inputSize, DEFAULT_TEXT_SIZE), TypedValue.COMPLEX_UNIT_PX)
                setInputTextColor(getColor(R.styleable.OptionEditText_inputTextColor, DEFAULT_TEXT_COLOR))
                setInputHintColor(getColor(R.styleable.OptionEditText_inputHintColor, DEFAULT_TEXT_COLOR))

                // Label related
                setLabelText(getString(R.styleable.OptionEditText_label) ?: "")
                setLabelSize(getDimension(R.styleable.OptionEditText_labelSize, DEFAULT_TEXT_SIZE), TypedValue.COMPLEX_UNIT_PX)
                setLabelColor(getColor(R.styleable.OptionEditText_labelColor, DEFAULT_TEXT_COLOR))

                // Error related
                setErrorText(getString(R.styleable.OptionEditText_error) ?: "")
                setErrorSize(getDimension(R.styleable.OptionEditText_errorSize, DEFAULT_TEXT_SIZE), TypedValue.COMPLEX_UNIT_PX)
                setErrorColor(getColor(R.styleable.OptionEditText_errorColor, DEFAULT_TEXT_COLOR))
            } finally {
                recycle()
            }
        }
    }


    /**
     * Sets a background color of the input container.
     *
     * @param color The color to set
     */
    fun setInputContainerColor(color: Int) {
        inputContainerRl.setBackgroundColor(color)
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
     * Sets a color of the title.
     *
     * @param color The color to set
     */
    fun setTitleColor(color: Int) {
        titleTv.setTextColor(color)
    }


    /**
     * Sets a text of the input.
     *
     * @param text The text to set
     */
    fun setInputText(text: String) {
        inputEt.setText(text)
        inputEt.setSelection(text.length)
    }


    /**
     * Sets a hint of the input.
     *
     * @param hint The hint to set
     */
    fun setInputHint(hint: String) {
        inputEt.hint = hint
    }


    /**
     * Sets a text size of the input.
     *
     * @param textSize The text size to set
     * @param unit The text size's unit. Default is SP.
     */
    fun setInputSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_SP) {
        inputEt.setTextSize(unit, textSize)
    }


    /**
     * Sets a text color of the input.
     *
     * @param color The color to set
     */
    fun setInputTextColor(color: Int) {
        inputEt.setTextColor(color)
    }


    /**
     * Sets a color of the input hint.
     *
     * @param color The color to set
     */
    fun setInputHintColor(color: Int) {
        inputEt.setHintTextColor(color)
    }


    /**
     * Sets an type of the input.
     *
     * @param inputType The type to set
     */
    fun setInputType(inputType: Int) {
        inputEt.inputType = inputType
    }


    /**
     * Sets a cursor drawable of the input.
     *
     * @param drawable The drawable to set
     */
    fun setInputCursorDrawable(drawable: Drawable?) {
        inputEt.setCursorDrawable(drawable)
    }


    /**
     * Sets input filters of the input.
     *
     * @param filters The filters to set
     */
    fun setInputFilters(filters: Array<InputFilter>) {
        inputEt.filters = filters
    }


    /**
     * Adds a text watcher listener of the input.
     *
     * @param listener The listener to add
     */
    fun addTextChangedListener(listener: TextWatcher) {
        inputEt.addTextChangedListener(listener)
    }


    /**
     * Sets a key listener of the input.
     *
     * @param listener The listener to add
     */
    fun setKeyListener(listener: KeyListener) {
        inputEt.keyListener = listener
    }


    /**
     * Sets a text of the label.
     *
     * @param text The text to set
     */
    fun setLabelText(text: String) {
        labelTv.text = text
    }


    /**
     * Sets a text size of the label.
     *
     * @param textSize The text size to set
     * @param unit The text size's unit. Default is SP.
     */
    fun setLabelSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_SP) {
        labelTv.setTextSize(unit, textSize)
    }


    /**
     * Sets a color of the label.
     *
     * @param color The color to set
     */
    fun setLabelColor(color: Int) {
        labelTv.setTextColor(color)
    }


    /**
     * Sets a text of the error.
     *
     * @param text The text to set
     */
    fun setErrorText(text: String) {
        errorTv.text = text
    }


    /**
     * Sets a text size of the error.
     *
     * @param textSize The text size to set
     * @param unit The text size's unit. Default is SP.
     */
    fun setErrorSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_SP) {
        errorTv.setTextSize(unit, textSize)
    }


    /**
     * Sets a text color of the error.
     *
     * @param color The color to set
     */
    fun setErrorColor(color: Int) {
        errorTv.setTextColor(color)
    }


    /**
     * Checks whether the input is empty.
     *
     * @return true if empty; false otherwise
     */
    fun isInputEmpty(): Boolean {
        return inputEt.isEmpty()
    }


    /**
     * Retrieves the text of the input.
     *
     * @return The input's text
     */
    fun getInputText(): String {
        return inputEt.getContent()
    }


}