package com.stocksexchange.android.ui.wallets.fragment

import com.stocksexchange.android.R
import com.stocksexchange.android.model.DataTypes
import com.stocksexchange.android.model.Wallet
import com.stocksexchange.android.model.WalletParameters
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.android.ui.base.mvp.presenters.BaseListDataLoadingPresenter
import org.koin.standalone.inject

class WalletsPresenter(
    model: WalletsModel,
    view: WalletsContract.View
) : BaseListDataLoadingPresenter<
        WalletsModel,
        WalletsContract.View,
        List<Wallet>,
        WalletParameters
        >(model, view), WalletsContract.ActionListener, WalletsModel.ActionListener {


    private val mStringProvider: StringProvider by inject()




    init {
        model.setActionListener(this)
    }


    constructor(view: WalletsContract.View): this(WalletsModel(), view)


    override fun start() {
        mModel.start()
        registerEventBusIfNecessary()

        if(mView.isDataSourceEmpty()) {
            mView.hideMainView()
            mView.showEmptyView()
        }

        if((mView.isDataSourceEmpty() || !mModel.mWasLastDataFetchingSuccessful)
                && !mModel.mIsDataLoading
                && mView.isViewSelected()
                && mModel.isDataLoadingIntervalApplied()) {
            loadData(DataTypes.NEW_DATA, false)
        }
    }


    override fun getDataLoadingParams(): WalletParameters {
        return mView.getWalletParameters()
    }


    override fun onDepositButtonClicked(wallet: Wallet) {
        mView.launchDepositActivity(wallet)
    }


    override fun onWithdrawButtonClicked(wallet: Wallet) {
        //view.launchWithdrawalActivity(wallet)
        mView.showLongToast(mStringProvider.getString(R.string.withdrawals_disabled))
    }


    override fun onEmptyWalletsFlagChanged(shouldShowEmptyWallets: Boolean) {
        reloadData()
    }


    override fun toString(): String {
        return "${super.toString()}_${mView.getWalletParameters().walletMode.name}"
    }


}