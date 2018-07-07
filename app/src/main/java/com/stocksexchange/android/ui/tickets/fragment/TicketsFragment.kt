package com.stocksexchange.android.ui.tickets.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.util.DiffUtil
import android.view.View
import android.widget.ProgressBar
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.model.TicketModes
import com.stocksexchange.android.model.TicketParameters
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.ui.base.fragments.BaseListDataLoadingFragment
import com.stocksexchange.android.ui.utils.diffcallbacks.TicketsDiffCallback
import com.stocksexchange.android.ui.views.InfoView
import kotlinx.android.synthetic.main.tickets_fragment_layout.view.*
import org.jetbrains.anko.support.v4.ctx

class TicketsFragment : BaseListDataLoadingFragment<
    TicketsPresenter,
    List<Ticket>,
    Ticket,
    TicketsRecyclerViewAdapter
>(), TicketsContract.View {


    companion object {

        private const val SAVED_STATE_TICKET_PARAMETERS = "ticket_parameters"


        fun newStandardInstance() = newInstance(TicketModes.STANDARD)

        fun newSearchInstance() = newInstance(TicketModes.SEARCH)

        fun newInstance(mode: TicketModes): TicketsFragment {
            val fragment = TicketsFragment()

            fragment.mTicketParameters = fragment.mTicketParameters.copy(
                mode = mode
            )

            return fragment
        }

    }


    /**
     * Parameters for tickets data loading.
     */
    private var mTicketParameters: TicketParameters = TicketParameters()




    override fun initPresenter(): TicketsPresenter = TicketsPresenter(this)


    override fun initAdapter() {
        mAdapter = TicketsRecyclerViewAdapter(ctx, mItems)
    }


    override fun addData(data: List<Ticket>) {
        val mutableData = data.toMutableList()

        if(isDataSourceEmpty()) {
            mAdapter.setItems(mutableData)
        } else {
            val result = DiffUtil.calculateDiff(TicketsDiffCallback(mItems, mutableData))
            mAdapter.setItems(mutableData, false)
            result.dispatchUpdatesTo(mAdapter)
        }
    }


    override fun setSearchQuery(query: String) {
        mTicketParameters = mTicketParameters.copy(searchQuery = query)
    }


    override fun getInfoViewIcon(iconProvider: InfoViewIconProvider): Drawable? {
        return iconProvider.getTicketsIcon(mTicketParameters.mode)
    }


    override fun getEmptyViewCaption(): String {
        return when(mTicketParameters.mode) {

            TicketModes.SEARCH -> {
                val searchQuery = mTicketParameters.searchQuery

                if(searchQuery.isNotBlank()) {
                    getString(R.string.tickets_fragment_search_no_wallets_found_template, searchQuery)
                } else {
                    getString(R.string.tickets_fragment_search_initial_message)
                }
            }

            TicketModes.STANDARD -> {
                getString(R.string.tickets_fragment_info_view_caption)
            }

        }
    }


    override fun getSearchQuery(): String = mTicketParameters.searchQuery


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getTicketParameters(): TicketParameters {
        return mTicketParameters
    }


    override fun getContentLayoutResourceId(): Int = R.layout.tickets_fragment_layout


    override fun onRestoreState(savedState: Bundle?) {
        if(savedState != null) {
            mTicketParameters = savedState.getParcelable(SAVED_STATE_TICKET_PARAMETERS)
        }

        super.onRestoreState(savedState)
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putParcelable(SAVED_STATE_TICKET_PARAMETERS, mTicketParameters)
    }


}
