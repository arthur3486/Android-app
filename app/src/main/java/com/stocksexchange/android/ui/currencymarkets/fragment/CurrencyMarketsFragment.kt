package com.stocksexchange.android.ui.currencymarkets.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.util.DiffUtil
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.model.CurrencyMarketTypes
import com.stocksexchange.android.model.CurrencyMarketsParameters
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.ui.base.fragments.BaseListDataLoadingFragment
import com.stocksexchange.android.ui.currencymarketpreview.CurrencyMarketPreviewActivity
import com.stocksexchange.android.ui.utils.diffcallbacks.CurrencyMarketsDiffCallback
import com.stocksexchange.android.ui.utils.interfaces.Searchable
import com.stocksexchange.android.ui.utils.interfaces.Sortable
import com.stocksexchange.android.ui.views.InfoView
import kotlinx.android.synthetic.main.currency_markets_fragment_layout.view.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.dimen

class CurrencyMarketsFragment : BaseListDataLoadingFragment<
    CurrencyMarketsPresenter,
    List<CurrencyMarket>,
    CurrencyMarket,
    CurrencyMarketsRecyclerViewAdapter
>(), CurrencyMarketsContract.View, Sortable, Searchable {


    companion object {

        private const val SAVED_STATE_IS_DATA_SET_SORTED = "is_data_set_sorted"
        private const val SAVED_STATE_CURRENCY_MARKETS_PARAMS = "currency_markets_params"


        fun newBtcInstance() = newInstance(CurrencyMarketTypes.BTC)

        fun newUsdtInstance() = newInstance(CurrencyMarketTypes.USDT)

        fun newNxtInstance() = newInstance(CurrencyMarketTypes.NXT)

        fun newLtcInstance() = newInstance(CurrencyMarketTypes.LTC)

        fun newEthInstance() = newInstance(CurrencyMarketTypes.ETH)

        fun newTusdInstance() = newInstance(CurrencyMarketTypes.TUSD)

        fun newFavoritesInstance() = newInstance(CurrencyMarketTypes.FAVORITES)

        fun newSearchInstance() = newInstance(CurrencyMarketTypes.SEARCH)

        /**
         * Creates a new fragment with a particular type and returns it.
         *
         * @param currencyMarketType The currency market type
         *
         * @return The new instance of this fragment
         */
        fun newInstance(currencyMarketType: CurrencyMarketTypes): CurrencyMarketsFragment {
            val fragment = CurrencyMarketsFragment()

            fragment.mCurrencyMarketsParams = fragment.mCurrencyMarketsParams.copy(
                currencyMarketType = currencyMarketType
            )

            return fragment
        }

    }


    /**
     * A flag indicating whether the data set is sorted or not.
     */
    private var mIsDataSetSorted: Boolean = false

    /**
     * Parameters for currency markets data loading.
     */
    private var mCurrencyMarketsParams: CurrencyMarketsParameters = CurrencyMarketsParameters(
        "", CurrencyMarketTypes.BTC
    )

    /**
     * Represents a currently selected comparator for sorting the adapter's data set.
     */
    private var mComparator: CurrencyMarketComparator? = null




    override fun initPresenter(): CurrencyMarketsPresenter = CurrencyMarketsPresenter(this)


    override fun initAdapter() {
        mAdapter = CurrencyMarketsRecyclerViewAdapter(ctx, mItems)
        mAdapter.mOnItemClickListener = onItemClickListener
    }


    override fun adjustView(view: View) {
        val canCenterView = (
            (mCurrencyMarketsParams.currencyMarketType == CurrencyMarketTypes.FAVORITES)
            || (mCurrencyMarketsParams.currencyMarketType== CurrencyMarketTypes.SEARCH)
        )
        val layoutParams: FrameLayout.LayoutParams

        when(view.id) {

            R.id.progressBar -> {
                layoutParams = (view.layoutParams as FrameLayout.LayoutParams)

                if(canCenterView) {
                    layoutParams.gravity = Gravity.CENTER
                    layoutParams.setMargins(0, 0, 0, 0)
                } else {
                    layoutParams.setMargins(0, dimen(R.dimen.fragment_progress_bar_margin_top), 0, 0)
                }

                view.layoutParams = layoutParams
            }

            R.id.infoView -> {
                layoutParams = (view.layoutParams as FrameLayout.LayoutParams)

                if(canCenterView) {
                    layoutParams.gravity = Gravity.CENTER
                }

                view.layoutParams = layoutParams
            }

        }
    }


    override fun sort(payload: Any) {
        if((payload !is CurrencyMarketComparator) || (payload.id == mComparator?.id)) {
            return
        }

        mComparator = payload
        mIsDataSetSorted = false

        sortDataSetIfNecessary()
    }


    override fun sortDataSetIfNecessary() {
        if(mIsDataSetSorted || !isInitialized() || !isSelected() || isDataSourceEmpty()) {
            return
        }

        mAdapter.sort(mComparator)
        mIsDataSetSorted = true
    }


    override fun updateItemWith(item: CurrencyMarket, position: Int) {
        mAdapter.updateItemWith(item, position)
    }


    override fun launchCurrencyMarketPreviewActivity(currencyMarket: CurrencyMarket) {
        startActivity(CurrencyMarketPreviewActivity.newInstance(ctx, currencyMarket))
    }


    override fun addData(data: List<CurrencyMarket>) {
        val mutableData = data.toMutableList()

        if(isDataSourceEmpty()) {
            mAdapter.setItems(mutableData, false)

            mIsDataSetSorted = false
            sortDataSetIfNecessary()
        } else {
            mComparator?.apply { mutableData.sortWith(this) }

            val result = DiffUtil.calculateDiff(CurrencyMarketsDiffCallback(mItems, mutableData))
            mAdapter.setItems(mutableData, false)
            result.dispatchUpdatesTo(mAdapter)
        }
    }


    override fun addCurrencyMarketChronologically(currencyMarket: CurrencyMarket) {
        mAdapter.addItem(
            currencyMarket,
            mAdapter.getChronologicalPositionForCurrencyMarket(currencyMarket, mComparator)
        )
    }


    override fun deleteCurrencyMarket(currencyMarket: CurrencyMarket) {
        mAdapter.deleteItem(currencyMarket)
    }


    override fun setSearchQuery(query: String) {
        mCurrencyMarketsParams = mCurrencyMarketsParams.copy(searchQuery = query)
    }


    override fun containsCurrencyMarket(currencyMarket: CurrencyMarket): Boolean {
        return mAdapter.contains(currencyMarket)
    }


    override fun getMarketIndexForMarketId(id: Long): Int? {
        return mAdapter.getMarketIndexForMarketId(id)
    }


    override fun getItem(position: Int): CurrencyMarket? {
        return mAdapter.getItem(position)
    }


    override fun getCurrencyMarketsParameters(): CurrencyMarketsParameters {
        return mCurrencyMarketsParams
    }


    override fun getInfoViewIcon(iconProvider: InfoViewIconProvider): Drawable? {
        return iconProvider.getIconForMarketType(mCurrencyMarketsParams.currencyMarketType)
    }


    override fun getEmptyViewCaption(): String {
        return when(mCurrencyMarketsParams.currencyMarketType) {

            CurrencyMarketTypes.SEARCH -> {
                val searchQuery = mCurrencyMarketsParams.searchQuery

                if(searchQuery.isNotBlank()) {
                    getString(R.string.currency_markets_fragment_search_no_markets_found_template, searchQuery)
                } else {
                    getString(R.string.currency_markets_fragment_search_initial_message)
                }
            }

            CurrencyMarketTypes.FAVORITES -> {
                getString(R.string.currency_markets_fragment_no_favorite_markets_message)
            }

            else -> {
                getString(
                    R.string.currency_markets_fragment_empty_view_caption,
                    mCurrencyMarketsParams.currencyMarketType.marketName
                )
            }

        }
    }


    override fun getSearchQuery(): String = mCurrencyMarketsParams.searchQuery


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getContentLayoutResourceId() = R.layout.currency_markets_fragment_layout


    override fun onBackPressed() {
        mPresenter?.onBackPressed()
    }


    private val onItemClickListener: ((View, CurrencyMarket, Int) -> Unit) = { _, item, _ ->
        mPresenter?.onCurrencyMarketItemClicked(item)
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: Bundle?) {
        savedState?.apply {
            mIsDataSetSorted = getBoolean(SAVED_STATE_IS_DATA_SET_SORTED, false)
            mCurrencyMarketsParams = getParcelable(SAVED_STATE_CURRENCY_MARKETS_PARAMS)
        }

        super.onRestoreState(savedState)
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putBoolean(SAVED_STATE_IS_DATA_SET_SORTED, mIsDataSetSorted)
        savedState.putParcelable(SAVED_STATE_CURRENCY_MARKETS_PARAMS, mCurrencyMarketsParams)
    }


}