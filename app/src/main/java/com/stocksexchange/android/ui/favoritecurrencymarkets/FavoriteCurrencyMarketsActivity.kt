package com.stocksexchange.android.ui.favoritecurrencymarkets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.stocksexchange.android.R
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.LAST_VOLUME_DESCENDING_COMPARATOR
import com.stocksexchange.android.ui.base.activities.BaseFragmentActivity
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketsFragment
import com.stocksexchange.android.ui.currencymarkets.search.CurrencyMarketsSearchActivity
import com.stocksexchange.android.ui.utils.extensions.makeVisible
import kotlinx.android.synthetic.main.favorite_currency_markets_activity.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.jetbrains.anko.intentFor

class FavoriteCurrencyMarketsActivity : BaseFragmentActivity<CurrencyMarketsFragment, FavoriteCurrencyMarketsPresenter>(),
    FavoriteCurrencyMarketsContract.View {


    companion object {

        const val TAG = "FavoriteCurrencyMarketsActivity"

        private const val SAVED_STATE_COMPARATOR = "comparator"


        @JvmStatic
        fun newInstance(context: Context): Intent {
            return context.intentFor<FavoriteCurrencyMarketsActivity>()
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


    override fun initPresenter(): FavoriteCurrencyMarketsPresenter = FavoriteCurrencyMarketsPresenter(this)


    override fun init() {
        super.init()

        initToolbar()
        initCurrencyMarketsSortPanel()
    }


    private fun initToolbar() {
        toolbar.returnBackBtnIv.setOnClickListener { onBackPressed() }
        toolbar.titleTv.text = getString(R.string.favorites)

        toolbar.actionBtnIv.setImageResource(R.drawable.ic_search)
        toolbar.actionBtnIv.setOnClickListener { mPresenter?.onActionButtonClicked() }
        toolbar.actionBtnIv.makeVisible()
    }


    private fun initCurrencyMarketsSortPanel() {
        currencyMarketsSortPanel.mOnSortPanelTitleClickListener = { mFragment.sort(it.mComparator) }
        currencyMarketsSortPanel.setComparator(mComparator)
    }


    override fun launchSearchActivity() {
        startActivity(CurrencyMarketsSearchActivity.newInstance(this))
    }


    override fun getActivityFragment(): CurrencyMarketsFragment {
        return CurrencyMarketsFragment.newFavoritesInstance()
    }


    override fun getContentLayoutResourceId(): Int = R.layout.favorite_currency_markets_activity


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