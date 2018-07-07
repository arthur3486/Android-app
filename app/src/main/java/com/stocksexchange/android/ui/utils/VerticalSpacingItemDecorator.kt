package com.stocksexchange.android.ui.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * A item decorator for applying vertical spacing
 * between [RecyclerView] items.
 */
class VerticalSpacingItemDecorator(
    private val verticalSpacing: Int,
    private val verticalSpacingCompensation: Int
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if(parent.getChildAdapterPosition(view) != (parent.adapter.itemCount - 1)) {
            outRect.bottom = (verticalSpacing - verticalSpacingCompensation)
        } else {
            outRect.bottom = verticalSpacing
        }
    }


}