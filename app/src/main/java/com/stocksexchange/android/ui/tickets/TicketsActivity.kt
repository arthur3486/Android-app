package com.stocksexchange.android.ui.tickets

import android.content.Context
import android.content.Intent
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.activities.BaseFragmentActivity
import com.stocksexchange.android.ui.tickets.creation.TicketCreationActivity
import com.stocksexchange.android.ui.tickets.fragment.TicketsFragment
import com.stocksexchange.android.ui.tickets.search.TicketsSearchActivity
import com.stocksexchange.android.ui.utils.extensions.makeVisible
import kotlinx.android.synthetic.main.tickets_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.jetbrains.anko.intentFor

class TicketsActivity : BaseFragmentActivity<TicketsFragment, TicketsActivityPresenter>(),
    TicketsActivityContract.View {


    companion object {

        fun newInstance(context: Context): Intent {
            return context.intentFor<TicketsActivity>()
        }

    }




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.horizontal_sliding_right_to_left_enter_animation,
            R.anim.default_window_exit_animation
        )
    }


    override fun initPresenter(): TicketsActivityPresenter = TicketsActivityPresenter(this)


    override fun init() {
        super.init()

        initToolbar()
        initActionButtonFab()
    }


    private fun initToolbar() {
        toolbar.returnBackBtnIv.setOnClickListener { onBackPressed() }
        toolbar.titleTv.text = getString(R.string.tickets)

        toolbar.actionBtnIv.setImageResource(R.drawable.ic_search)
        toolbar.actionBtnIv.setOnClickListener { mPresenter?.onActionButtonClicked() }
        toolbar.actionBtnIv.makeVisible()
    }


    private fun initActionButtonFab() {
        actionButtonFab.setOnClickListener {
            mPresenter?.onActionButtonFabClicked()
        }
    }


    override fun launchTicketSearchActivity() {
        startActivity(TicketsSearchActivity.newInstance(this))
    }


    override fun launchTicketCreationActivity() {
        startActivity(TicketCreationActivity.newInstance(this))
    }


    override fun getActivityFragment(): TicketsFragment = TicketsFragment.newStandardInstance()


    override fun getContentLayoutResourceId(): Int = R.layout.tickets_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.default_window_enter_animation,
            R.anim.horizontal_sliding_right_to_left_exit_animation
        )
    }


}