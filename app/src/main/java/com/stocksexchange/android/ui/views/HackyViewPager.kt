package com.stocksexchange.android.ui.views

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * A view pager with a fix for the following bug:
 * https://issuetracker.google.com/issues/36931456
 */
class HackyViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch(exception: IllegalArgumentException) {
            exception.printStackTrace()
        }

        return false
    }


}