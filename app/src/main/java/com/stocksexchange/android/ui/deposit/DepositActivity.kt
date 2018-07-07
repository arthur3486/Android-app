package com.stocksexchange.android.ui.deposit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.stocksexchange.android.R
import com.stocksexchange.android.model.Wallet
import com.stocksexchange.android.ui.base.activities.BaseFragmentActivity
import com.stocksexchange.android.ui.deposit.fragment.DepositFragment
import com.stocksexchange.android.ui.utils.extensions.setColor
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.jetbrains.anko.intentFor

class DepositActivity : BaseFragmentActivity<DepositFragment, DepositActivityPresenter>(),
    DepositActivityContract.View {


    companion object {

        private const val EXTRA_WALLET = "wallet"

        private const val SAVED_STATE_WALLET = "wallet"


        fun newInstance(context: Context, wallet: Wallet): Intent {
            return context.intentFor<DepositActivity>(
                EXTRA_WALLET to wallet
            )
        }

    }


    /**
     * A wallet holding the additional deposit information.
     */
    private lateinit var mWallet: Wallet




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.horizontal_sliding_right_to_left_enter_animation,
            R.anim.default_window_exit_animation
        )
    }


    override fun initPresenter(): DepositActivityPresenter = DepositActivityPresenter(this)


    override fun init() {
        super.init()

        initToolbar()
    }


    private fun initToolbar() {
        toolbar.returnBackBtnIv.setOnClickListener { onBackPressed() }

        toolbar.titleTv.setText(R.string.action_deposit)
        toolbar.progressBar.setColor(R.color.colorProgressBar)
    }


    override fun getActivityFragment(): DepositFragment {
        return DepositFragment.newInstance(mWallet)
    }


    override fun getContentLayoutResourceId(): Int = R.layout.deposit_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.default_window_enter_animation,
            R.anim.horizontal_sliding_right_to_left_exit_animation
        )
    }


    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        if(savedState != null) {
            mWallet = (savedState.getSerializable(SAVED_STATE_WALLET) as Wallet)
        } else {
            mWallet = (intent.getSerializableExtra(EXTRA_WALLET) as Wallet)
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putSerializable(SAVED_STATE_WALLET, mWallet)
    }


}