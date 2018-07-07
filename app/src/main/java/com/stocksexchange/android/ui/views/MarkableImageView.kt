package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.*
import android.support.annotation.ColorInt
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.ImageView
import org.jetbrains.anko.sp

/**
 * A wrapper around [ImageView] with marking functionality.
 */
class MarkableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {


    companion object {

        const val DEFAULT_TEXT_SIZE = 18
        const val DEFAULT_TEXT_COLOR = Color.WHITE

    }


    /**
     * A paint used for drawing the outer background of this image view.
     */
    private var mOuterBackgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * A paint used for drawing the inner background of this image view.
     */
    private var mInnerBackgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * A paint used for drawing the text on this image view.
     */
    private var mTextPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * Coordinates of the outer background.
     */
    private var mOuterBackgroundBounds: RectF = RectF()

    /**
     * Coordinates of the inner background.
     */
    private var mInnerBackgroundBounds: RectF = RectF()

    /**
     * Coordinates of the text.
     */
    private var mTextBounds: Rect = Rect()

    /**
     * An array holding this view's dimensions (width and height).
     */
    private var mViewSize: IntArray = IntArray(2)

    /**
     * A boolean flag indicating whether to draw backgrounds on this
     * image view or not.
     */
    var mShouldDrawBackground: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    /**
     * A type of shape to draw on this image view.
     */
    var mBackgroundShape: Shape = Shape.RECTANGULARISH
        set(value) {
            field = value
            invalidate()
        }

    /**
     * A mark (letter) to draw on this image view.
     */
    var mMark: String = ""
        set(value) {
            field = value
            invalidate()
        }




    init {
        mTextPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        mTextPaint.textSize = sp(DEFAULT_TEXT_SIZE).toFloat()
        mTextPaint.color = DEFAULT_TEXT_COLOR
        mTextPaint.textAlign = Paint.Align.CENTER
    }


    /**
     * Sets the color of the outer background of this image view.
     *
     * @param color The color to set
     */
    fun setOuterBackgroundColor(@ColorInt color: Int) {
        mOuterBackgroundPaint.color = color
        invalidate()
    }


    /**
     * Sets the color of the inner background of this image view.
     */
    fun setInnerBackgroundColor(@ColorInt color: Int) {
        mInnerBackgroundPaint.color = color
        invalidate()
    }


    /**
     * Sets the text size of the text paint.
     *
     * @param textSize The text size to set (in pixels).
     */
    fun setTextSize(textSize: Int) {
        mTextPaint.textSize = textSize.toFloat()
        invalidate()
    }


    /**
     * Gets the text size of the text paint.
     *
     * @return The text size (in pixels)
     */
    fun getTextSize(): Int {
        return mTextPaint.textSize.toInt()
    }


    /**
     * Sets the text color of the text paint.
     *
     * @param textColor The text color to set
     */
    fun setTextColor(@ColorInt textColor: Int) {
        mTextPaint.color = textColor
        invalidate()
    }


    /**
     * Gets the text color of the text paint.
     *
     * @return The text color
     */
    fun getTextColor(): Int {
        return mTextPaint.color
    }


    /**
     * Checks whether this image is marked.
     *
     * @return true if marked; false otherwise
     */
    fun isMarked(): Boolean {
        return mMark.isNotBlank()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mViewSize[0] = measuredWidth
        mViewSize[1] = measuredHeight

        mOuterBackgroundBounds.set(
            0f,
            0f,
            mViewSize[0].toFloat(),
            mViewSize[1].toFloat()
        )

        mInnerBackgroundBounds.set(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (mViewSize[0] - paddingRight).toFloat(),
            (mViewSize[1] - paddingBottom).toFloat()
        )
    }


    override fun onDraw(canvas: Canvas) {
        if(mShouldDrawBackground) {
            if(mBackgroundShape == Shape.RECTANGULARISH) {
                canvas.drawRect(mOuterBackgroundBounds, mOuterBackgroundPaint)
                canvas.drawRect(mInnerBackgroundBounds, mInnerBackgroundPaint)
            } else if(mBackgroundShape == Shape.OVALISH) {
                canvas.drawOval(mOuterBackgroundBounds, mOuterBackgroundPaint)
                canvas.drawOval(mInnerBackgroundBounds, mInnerBackgroundPaint)
            }
        }

        super.onDraw(canvas)

        if(isMarked()) {
            mTextPaint.getTextBounds(mMark, 0, mMark.length, mTextBounds)

            canvas.drawText(
                mMark,
                (mViewSize[0] / 2).toFloat(),
                ((mViewSize[1] + mTextBounds.height()) / 2).toFloat(),
                mTextPaint
            )
        }
    }


    enum class Shape {

        RECTANGULARISH,
        OVALISH

    }


}