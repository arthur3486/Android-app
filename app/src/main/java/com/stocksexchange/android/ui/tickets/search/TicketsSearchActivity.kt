package com.stocksexchange.android.ui.tickets.search

import android.content.Context
import android.content.Intent
import android.text.InputType
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.activities.BaseSearchActivity
import com.stocksexchange.android.ui.tickets.fragment.TicketsFragment
import org.jetbrains.anko.intentFor

class TicketsSearchActivity : BaseSearchActivity<TicketsFragment, TicketsSearchPresenter>(),
    TicketsSearchContract.View {


    companion object {

        fun newInstance(context: Context): Intent {
            return context.intentFor<TicketsSearchActivity>()
        }

    }




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.kitkat_scaling_window_b_enter_animation,
            R.anim.kitkat_scaling_window_a_exit_animation
        )
    }


    override fun initPresenter(): TicketsSearchPresenter = TicketsSearchPresenter(this)


    override fun performSearch(query: String) {
        mFragment.onPerformSearch(query)
    }


    override fun cancelSearch() {
        mFragment.onCancelSearch()
    }


    override fun getInputHint(): String {
        return getString(R.string.tickets_search_activity_input_hint_text)
    }


    override fun getInputType(): Int {
        return (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
    }


    override fun getActivityFragment(): TicketsFragment {
        return TicketsFragment.newSearchInstance()
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