package com.stocksexchange.android.ui.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.stocksexchange.android.R
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Columns.*
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.DAILY_CHANGE_ASCENDING_COMPARATOR
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.LAST_PRICE_ASCENDING_COMPARATOR
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.LAST_VOLUME_ASCENDING_COMPARATOR
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.NAME_ASCENDING_COMPARATOR
import kotlinx.android.synthetic.main.currency_markets_sort_panel_layout.view.*

/**
 * Contains all the functionality related to sorting the currency markets.
 */
class CurrencyMarketsSortPanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    /**
     * Represents a currently selected title on the panel.
     */
    private var mSelectedTitleTv: SortPanelTextView? = null


    /**
     * A listener used for notifying whenever a title TextView is clicked.
     */
    private val mOnTitleClickListener: ((View) -> Unit) = { view ->
        if(mSelectedTitleTv?.id == view.id) {
            mSelectedTitleTv?.switchComparator()
        } else {
            val newSelectedTitleTv = (view as SortPanelTextView)

            mSelectedTitleTv?.isSelected = false
            newSelectedTitleTv.isSelected = true

            mSelectedTitleTv = newSelectedTitleTv
        }

        mOnSortPanelTitleClickListener?.invoke(mSelectedTitleTv!!)
    }


    /**
     * A listener used for notifying whenever a title is clicked on a panel.
     */
    var mOnSortPanelTitleClickListener: ((SortPanelTextView) -> Unit)? = null




    init {
        View.inflate(context, R.layout.currency_markets_sort_panel_layout, this)

        nameTitleTv.mComparator = NAME_ASCENDING_COMPARATOR
        nameTitleTv.setOnClickListener(mOnTitleClickListener)

        volumeTitleTv.mComparator = LAST_VOLUME_ASCENDING_COMPARATOR
        volumeTitleTv.setOnClickListener(mOnTitleClickListener)

        lastPriceTitleTv.mComparator = LAST_PRICE_ASCENDING_COMPARATOR
        lastPriceTitleTv.setOnClickListener(mOnTitleClickListener)

        dailyChangeTitleTv.mComparator = DAILY_CHANGE_ASCENDING_COMPARATOR
        dailyChangeTitleTv.setOnClickListener(mOnTitleClickListener)
    }


    /**
     * Selects the sort panel TextView according to the comparator.
     *
     * @param comparator The comparator to select the panel title for
     */
    fun setComparator(comparator: CurrencyMarketComparator) {
        if(mSelectedTitleTv?.mComparator?.id == comparator.id) {
            return
        }

        val newSelectedTitleTv = getTitleTvForComparator(comparator)
        newSelectedTitleTv.mComparator = comparator
        mOnTitleClickListener.invoke(newSelectedTitleTv)
    }


    /**
     * Returns a currently selected comparator or [NAME_ASCENDING_COMPARATOR] if none is selected.
     *
     * @return The currently selected comparator
     */
    fun getComparator(): CurrencyMarketComparator {
        return mSelectedTitleTv?.mComparator ?: NAME_ASCENDING_COMPARATOR
    }


    /**
     * Retrieves a sort panel TextView for a comparator.
     *
     * @param comparator The comparator to get the TextView for
     *
     * @return The sort panel TextView
     */
    private fun getTitleTvForComparator(comparator: CurrencyMarketComparator): SortPanelTextView {
        return when(comparator.column) {

            NAME -> nameTitleTv
            LAST_VOLUME -> volumeTitleTv
            LAST_PRICE -> lastPriceTitleTv
            DAILY_CHANGE -> dailyChangeTitleTv

        }
    }


}