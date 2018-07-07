package com.stocksexchange.android.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.utils.extensions.getCompatColor
import com.stocksexchange.android.ui.utils.extensions.getDimension

/**
 * A class used as a tab inside of TabLayout.
 */
class CustomTab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    companion object {

        private const val DEFAULT_ANIMATION_DURATION = 250L

        private const val DEFAULT_MIN_TITLE_SCALE = 0.8f
        private const val DEFAULT_MAX_TITLE_SCALE = 1f

        private const val DEFAULT_MIN_TITLE_ALPHA = 0.5f
        private const val DEFAULT_MAX_TITLE_ALPHA = 1f

        private const val ANIMATION_TYPE_MINIMIZATION = 1
        private const val ANIMATION_TYPE_MAXIMIZATION = 2

        private val DEFAULT_INTERPOLATOR = LinearInterpolator()

    }


    var mAnimationDuration: Long = DEFAULT_ANIMATION_DURATION

    var mMinTitleScaleX: Float = DEFAULT_MIN_TITLE_SCALE
    var mMaxTitleScaleX: Float = DEFAULT_MAX_TITLE_SCALE

    var mMinTitleScaleY: Float = DEFAULT_MIN_TITLE_SCALE
    var mMaxTitleScaleY: Float = DEFAULT_MAX_TITLE_SCALE

    var mMinTitleAlpha: Float = DEFAULT_MIN_TITLE_ALPHA
    var mMaxTitleAlpha: Float = DEFAULT_MAX_TITLE_ALPHA

    var mInterpolator: Interpolator = DEFAULT_INTERPOLATOR

    private lateinit var mAnimator: ValueAnimator

    private lateinit var mTitleTv: TextView




    init {
        init(context)
    }


    /**
     * Initializing the tab.
     *
     * @param context The android context
     */
    private fun init(context: Context) {
        mTitleTv = TextView(context)
        setTitleColor(context.getCompatColor(R.color.colorPrimaryText))
        setTitleTextSize(context.getDimension(R.dimen.custom_tab_text_size), TypedValue.COMPLEX_UNIT_PX)
        maximize(false)

        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        mTitleTv.layoutParams = layoutParams

        addView(mTitleTv)
    }


    /**
     * Minimizes the tab.
     *
     * @param animate true if animate minimization; false otherwise
     */
    fun minimize(animate: Boolean) {
        if(animate) {
            startAnimation(ANIMATION_TYPE_MINIMIZATION)
        } else {
            setTitleScaleX(mMinTitleScaleX)
            setTitleScaleY(mMinTitleScaleY)
            setTitleAlpha(mMinTitleAlpha)
        }
    }


    /**
     * Maximizes the tab.
     *
     * @param animate true if animate maximization; false otherwise
     */
    fun maximize(animate: Boolean) {
        if(animate) {
            startAnimation(ANIMATION_TYPE_MAXIMIZATION)
        } else {
            setTitleScaleX(mMaxTitleScaleX)
            setTitleScaleY(mMaxTitleScaleY)
            setTitleAlpha(mMaxTitleAlpha)
        }
    }


    /**
     * Sets the X scale for the title.
     *
     * @param scaleX The scale to set
     */
    fun setTitleScaleX(scaleX: Float) {
        mTitleTv.scaleX = scaleX
    }


    /**
     * Sets the Y scale for the title.
     *
     * @param scaleY The scale to set
     */
    fun setTitleScaleY(scaleY: Float) {
        mTitleTv.scaleY = scaleY
    }


    /**
     * Sets the alpha for the title.
     *
     * @Param alpha The alpha to set
     */
    fun setTitleAlpha(alpha: Float) {
        mTitleTv.alpha = alpha
    }


    /**
     * Sets the title text.
     *
     * @param title The title text to set
     */
    fun setTitle(title: CharSequence) {
        mTitleTv.text = title
    }


    /**
     * Sets the color of the title.
     *
     * @param color The title color to set
     */
    fun setTitleColor(color: Int) {
        mTitleTv.setTextColor(color)
    }


    /**
     * Sets the text size of the title.
     *
     * @param textSize The size for the title
     * @param unit The size's unit. Default is SP.
     */
    fun setTitleTextSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_SP) {
        mTitleTv.setTextSize(unit, textSize)
    }


    /**
     * Gets a title's scale X value.
     *
     * @return The scale X value of the title
     */
    fun getTitleScaleX(): Float {
        return mTitleTv.scaleX
    }


    /**
     * Gets a title's scale Y value.
     *
     * @return The scale Y value of the title
     */
    fun getTitleScaleY(): Float {
        return mTitleTv.scaleY
    }


    /**
     * Gets a title's alpha value.
     *
     * @return The alpha of the title
     */
    fun getTitleAlpha(): Float {
        return mTitleTv.alpha
    }


    /**
     * Gets a title's text.
     *
     * @return The text of the title
     */
    fun getTitle(): CharSequence {
        return mTitleTv.text
    }


    /**
     * Gets a title's color.
     *
     * @return The color of the title
     */
    fun getTitleColor(): Int {
        return mTitleTv.textColors.defaultColor
    }


    /**
     * Gets a title's text size.
     *
     * @return The text size of the title
     */
    fun getTitleTextSize(): Float {
        return mTitleTv.textSize
    }


    /**
     * Determines whether the tab is minimized.
     *
     * @return true if the tab is minimized; false otherwise
     */
    fun isMinimized(): Boolean {
        return ((getTitleScaleX() == mMinTitleScaleX) && (getTitleScaleY() == mMinTitleScaleY) && (getTitleAlpha() == mMinTitleAlpha))
    }


    /**
     * Determines whether the tab is maximized.
     *
     * @return true if the tab is maximized; false otherwise
     */
    fun isMaximized(): Boolean {
        return ((getTitleScaleX() == mMaxTitleScaleX) && (getTitleScaleY() == mMaxTitleScaleY) && (getTitleAlpha() == mMaxTitleAlpha))
    }


    /**
     * Performs specific animation based on animation type.
     *
     * @param animationType Either [ANIMATION_TYPE_MINIMIZATION] or [ANIMATION_TYPE_MAXIMIZATION]
     */
    private fun startAnimation(animationType: Int) {
        cancelAnimation()

        val currentScaleX = getTitleScaleX()
        val currentScaleY = getTitleScaleY()
        val currentAlpha = getTitleAlpha()
        var animatedValue: Float

        mAnimator = ValueAnimator.ofFloat(0f, 1f)
        mAnimator.addUpdateListener {
            animatedValue = (it.animatedValue as Float)

            if(animationType == ANIMATION_TYPE_MAXIMIZATION) {
                setTitleScaleX(currentScaleX + ((mMaxTitleScaleX - currentScaleX) * animatedValue))
                setTitleScaleY(currentScaleY + ((mMaxTitleScaleY - currentScaleY) * animatedValue))
                setTitleAlpha(currentAlpha + ((mMaxTitleAlpha - currentAlpha) * animatedValue))
            } else if(animationType == ANIMATION_TYPE_MINIMIZATION) {
                setTitleScaleX(currentScaleX - ((currentScaleX - mMinTitleScaleX) * animatedValue))
                setTitleScaleY(currentScaleY - ((currentScaleY - mMinTitleScaleY) * animatedValue))
                setTitleAlpha(currentAlpha - ((currentAlpha - mMinTitleAlpha) * animatedValue))
            }
        }
        mAnimator.interpolator = mInterpolator
        mAnimator.duration = mAnimationDuration
        mAnimator.start()
    }


    /**
     * Cancels the currently running animation (if any).
     */
    private fun cancelAnimation() {
        if(::mAnimator.isInitialized && mAnimator.isRunning) {
            mAnimator.cancel()
        }
    }


}