package com.stocksexchange.android.ui.tickets.fragment

import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.model.DataTypes
import com.stocksexchange.android.model.TicketParameters
import com.stocksexchange.android.events.TicketEvent
import com.stocksexchange.android.ui.base.mvp.presenters.BaseListDataLoadingPresenter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TicketsPresenter(
    model: TicketsModel,
    view: TicketsContract.View
) : BaseListDataLoadingPresenter<
        TicketsModel,
        TicketsContract.View,
        List<Ticket>,
        TicketParameters
        >(model, view), TicketsContract.ActionListener, TicketsModel.ActionListener {


    init {
        model.setActionListener(this)
    }


    constructor(view: TicketsContract.View): this(TicketsModel(), view)


    override fun getDataLoadingParams(): TicketParameters {
        return mView.getTicketParameters()
    }


    override fun reloadData() {
        if(mModel.mIsDataLoading) {
            return
        }

        mModel.resetParameters()

        mView.hideMainView()
        mView.hideInfoView()

        loadData(DataTypes.NEW_DATA, false)
    }


    override fun onDataLoadingSucceeded(data: List<Ticket>) {
        mView.addData(data.sortedWith(
            compareBy<Ticket> { it.statusId }
            .thenByDescending { it.updatedAt }
        ))
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(event: TicketEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        when(event.action) {

            TicketEvent.Actions.CREATED -> {
                reloadData()
            }

        }

        event.consume()
    }


    override fun canReceiveEvents(): Boolean {
        return true
    }


    override fun toString(): String {
        return "${super.toString()}_${mView.getTicketParameters().mode.name}"
    }


}