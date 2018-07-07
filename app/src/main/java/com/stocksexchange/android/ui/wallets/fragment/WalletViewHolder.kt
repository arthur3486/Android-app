package com.stocksexchange.android.ui.wallets.fragment

import android.view.View
import android.widget.TextView
import com.stocksexchange.android.R
import com.stocksexchange.android.model.Wallet
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseViewHolder
import com.stocksexchange.android.ui.utils.extensions.disable
import com.stocksexchange.android.ui.utils.extensions.enable
import com.stocksexchange.android.ui.utils.extensions.getLayerDrawable
import com.stocksexchange.android.ui.utils.extensions.makeGone
import com.stocksexchange.android.ui.views.DottedOptionTextView
import com.stocksexchange.android.ui.wallets.fragment.WalletsResources.Companion.COLOR_GREEN
import com.stocksexchange.android.ui.wallets.fragment.WalletsResources.Companion.COLOR_RED
import com.stocksexchange.android.ui.wallets.fragment.WalletsResources.Companion.STRING_ACTIVE
import com.stocksexchange.android.ui.wallets.fragment.WalletsResources.Companion.STRING_DISABLED

class WalletViewHolder(
    itemView: View,
    resources: WalletsResources?
) : BaseViewHolder<Wallet, WalletsResources>(itemView, resources) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.wallet_item_layout

    }


    val mCurrencyNameTv: TextView = itemView.findViewById(R.id.currencyNameTv)
    val mCurrencyLongNameTv: TextView = itemView.findViewById(R.id.currencyLongNameTv)

    val mStatusDotv: DottedOptionTextView = itemView.findViewById(R.id.statusDotv)
    val mAvailableBalanceDotv: DottedOptionTextView = itemView.findViewById(R.id.availableBalanceDotv)
    val mBalanceInOrdersDotv: DottedOptionTextView = itemView.findViewById(R.id.balanceInOrdersDotv)

    val mDepositTvBtn: TextView = itemView.findViewById(R.id.depositTvBtn)
    val mWithdrawTvBtn: TextView = itemView.findViewById(R.id.withdrawTvBtn)




    override fun bind(itemModel: Wallet, resources: WalletsResources?) {
        mCurrencyNameTv.background = itemView.context.getLayerDrawable(
            R.drawable.secondary_button_background,
            R.color.colorYellowAccent,
            R.color.colorCardView
        )
        mCurrencyNameTv.text = itemModel.currency.name

        mCurrencyLongNameTv.text = itemModel.currency.longName

        if(itemModel.currency.isActive) {
            mStatusDotv.setValueColor(resources!!.colors[COLOR_GREEN])
            mStatusDotv.setValueText(resources.strings[STRING_ACTIVE])

            mDepositTvBtn.enable(true)
            mWithdrawTvBtn.enable(true)
        } else {
            mStatusDotv.setValueColor(resources!!.colors[COLOR_RED])
            mStatusDotv.setValueText(resources.strings[STRING_DISABLED])

            mDepositTvBtn.disable(true)
            mWithdrawTvBtn.disable(true)
        }

        mAvailableBalanceDotv.setValueText(resources.formatter.formatPrice(itemModel.availableBalance))
        mBalanceInOrdersDotv.setValueText(resources.formatter.formatPrice(itemModel.balanceInOrders))

        mDepositTvBtn.background = itemView.context.getLayerDrawable(
            R.drawable.secondary_button_background,
            R.color.colorBlueAccent,
            R.color.colorCardView
        )
        mWithdrawTvBtn.background = itemView.context.getLayerDrawable(
            R.drawable.secondary_button_background,
            R.color.colorBlueAccent,
            R.color.colorCardView
        )

        //todo
        mWithdrawTvBtn.makeGone()
    }


    fun setOnDepositButtonClickListener(position: Int, itemModel: Wallet, listener: ((View, Wallet, Int) -> Unit)?) {
        mDepositTvBtn.setOnClickListener { listener?.invoke(it, itemModel, position) }
    }


    fun setOnWithdrawButtonClickListnener(position: Int, itemModel: Wallet, listener: ((View, Wallet, Int) -> Unit)?) {
        mWithdrawTvBtn.setOnClickListener { listener?.invoke(it, itemModel, position) }
    }


}