package com.stocksexchange.android.ui.help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import com.stocksexchange.android.R
import com.stocksexchange.android.model.HelpItem
import com.stocksexchange.android.ui.about.AboutActivity
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.feedback.FeedbackActivity
import com.stocksexchange.android.ui.utils.VerticalSpacingItemDecorator
import com.stocksexchange.android.ui.utils.extensions.makeVisible
import kotlinx.android.synthetic.main.help_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.jetbrains.anko.dimen
import org.jetbrains.anko.intentFor
import java.io.Serializable

class HelpActivity : BaseActivity<HelpPresenter>(), HelpContract.View {


    companion object {

        private const val SAVED_STATE_ITEMS = "items"


        fun newInstance(context: Context): Intent {
            return context.intentFor<HelpActivity>()
        }

    }


    /**
     * Items that this fragment's adapter contains.
     */
    private var mItems: MutableList<HelpItem> = mutableListOf()

    /**
     * An adapter used by this fragment's recycler view.
     */
    private lateinit var mAdapter: HelpRecyclerViewAdapter

    private var mPopupMenu: PopupMenu? = null




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.horizontal_sliding_right_to_left_enter_animation,
            R.anim.default_window_exit_animation
        )
    }


    override fun initPresenter(): HelpPresenter = HelpPresenter(this)


    override fun init() {
        initToolbar()
        initRecyclerView()
    }


    private fun initToolbar() {
        toolbar.returnBackBtnIv.setOnClickListener { onBackPressed() }

        toolbar.titleTv.text = getString(R.string.help)

        toolbar.actionBtnIv.setImageResource(R.drawable.ic_dots_vertical)
        toolbar.actionBtnIv.setOnClickListener { mPresenter?.onActionButtonClicked() }
        toolbar.actionBtnIv.makeVisible()
    }


    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(VerticalSpacingItemDecorator(
            dimen(R.dimen.recycler_view_divider_size),
            dimen(R.dimen.card_view_elevation)
        ))
        recyclerView.setHasFixedSize(true)

        mAdapter = HelpRecyclerViewAdapter(this, mItems)
        mAdapter.mOnItemClickListener = onItemClickListener

        recyclerView.adapter = mAdapter
    }


    override fun showPopupMenu() {
        mPopupMenu = PopupMenu(this, toolbar.actionBtnIv)
        mPopupMenu?.inflate(R.menu.help_activity_overflow_menu)
        mPopupMenu?.setOnMenuItemClickListener {
            when(it.itemId) {

                R.id.feedback -> {
                    mPresenter?.onFeedbackMenuItemClicked()
                    true
                }

                R.id.about -> {
                    mPresenter?.onAboutMenuItemClicked()
                    true
                }

                else -> false

            }
        }

        mPopupMenu?.show()
    }


    override fun hidePopupMenu() {
        mPopupMenu?.dismiss()
    }


    override fun launchFeedbackActivity() {
        startActivity(FeedbackActivity.newInstance(this))
    }


    override fun launchAboutActivity() {
        startActivity(AboutActivity.newInstance(this))
    }


    override fun setItems(items: MutableList<HelpItem>) {
        mAdapter.setItems(items)
    }


    override fun isDataSetEmpty(): Boolean {
        return (mAdapter.itemCount == 0)
    }


    override fun getContentLayoutResourceId() = R.layout.help_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.default_window_enter_animation,
            R.anim.horizontal_sliding_right_to_left_exit_animation
        )
    }


    private val onItemClickListener: ((HelpItemViewHolder, HelpItem, Int) -> Unit) = { viewHolder, itemModel, _ ->
        val resources = mAdapter.getResources()

        if(itemModel.isCollapsed()) {
            viewHolder.expand(resources)
        } else {
            viewHolder.collapse(resources)
        }

        itemModel.toggleState()
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        if(savedState != null) {
            mItems = (savedState.getSerializable(SAVED_STATE_ITEMS) as MutableList<HelpItem>)
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putSerializable(SAVED_STATE_ITEMS, (mItems as Serializable))
    }


}