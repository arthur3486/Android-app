package com.stocksexchange.android.ui.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.stocksexchange.android.R
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Orders
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Orders.ASC
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Orders.DESC
import com.stocksexchange.android.ui.utils.extensions.*

/**
 * A view holding functionality for sorting currency markets.
 */
class SortPanelTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {


    companion object {

        private const val ANIMATION_DURATION = 300L

        private val INTERPOLATOR = LinearInterpolator()

    }


    /**
     * An map of drawables used for animating compound drawables.
     */
    private val mArrowDrawables: Map<Orders, Drawable?> = mapOf(
        ASC to context.getCompatDrawable(R.drawable.arrow_down_rotation_drawable),
        DESC to context.getCompatDrawable(R.drawable.arrow_up_rotation_drawable)
    )

    /**
     * A value animator used for animating arrow
     * drawables when the comparator changes.
     */
    private var mValueAnimator: ValueAnimator = ValueAnimator.ofInt(0, 10000)

    /**
     * Current comparator associated with this TextView.
     */
    lateinit var mComparator: CurrencyMarketComparator



    init {
        mValueAnimator.addUpdateListener { getRightDrawable()?.level = (it.animatedValue as Int) }
        mValueAnimator.interpolator = INTERPOLATOR
        mValueAnimator.duration = ANIMATION_DURATION
    }




    /**
     * Calls the parent implementation as well as modifies
     * the view itself to signify that it is selected.
     */
    override fun setSelected(isSelected: Boolean) {
        super.setSelected(isSelected)

        if(isSelected) {
            setTypefaceStyle(Typeface.BOLD)
            setTextColor(context.getCompatColor(R.color.colorMarketsSortPanelSelected))
            updateDrawable(false)
        } else {
            setTypefaceStyle(Typeface.NORMAL)
            setTextColor(context.getCompatColor(R.color.colorMarketsSortPanelUnselected))
            cancelAnimation()
            clearDrawable()
        }
    }


    /**
     * Switches the comparator, that is, exchanges the ascending
     * version for the descending one or vice-versa as well as
     * updates the compound drawable.
     */
    fun switchComparator() {
        mComparator = !mComparator
        updateDrawable(true)
    }


    /**
     * Updates the right compound drawable associated with this TextView.
     *
     * @param animate Whether to animate the update or not
     */
    private fun updateDrawable(animate: Boolean) {
        val newRightDrawable = mArrowDrawables[mComparator.order]

        if(animate) {
            cancelAnimation()

            mValueAnimator.removeAllListeners()
            mValueAnimator.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    getRightDrawable()?.level = 0
                    setRightDrawable(newRightDrawable)
                }

            })
            mValueAnimator.start()
        } else {
            setRightDrawable(newRightDrawable)
        }
    }


    /**
     * Cancels the current animation being performed on the drawable.
     */
    private fun cancelAnimation() {
        if(mValueAnimator.isRunning) {
            mValueAnimator.cancel()
        }
    }


}