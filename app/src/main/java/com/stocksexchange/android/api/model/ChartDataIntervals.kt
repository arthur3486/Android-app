package com.stocksexchange.android.api.model

/**
 * An enumeration of all possible intervals for querying chart data.
 */
enum class ChartDataIntervals(val intervalName: String) {

    ONE_DAY("1D"),
    ONE_WEEK("1W"),
    ONE_MONTH("1M"),
    THREE_MONTHS("3M")

}