package com.stocksexchange.android.model

/**
 * An enumeration of all possible states a help item can be in.
 */
enum class HelpItemState {


    COLLAPSED,
    EXPANDED;


    /**
     * Returns an opposite state.
     *
     * @return The opposite help item state
     */
    operator fun not(): HelpItemState {
        return when(this) {
            COLLAPSED -> EXPANDED
            EXPANDED -> COLLAPSED
        }
    }


}