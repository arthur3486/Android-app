package com.stocksexchange.android.ui.views

import android.content.Context
import android.support.design.widget.NavigationView
import android.support.v4.view.ViewCompat
import android.support.v4.view.WindowInsetsCompat
import android.util.AttributeSet
import com.stocksexchange.android.AT_LEAST_LOLLIPOP
import com.stocksexchange.android.ui.utils.extensions.isVisible
import org.jetbrains.anko.childrenSequence

/**
 * A wrapper around the navigation view for applying
 * the insets to its header view.
 */
class CustomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NavigationView(context, attrs, defStyleAttr) {


    override fun onInsetsChanged(insets: WindowInsetsCompat) {
        getHeaderView(0).apply {
            ViewCompat.dispatchApplyWindowInsets(this, insets)

            childrenSequence().forEach {
                takeIf { isVisible() && ViewCompat.getFitsSystemWindows(it) && AT_LEAST_LOLLIPOP }
                apply { ViewCompat.dispatchApplyWindowInsets(it, insets) }
            }
        }
    }


}