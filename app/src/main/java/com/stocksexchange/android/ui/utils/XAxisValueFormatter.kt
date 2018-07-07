package com.stocksexchange.android.ui.utils

import android.content.Context
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.stocksexchange.android.api.model.ChartData

/**
 * Formatter used for formatting numeric X axis values
 * to the date representations.
 */
class XAxisValueFormatter(
    private val context: Context,
    private val chartData: ChartData,
    private val timeFormatter: TimeFormatter
) : IAxisValueFormatter {


    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        if(value.toInt() >= chartData.candleSticks.size) {
            return ""
        }

        return timeFormatter.formatChartXAxis(
            context,
            chartData.candleSticks[value.toInt()].date,
            chartData.interval
        )
    }


}