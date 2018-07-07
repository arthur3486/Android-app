package com.stocksexchange.android.ui.views

import android.content.Context
import android.os.Parcelable
import android.support.v4.widget.DrawerLayout
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import com.stocksexchange.android.R
import org.jetbrains.anko.portrait

/**
 * A custom drawer layout with touch event interception functionality.
 */
class CustomDrawerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DrawerLayout(context, attrs, defStyleAttr) {


    private var mAllowIntercept: Boolean = true




    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return (mAllowIntercept && super.onInterceptTouchEvent(ev))
    }


    override fun setDrawerLockMode(lockMode: Int) {
        super.setDrawerLockMode(lockMode)

        mAllowIntercept = (lockMode != LOCK_MODE_LOCKED_OPEN)
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)

        // Closing the drawer due to DrawerLayout's state restoration
        // which saves its mode (open or closed) and restores it. Since
        // we don't want the drawer to be opened when changing orientation
        // to portrait on tablets, we have to manually close it
        if(resources.getBoolean(R.bool.isTablet) && resources.configuration.portrait) {
            closeDrawer(Gravity.START)
        }
    }


}