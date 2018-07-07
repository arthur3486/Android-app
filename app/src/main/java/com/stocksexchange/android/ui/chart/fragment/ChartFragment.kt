package com.stocksexchange.android.ui.chart.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.stocksexchange.android.R
import com.stocksexchange.android.REQUEST_CODE_CHART_ACTIVITY
import com.stocksexchange.android.api.model.ChartData
import com.stocksexchange.android.api.model.ChartDataIntervals
import com.stocksexchange.android.cache.ObjectCache
import com.stocksexchange.android.model.ChartModes
import com.stocksexchange.android.model.ChartParameters
import com.stocksexchange.android.model.SortTypes
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.ui.base.fragments.BaseDataLoadingFragment
import com.stocksexchange.android.ui.chart.ChartActivity
import com.stocksexchange.android.ui.utils.TimeFormatter
import com.stocksexchange.android.ui.utils.XAxisValueFormatter
import com.stocksexchange.android.ui.utils.extensions.getCompatColor
import com.stocksexchange.android.ui.utils.extensions.getDimension
import com.stocksexchange.android.ui.utils.extensions.setPaddingBottom
import com.stocksexchange.android.ui.utils.extensions.setPaddingTop
import com.stocksexchange.android.ui.views.InfoView
import kotlinx.android.synthetic.main.chart_fragment_layout.*
import kotlinx.android.synthetic.main.chart_fragment_layout.view.*
import org.jetbrains.anko.landscape
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.dimen

class ChartFragment : BaseDataLoadingFragment<ChartPresenter, ChartData>(),
    ChartContract.View {


    companion object {

        private const val SAVED_STATE_CHART_PARAMETERS = "chart_parameters"
        private const val SAVED_STATE_CHART_DATA = "chart_data"

        private const val CHART_SHADOW_WIDTH = 0.5f

        private val DEFAULT_CHART_DATA_INTERVAL = ChartDataIntervals.ONE_DAY


        fun newStandardInstance(marketName: String): ChartFragment {
            return newInstance(ChartModes.STANDARD, marketName, DEFAULT_CHART_DATA_INTERVAL)
        }

        fun newFullscreenInstance(marketName: String, interval: ChartDataIntervals): ChartFragment {
            return newInstance(ChartModes.FULLSCREEN, marketName, interval)
        }

        fun newInstance(mode: ChartModes, marketName: String,
                        interval: ChartDataIntervals): ChartFragment {
            val fragment = ChartFragment()

            fragment.mChartParameters = fragment.mChartParameters.copy(
                mode = mode,
                marketName = marketName,
                interval = interval
            )

            return fragment
        }

    }


    /**
     * Parameters for chart data loading.
     */
    private var mChartParameters = ChartParameters(
        ChartModes.STANDARD, "Invalid", ChartDataIntervals.ONE_DAY,
        SortTypes.ASC, 100, 1
    )

    /**
     * Data to supply for the chart.
     */
    private var mChartData: ChartData? = null




    override fun initPresenter(): ChartPresenter = ChartPresenter(this)


    override fun init() {
        super.init()

        initChart()
        initIntervalsRadioGroup()
    }


    override fun initSwipeRefreshProgressBar() {
        super.initSwipeRefreshProgressBar()

        getRefreshProgressBar().setOnChildScrollUpCallback { _, _ -> !chart.isFullyZoomedOut }
        disableRefreshProgressBar()
    }


    private fun initChart() {
        val axisTextSize = ctx.getDimension(R.dimen.chart_fragment_chart_axis_text_size)

        mRootView.chart.setOnTouchListener { _, _ ->
            // Disallowing the ViewPager to intercept the events
            // whenever the chart is zoomed in
            mRootView.parentRl.requestDisallowInterceptTouchEvent(!chart.isFullyZoomedOut)
            false
        }

        mRootView.chart.description.isEnabled = false
        mRootView.chart.setMaxVisibleValueCount(resources.getInteger(R.integer.chart_max_visible_value_count))
        mRootView.chart.setDrawGridBackground(false)
        mRootView.chart.setPinchZoom(true)
        mRootView.chart.setNoDataText("")

        mRootView.chart.xAxis.setDrawGridLines(true)
        mRootView.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        mRootView.chart.xAxis.gridColor = ctx.getCompatColor(R.color.colorChartAxisGridLineColor)
        mRootView.chart.xAxis.textColor = ctx.getCompatColor(R.color.colorChartTextColor)
        mRootView.chart.xAxis.textSize = axisTextSize

        mRootView.chart.axisLeft.setDrawGridLines(true)
        mRootView.chart.axisLeft.gridColor = ctx.getCompatColor(R.color.colorChartAxisGridLineColor)
        mRootView.chart.axisLeft.textColor = ctx.getCompatColor(R.color.colorChartTextColor)
        mRootView.chart.axisLeft.textSize = axisTextSize

        mRootView.chart.axisRight.isEnabled = false
        mRootView.chart.legend.isEnabled = false

        if(!isDataSourceEmpty()) {
            addData(mChartData!!)
        }
    }


    private fun initIntervalsRadioGroup() {
        updateIntervalsRadioGroup(mChartParameters.interval)

        mRootView.intervalsRg.setOnCheckedChangeListener { _, checkedId ->
            mPresenter?.onChartIntervalPicked(when(checkedId) {
                R.id.oneDayRb -> ChartDataIntervals.ONE_DAY
                R.id.oneWeekRb -> ChartDataIntervals.ONE_WEEK
                R.id.oneMonthRb -> ChartDataIntervals.ONE_MONTH

                else -> ChartDataIntervals.THREE_MONTHS
            })
        }
    }


    private fun updateIntervalsRadioGroup(interval: ChartDataIntervals) {
        when(interval) {
            ChartDataIntervals.ONE_DAY -> mRootView.oneDayRb.isChecked = true
            ChartDataIntervals.ONE_WEEK -> mRootView.oneWeekRb.isChecked = true
            ChartDataIntervals.ONE_MONTH -> mRootView.oneMonthRb.isChecked = true
            ChartDataIntervals.THREE_MONTHS -> mRootView.threeMonthsRb.isChecked = true
        }
    }


    override fun adjustView(view: View) {
        super.adjustView(view)

        when(view.id) {

            R.id.infoView -> {
                view.setPaddingTop(dimen(R.dimen.currency_market_preview_fragment_info_view_padding_top))
                view.setPaddingBottom(dimen(R.dimen.currency_market_preview_fragment_info_view_padding_bottom))
            }

        }
    }


    override fun addData(data: ChartData) {
        mChartData = data

        if(isDataSourceEmpty()) {
            return
        }

        val candleEntries: MutableList<CandleEntry> = mutableListOf()

        data.candleSticks.forEachIndexed { index, candleStick ->
            candleEntries.add(CandleEntry(
                index.toFloat(),
                candleStick.highPrice.toFloat(),
                candleStick.lowPrice.toFloat(),
                candleStick.openPrice.toFloat(),
                candleStick.closePrice.toFloat()
            ))
        }

        val dataSet = CandleDataSet(candleEntries, "DataSet")

        dataSet.setDrawIcons(false)
        dataSet.axisDependency = YAxis.AxisDependency.LEFT

        dataSet.shadowColor = ctx.getCompatColor(R.color.colorChartShadowColor)
        dataSet.shadowWidth = CHART_SHADOW_WIDTH

        dataSet.decreasingPaintStyle = Paint.Style.FILL
        dataSet.decreasingColor = ctx.getCompatColor(R.color.colorRedAccent)

        dataSet.increasingPaintStyle = Paint.Style.FILL
        dataSet.increasingColor = ctx.getCompatColor(R.color.colorGreenAccent)

        dataSet.valueTextColor = ctx.getCompatColor(R.color.colorChartTextColor)
        dataSet.valueTextSize = ctx.getDimension(R.dimen.chart_fragment_chart_value_text_size)

        dataSet.isHighlightEnabled = false
        dataSet.neutralColor = ctx.getCompatColor(R.color.colorBlueAccent)

        mRootView.chart.xAxis.valueFormatter = XAxisValueFormatter(
            ctx, data, TimeFormatter.getInstance(ctx)
        )

        mRootView.chart.data = CandleData(dataSet)
        mRootView.chart.extraLeftOffset = ctx.getDimension(R.dimen.chart_fragment_chart_left_offset)
        mRootView.chart.extraBottomOffset = ctx.getDimension(R.dimen.chart_fragment_chart_bottom_offset)
        mRootView.chart.invalidate()
    }


    override fun updateChartInterval(interval: ChartDataIntervals) {
        mChartParameters = mChartParameters.copy(interval = interval)
    }


    override fun clearChartData() {
        mChartData = null
        mRootView.chart.clear()
    }


    override fun enableRefreshProgressBar() {
        // Do nothing since currently refresh progress bar
        // is not supported in ChartFragment//todo
    }


    override fun isDataSourceEmpty(): Boolean {
        return (mChartData?.candleSticks?.isEmpty() ?: true)
    }


    override fun getInfoViewIcon(iconProvider: InfoViewIconProvider): Drawable? {
        return iconProvider.getChartIcon()
    }


    override fun getEmptyViewCaption(): String = getString(R.string.error_no_chart_data_available)


    override fun getMainView(): View = mRootView.chart


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getChartParameters(): ChartParameters = mChartParameters


    override fun getContentLayoutResourceId(): Int = R.layout.chart_fragment_layout


    override fun onConfigurationChanged(config: Configuration) {
        super.onConfigurationChanged(config)

        if(config.landscape) {
            startActivityForResult(
                ChartActivity.newInstance(
                    ctx, mChartParameters.marketName,
                    mChartParameters.interval
                ),
                REQUEST_CODE_CHART_ACTIVITY
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if((requestCode == REQUEST_CODE_CHART_ACTIVITY) && (resultCode == Activity.RESULT_OK)) {
            val orientation = data!!.getIntExtra(ChartActivity.EXTRA_KEY_INTERVAL, Configuration.ORIENTATION_LANDSCAPE)
            val interval = (data.getSerializableExtra(ChartActivity.EXTRA_KEY_INTERVAL) as ChartDataIntervals)

            if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }

            if(interval != mChartParameters.interval) {
                updateIntervalsRadioGroup(interval)
            }
        }
    }


    override fun onRestoreState(savedState: Bundle?) {
        if(savedState != null) {
            mChartParameters = savedState.getParcelable(SAVED_STATE_CHART_PARAMETERS)
        }

        mChartData = (ObjectCache.remove("${mPresenter!!}_$SAVED_STATE_CHART_DATA") as? ChartData)

        super.onRestoreState(savedState)
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putParcelable(SAVED_STATE_CHART_PARAMETERS, mChartParameters)

        if(mChartData != null) {
            ObjectCache.put("${mPresenter!!}_$SAVED_STATE_CHART_DATA", mChartData!!)
        }
    }


    override fun onRecycle(isChangingConfigurations: Boolean) {
        super.onRecycle(isChangingConfigurations)

        if(!isChangingConfigurations) {
            ObjectCache.remove("${mPresenter!!}_$SAVED_STATE_CHART_DATA")
        }
    }


}