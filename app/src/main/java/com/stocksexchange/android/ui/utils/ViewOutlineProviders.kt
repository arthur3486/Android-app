package com.stocksexchange.android.ui.utils

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/**
 * Providers for views outlining.
 */
@SuppressWarnings("NewApi")
class ViewOutlineProviders {


    companion object {

        val CIRCLE = object : ViewOutlineProvider() {

            override fun getOutline(view: View, outline: Outline) {
                outline.setOval(
                    view.paddingLeft,
                    view.paddingTop,
                    view.measuredWidth,
                    view.measuredHeight
                )
            }

        }

    }


}