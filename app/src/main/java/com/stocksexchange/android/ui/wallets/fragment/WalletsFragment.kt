package com.stocksexchange.android.ui.wallets.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.util.DiffUtil
import android.view.View
import android.widget.ProgressBar
import com.stocksexchange.android.R
import com.stocksexchange.android.model.Wallet
import com.stocksexchange.android.model.WalletModes
import com.stocksexchange.android.model.WalletParameters
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.ui.base.fragments.BaseListDataLoadingFragment
import com.stocksexchange.android.ui.deposit.DepositActivity
import com.stocksexchange.android.ui.utils.diffcallbacks.WalletsDiffCallback
import com.stocksexchange.android.ui.views.InfoView
import kotlinx.android.synthetic.main.wallets_fragment_layout.view.*
import org.jetbrains.anko.support.v4.ctx

class WalletsFragment : BaseListDataLoadingFragment<
    WalletsPresenter,
    List<Wallet>,
    Wallet,
    WalletsRecyclerViewAdapter
>(), WalletsContract.View {


    companion object {

        private const val SAVED_STATE_WALLET_PARAMETERS = "wallet_parameters"


        fun newStandardInstance() = newInstance(WalletModes.STANDARD)

        fun newSearchInstance() = newInstance(WalletModes.SEARCH)

        fun newInstance(walletMode: WalletModes): WalletsFragment {
            val fragment = WalletsFragment()

            fragment.mWalletParameters = fragment.mWalletParameters.copy(
                walletMode = walletMode
            )

            return fragment
        }

    }


    /**
     * Parameters for orders data loading.
     */
    private var mWalletParameters: WalletParameters = WalletParameters(
        true, "", WalletModes.STANDARD
    )




    override fun initPresenter(): WalletsPresenter = WalletsPresenter(this)


    override fun initAdapter() {
        mAdapter = WalletsRecyclerViewAdapter(ctx, mItems)
        mAdapter.mOnDepositButtonClickListener = { _, wallet, _ ->
            mPresenter?.onDepositButtonClicked(wallet)
        }
        mAdapter.mOnWithdrawButtonClickListener = { _, wallet, _ ->
            mPresenter?.onWithdrawButtonClicked(wallet)
        }
    }


    override fun addData(data: List<Wallet>) {
        val mutableData = data.toMutableList()

        if(isDataSourceEmpty()) {
            mAdapter.setItems(mutableData)
        } else {
            val result = DiffUtil.calculateDiff(WalletsDiffCallback(mItems, mutableData))
            mAdapter.setItems(mutableData, false)
            result.dispatchUpdatesTo(mAdapter)
        }
    }


    override fun onEmptyWalletsFlagChanged(shouldShowEmptyWallets: Boolean) {
        if(mWalletParameters.shouldShowEmptyWallets == shouldShowEmptyWallets) {
            return
        }

        mWalletParameters = mWalletParameters.copy(shouldShowEmptyWallets = shouldShowEmptyWallets)
        mPresenter?.onEmptyWalletsFlagChanged(shouldShowEmptyWallets)
    }


    override fun launchDepositActivity(wallet: Wallet) {
        startActivity(DepositActivity.newInstance(ctx, wallet))
    }


    override fun launchWithdrawalActivity(wallet: Wallet) {
        //todo
    }


    override fun setSearchQuery(query: String) {
        mWalletParameters = mWalletParameters.copy(searchQuery = query)
    }


    override fun getInfoViewIcon(iconProvider: InfoViewIconProvider): Drawable? {
        return iconProvider.getWalletsIconForMode(mWalletParameters.walletMode)
    }


    override fun getEmptyViewCaption(): String {
        return when(mWalletParameters.walletMode) {

            WalletModes.SEARCH -> {
                val searchQuery = mWalletParameters.searchQuery

                if(searchQuery.isNotBlank()) {
                    getString(R.string.wallets_fragment_search_no_wallets_found_template, searchQuery)
                } else {
                    getString(R.string.wallets_fragment_search_initial_message)
                }
            }

            WalletModes.STANDARD -> {
                getString(R.string.wallets_fragment_info_view_caption)
            }

        }
    }


    override fun getSearchQuery(): String = mWalletParameters.searchQuery


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getWalletParameters(): WalletParameters {
        return mWalletParameters
    }


    override fun getContentLayoutResourceId(): Int = R.layout.wallets_fragment_layout


    override fun onRestoreState(savedState: Bundle?) {
        if(savedState != null) {
            mWalletParameters = savedState.getParcelable(SAVED_STATE_WALLET_PARAMETERS)
        }

        super.onRestoreState(savedState)
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putParcelable(SAVED_STATE_WALLET_PARAMETERS, mWalletParameters)
    }


}