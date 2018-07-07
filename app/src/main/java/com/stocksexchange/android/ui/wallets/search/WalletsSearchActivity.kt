package com.stocksexchange.android.ui.wallets.search

import android.content.Context
import android.content.Intent
import android.text.InputType
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.activities.BaseSearchActivity
import com.stocksexchange.android.ui.wallets.fragment.WalletsFragment
import org.jetbrains.anko.intentFor

class WalletsSearchActivity : BaseSearchActivity<WalletsFragment, WalletsSearchPresenter>(),
    WalletsSearchContract.View {


    companion object {

        fun newInstance(context: Context): Intent {
            return context.intentFor<WalletsSearchActivity>()
        }

    }




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.kitkat_scaling_window_b_enter_animation,
            R.anim.kitkat_scaling_window_a_exit_animation
        )
    }


    override fun initPresenter(): WalletsSearchPresenter = WalletsSearchPresenter(this)


    override fun performSearch(query: String) {
        mFragment.onPerformSearch(query)
    }


    override fun cancelSearch() {
        mFragment.onCancelSearch()
    }


    override fun getInputHint(): String {
        return getString(R.string.wallets_search_activity_input_hint_text)
    }


    override fun getInputType(): Int {
        return (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS)
    }


    override fun getActivityFragment(): WalletsFragment {
        return WalletsFragment.newSearchInstance()
    }


    override fun getContentLayoutResourceId(): Int = R.layout.wallets_search_activity


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.kitkat_scaling_window_a_enter_animation,
            R.anim.kitkat_scaling_window_b_exit_animation
        )
    }


}