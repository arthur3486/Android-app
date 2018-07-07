package com.stocksexchange.android.ui.wallets.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stocksexchange.android.model.Wallet
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseRecyclerViewAdapter

class WalletsRecyclerViewAdapter(
    context: Context,
    items: MutableList<Wallet>
) : BaseRecyclerViewAdapter<Wallet, WalletViewHolder, WalletsResources>(context, items) {


    /**
     * Resources for the adapter.
     */
    private val mResources: WalletsResources = WalletsResources.newInstance(context)

    /**
     * A listener used for notifying whenever the deposit button is clicked.
     */
    var mOnDepositButtonClickListener: ((View, Wallet, Int) -> Unit)? = null

    /**
     * A listener used for notifying whenever the deposit button is clicked.
     */
    var mOnWithdrawButtonClickListener: ((View, Wallet, Int) -> Unit)? = null




    override fun onCreateViewHolder(parent: ViewGroup, inflater: LayoutInflater,
                                    resources: WalletsResources?, viewType: Int): WalletViewHolder {
        return WalletViewHolder(
            inflater.inflate(WalletViewHolder.MAIN_LAYOUT_ID, parent, false),
            resources
        )
    }


    override fun onBindViewHolder(viewHolder: WalletViewHolder, itemModel: Wallet,
                                  resources: WalletsResources?, viewType: Int) {
        viewHolder.bind(itemModel, resources)
    }


    override fun assignListeners(viewHolder: WalletViewHolder, itemModel: Wallet,
                                 position: Int, viewType: Int) {
        viewHolder.setOnDepositButtonClickListener(position, itemModel, mOnDepositButtonClickListener)
        viewHolder.setOnWithdrawButtonClickListnener(position, itemModel, mOnWithdrawButtonClickListener)
    }


    override fun getResources(): WalletsResources? {
        return mResources
    }


}