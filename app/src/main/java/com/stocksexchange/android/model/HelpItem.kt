package com.stocksexchange.android.model

import com.stocksexchange.android.ui.help.HelpItemViewHolder
import java.io.Serializable

/**
 * A mode class holding data for [HelpItemViewHolder] class.
 */
data class HelpItem(
    val question: String,
    val answer: String,
    var state: HelpItemState = HelpItemState.COLLAPSED
) : Serializable {


    /**
     * Checks whether the item is in [HelpItemState.COLLAPSED] state.
     *
     * @return true if in collapsed state; false otherwise
     */
    fun isCollapsed() = (state == HelpItemState.COLLAPSED)


    /**
     * Checks whether the item is in [HelpItemState.EXPANDED] state.
     *
     * @return true if in expanded state; false otherwise
     */
    fun isExpanded() = (state == HelpItemState.EXPANDED)


    /**
     * Toggles the state of this help item.
     */
    fun toggleState() {
        state = !state
    }


}