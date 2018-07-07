package com.stocksexchange.android.ui.utils.extensions

import android.support.v7.widget.RecyclerView

/**
 * Disables all animations for the [RecyclerView].
 */
fun RecyclerView.disableAnimations() {
    if (itemAnimator != null) {
        itemAnimator = null
    }
}