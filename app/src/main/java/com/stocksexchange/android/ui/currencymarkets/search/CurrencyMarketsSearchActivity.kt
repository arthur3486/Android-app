package com.stocksexchange.android.ui.currencymarkets.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import com.stocksexchange.android.R
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.LAST_VOLUME_DESCENDING_COMPARATOR
import com.stocksexchange.android.ui.base.activities.BaseSearchActivity
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketsFragment
import kotlinx.android.synthetic.main.currency_markets_search_activity_layout.*
import org.jetbrains.anko.intentFor

class CurrencyMarketsSearchActivity : BaseSearchActivity<CurrencyMarketsFragment, CurrencyMarketsSearchPresenter>(),
    CurrencyMarketsSearchContract.View {


    companion object {

        private const val SAVED_STATE_COMPARATOR = "comparator"


        fun newInstance(context: Context): Intent {
            return context.intentFor<CurrencyMarketsSearchActivity>()
        }

    }

    /**
     * A comparator that is currently selected by the sort panel.
     */
    // todo to be extracted to the settings in the future
    private var mComparator: CurrencyMarketComparator = LAST_VOLUME_DESCENDING_COMPARATOR




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.kitkat_scaling_window_b_enter_animation,
            R.anim.kitkat_scaling_window_a_exit_animation
        )
    }


    override fun initPresenter(): CurrencyMarketsSearchPresenter = CurrencyMarketsSearchPresenter(this)


    override fun init() {
        super.init()

        initCurrencyMarketsSortPanel()
    }


    private fun initCurrencyMarketsSortPanel() {
        currencyMarketsSortPanel.mOnSortPanelTitleClickListener = { mFragment.sort(it.mComparator) }
        currencyMarketsSortPanel.setComparator(mComparator)
    }


    override fun performSearch(query: String) {
        mFragment.onPerformSearch(query)
    }


    override fun cancelSearch() {
        mFragment.onCancelSearch()
    }


    override fun getInputHint(): String {
        return getString(R.string.currency_markets_search_activity_toolbar_query_input_hint)
    }


    override fun getInputType(): Int {
        return (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS)
    }


    override fun getActivityFragment(): CurrencyMarketsFragment {
        return CurrencyMarketsFragment.newSearchInstance()
    }


    override fun getContentLayoutResourceId() = R.layout.currency_markets_search_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.kitkat_scaling_window_a_enter_animation,
            R.anim.kitkat_scaling_window_b_exit_animation
        )
    }


    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        if(savedState != null) {
            mComparator = savedState.getParcelable(SAVED_STATE_COMPARATOR)
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putParcelable(SAVED_STATE_COMPARATOR, currencyMarketsSortPanel.getComparator())
    }


}