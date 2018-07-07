package com.stocksexchange.android.ui.chart

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.ChartDataIntervals
import com.stocksexchange.android.ui.base.activities.BaseFragmentActivity
import com.stocksexchange.android.ui.chart.fragment.ChartFragment
import org.jetbrains.anko.configuration
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.portrait

class ChartActivity : BaseFragmentActivity<ChartFragment, ChartActivityPresenter>(), ChartActivityContract.View {


    companion object {

        const val EXTRA_KEY_ORIENTATION = "orientation"
        const val EXTRA_KEY_INTERVAL = "interval"

        private const val EXTRA_MARKET_NAME = "market_name"
        private const val EXTRA_INTERVAL = "interval"

        private const val SAVED_STATE_MARKET_NAME = "market_name"
        private const val SAVED_STATE_INTERVAL = "interval"


        fun newInstance(context: Context, marketName: String,
                        interval: ChartDataIntervals): Intent {
            return context.intentFor<ChartActivity>(
                EXTRA_MARKET_NAME to marketName,
                EXTRA_INTERVAL to interval
            )
        }

    }


    /**
     * A market name to load the chart data for.
     */
    private lateinit var mMarketName: String

    /**
     * An interval to load the chart data for.
     */
    private lateinit var mInterval: ChartDataIntervals




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.no_window_animation,
            R.anim.no_window_animation
        )
    }


    override fun initPresenter(): ChartActivityPresenter = ChartActivityPresenter(this)


    private fun setActivityResult() {
        val intent = Intent()
        intent.putExtra(EXTRA_KEY_ORIENTATION, configuration.orientation)
        intent.putExtra(EXTRA_KEY_INTERVAL, mFragment.getChartParameters().interval)

        setResult(Activity.RESULT_OK, intent)
    }


    override fun getActivityFragment(): ChartFragment {
        return ChartFragment.newFullscreenInstance(mMarketName, mInterval)
    }


    override fun getContentLayoutResourceId(): Int = R.layout.chart_activity_layout


    override fun onBackPressed() {
        setActivityResult()

        overridePendingTransition(
            R.anim.no_window_animation,
            R.anim.no_window_animation
        )

        super.onBackPressed()
    }


    override fun onConfigurationChanged(config: Configuration) {
        super.onConfigurationChanged(config)

        if(config.portrait) {
            onBackPressed()
        }
    }


    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        if(savedState != null) {
            with(savedState) {
                mMarketName = getString(SAVED_STATE_MARKET_NAME)
                mInterval = (getSerializable(SAVED_STATE_INTERVAL) as ChartDataIntervals)
            }
        } else {
            with(intent) {
                mMarketName = getStringExtra(EXTRA_MARKET_NAME)
                mInterval = (getSerializableExtra(SAVED_STATE_INTERVAL) as ChartDataIntervals)
            }
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putString(SAVED_STATE_MARKET_NAME, mMarketName)
        savedState.putSerializable(SAVED_STATE_INTERVAL, mInterval)
    }


}