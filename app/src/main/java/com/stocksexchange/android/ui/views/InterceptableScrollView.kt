package com.stocksexchange.android.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView


/**
 * A wrapper around [ScrollView] with a touch intercepting
 * functionality built-in.
 */
class InterceptableScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {


    /**
     * A flag indicating whether this view has been pressed or not.
     */
    private var mIsPressedDown: Boolean = false

    /**
     * A listener to invoke when a user pressed and then released this view.
     */
    var mInterceptableClickListener: View.OnClickListener? = null




    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action) {

            MotionEvent.ACTION_DOWN -> {
                mIsPressedDown = true
            }

            MotionEvent.ACTION_UP -> {
                if(mIsPressedDown) {
                    mInterceptableClickListener?.onClick(this)
                    mIsPressedDown = false
                }
            }

        }

        return super.onTouchEvent(ev)
    }


}