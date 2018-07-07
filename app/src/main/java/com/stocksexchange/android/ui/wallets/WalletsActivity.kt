package com.stocksexchange.android.ui.wallets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.PopupMenu
import android.view.Menu
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.activities.BaseFragmentActivity
import com.stocksexchange.android.ui.utils.extensions.makeVisible
import com.stocksexchange.android.ui.wallets.fragment.WalletsFragment
import com.stocksexchange.android.ui.wallets.search.WalletsSearchActivity
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.jetbrains.anko.intentFor

class WalletsActivity : BaseFragmentActivity<WalletsFragment, WalletsActivityPresenter>(),
    WalletsActivityContract.View {


    companion object {

        private const val SAVED_STATE_SHOULD_SHOW_EMPTY_WALLETS = "should_show_empty_wallets"

        private const val POPUP_MENU_SHOW_ZERO_BALANCES_ID = 100
        private const val POPUP_MENU_HIDE_ZERO_BALANCES_ID = 200


        fun newInstance(context: Context): Intent {
            return context.intentFor<WalletsActivity>()
        }

    }


    /**
     * A flag indicating whether to show empty wallets or not.
     */
    private var mShouldShowEmptyWallets: Boolean = true

    private var mPopupMenu: PopupMenu? = null




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.horizontal_sliding_right_to_left_enter_animation,
            R.anim.default_window_exit_animation
        )
    }


    override fun initPresenter(): WalletsActivityPresenter = WalletsActivityPresenter(this)


    override fun init() {
        super.init()

        initToolbar()
    }


    private fun initToolbar() {
        toolbar.returnBackBtnIv.setOnClickListener { onBackPressed() }
        toolbar.titleTv.text = getString(R.string.wallets)

        toolbar.actionBtnIv.setImageResource(R.drawable.ic_search)
        toolbar.actionBtnIv.setOnClickListener { mPresenter?.onActionButtonClicked() }
        toolbar.actionBtnIv.makeVisible()

        toolbar.secondaryActionBtnIv.setImageResource(R.drawable.ic_dots_vertical)
        toolbar.secondaryActionBtnIv.setOnClickListener { mPresenter?.onSecondaryActionButtonClicked() }
        toolbar.secondaryActionBtnIv.makeVisible()
    }


    override fun launchSearchActivity() {
        startActivity(WalletsSearchActivity.newInstance(this))
    }


    override fun showPopupMenu() {
        mPopupMenu = PopupMenu(this, toolbar.actionBtnIv)

        with(mPopupMenu!!) {
            if(mShouldShowEmptyWallets) {
                menu.add(
                    Menu.NONE, POPUP_MENU_HIDE_ZERO_BALANCES_ID,
                    Menu.NONE, getString(R.string.action_hide_zero_balances)
                )
            } else {
                menu.add(
                    Menu.NONE, POPUP_MENU_SHOW_ZERO_BALANCES_ID,
                    Menu.NONE, getString(R.string.action_show_zero_balances)
                )
            }

            setOnMenuItemClickListener {
                when(it.itemId) {

                    POPUP_MENU_SHOW_ZERO_BALANCES_ID,
                    POPUP_MENU_HIDE_ZERO_BALANCES_ID -> {
                        mShouldShowEmptyWallets = !mShouldShowEmptyWallets
                        mPresenter?.onEmptyWalletsFlagStateChanged(mShouldShowEmptyWallets)

                        true
                    }

                    else -> false
                }
            }

            show()
        }
    }


    override fun hidePopupMenu() {
        mPopupMenu?.dismiss()
    }


    override fun reloadWallets(shouldShowEmptyWallets: Boolean) {
        mFragment.onEmptyWalletsFlagChanged(shouldShowEmptyWallets)
    }


    override fun getActivityFragment(): WalletsFragment {
        return WalletsFragment.newStandardInstance()
    }


    override fun getContentLayoutResourceId(): Int = R.layout.wallets_activity_layout


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
            mShouldShowEmptyWallets = savedState.getBoolean(SAVED_STATE_SHOULD_SHOW_EMPTY_WALLETS, true)
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putBoolean(SAVED_STATE_SHOULD_SHOW_EMPTY_WALLETS, mShouldShowEmptyWallets)
    }


}