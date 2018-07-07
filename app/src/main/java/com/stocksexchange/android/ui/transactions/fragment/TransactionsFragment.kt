package com.stocksexchange.android.ui.transactions.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.util.DiffUtil
import android.view.View
import android.widget.ProgressBar
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.api.model.TransactionOperations
import com.stocksexchange.android.api.model.TransactionStatuses
import com.stocksexchange.android.model.SortTypes
import com.stocksexchange.android.model.TransactionModes
import com.stocksexchange.android.model.TransactionParameters
import com.stocksexchange.android.model.TransactionTypes
import com.stocksexchange.android.utils.handlers.BrowserHandler
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.ui.base.fragments.BaseListDataLoadingFragment
import com.stocksexchange.android.ui.utils.diffcallbacks.TransactionsDiffCallback
import com.stocksexchange.android.ui.views.InfoView
import kotlinx.android.synthetic.main.transactions_fragment_layout.view.*
import org.jetbrains.anko.support.v4.ctx
import org.koin.android.ext.android.get

class TransactionsFragment : BaseListDataLoadingFragment<
    TransactionsPresenter,
    List<Transaction>,
    Transaction,
    TransactionsRecyclerViewAdapter
>(), TransactionsContract.View {


    companion object {

        private const val SAVED_STATE_TRANSACTION_PARAMETERS = "transaction_parameters"


        fun newStandardInstance(type: TransactionTypes) = newInstance(TransactionModes.STANDARD, type)

        fun newSearchInstance(type: TransactionTypes) = newInstance(TransactionModes.SEARCH, type)

        fun newInstance(mode: TransactionModes, type: TransactionTypes): TransactionsFragment {
            val fragment = TransactionsFragment()

            fragment.mTransactionParameters = fragment.mTransactionParameters.copy(
                mode = mode,
                type = type,
                operation = type.getOperation()
            )

            return fragment
        }

    }


    /**
     * Parameters for transactions data loading.
     */
    private var mTransactionParameters: TransactionParameters = TransactionParameters(
        TransactionModes.STANDARD, TransactionTypes.DEPOSITS, "ALL",
        TransactionOperations.DEPOSIT, TransactionStatuses.ALL, SortTypes.DESC, 50, ""
    )




    override fun initPresenter(): TransactionsPresenter = TransactionsPresenter(this)


    override fun initAdapter() {
        mAdapter = TransactionsRecyclerViewAdapter(ctx, mItems)
        mAdapter.setTransactionType(mTransactionParameters.type)
        mAdapter.mOnTransactionAddressClickListener = { _, item, _ ->
            mPresenter?.onTransactionAddressClicked(item)
        }
        mAdapter.mOnTransactionIdClickListener = { _, item, _ ->
            mPresenter?.onTransactionIdClicked(item)
        }
    }


    override fun addData(data: List<Transaction>) {
        val mutableData = data.toMutableList()

        if(isDataSourceEmpty()) {
            mAdapter.setItems(mutableData)
        } else {
            val result = DiffUtil.calculateDiff(TransactionsDiffCallback(mItems, mutableData))
            mAdapter.setItems(mutableData, false)
            result.dispatchUpdatesTo(mAdapter)
        }
    }


    override fun launchBrowser(url: String) {
        get<BrowserHandler>().launchBrowser(ctx, url)
    }


    override fun setSearchQuery(query: String) {
        mTransactionParameters = mTransactionParameters.copy(searchQuery = query)
    }


    override fun getInfoViewIcon(iconProvider: InfoViewIconProvider): Drawable? {
        return iconProvider.getTransactionsIconForType(
            mTransactionParameters.mode, mTransactionParameters.type
        )
    }


    override fun getEmptyViewCaption(): String {
        return when(mTransactionParameters.mode) {

            TransactionModes.SEARCH -> {
                val query = mTransactionParameters.searchQuery

                if(query.isNotBlank()) {
                    getString(when(mTransactionParameters.type) {
                        TransactionTypes.DEPOSITS -> R.string.transactions_fragment_search_no_deposits_found_template
                        TransactionTypes.WITHDRAWALS -> R.string.transactions_fragment_search_no_withdrawals_found_template
                    }, query)
                } else {
                    getString(when(mTransactionParameters.type) {
                        TransactionTypes.DEPOSITS -> R.string.transactions_fragment_search_deposits_initial_message
                        TransactionTypes.WITHDRAWALS -> R.string.transactions_fragment_search_withdrawals_initial_message
                    })
                }
            }

            TransactionModes.STANDARD -> {
                getString(when(mTransactionParameters.type) {
                    TransactionTypes.DEPOSITS -> R.string.error_no_deposits_available
                    TransactionTypes.WITHDRAWALS -> R.string.error_no_withdrawals_available
                })
            }

        }
    }


    override fun getSearchQuery(): String = mTransactionParameters.searchQuery


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getTransactionParameters(): TransactionParameters {
        return mTransactionParameters
    }


    override fun getContentLayoutResourceId(): Int = R.layout.transactions_fragment_layout


    override fun onRestoreState(savedState: Bundle?) {
        savedState?.apply {
            mTransactionParameters = getParcelable(SAVED_STATE_TRANSACTION_PARAMETERS)
        }

        super.onRestoreState(savedState)
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putParcelable(SAVED_STATE_TRANSACTION_PARAMETERS, mTransactionParameters)
    }


}