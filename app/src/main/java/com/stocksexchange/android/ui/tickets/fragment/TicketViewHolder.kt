package com.stocksexchange.android.ui.tickets.fragment

import android.view.View
import android.widget.TextView
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.api.model.TicketCategories.*
import com.stocksexchange.android.api.model.TicketStatuses.*
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseViewHolder
import com.stocksexchange.android.ui.tickets.fragment.TicketResources.Companion.STRING_AWAITING_CUSTOMER_RESPONSE
import com.stocksexchange.android.ui.tickets.fragment.TicketResources.Companion.STRING_AWAITING_SUPPORT_RESPONSE
import com.stocksexchange.android.ui.tickets.fragment.TicketResources.Companion.STRING_CLOSED
import com.stocksexchange.android.ui.tickets.fragment.TicketResources.Companion.STRING_DEPOSIT_PROBLEM
import com.stocksexchange.android.ui.tickets.fragment.TicketResources.Companion.STRING_DIVIDEND_PROBLEM
import com.stocksexchange.android.ui.tickets.fragment.TicketResources.Companion.STRING_ORDER_PROBLEM
import com.stocksexchange.android.ui.tickets.fragment.TicketResources.Companion.STRING_OTHER
import com.stocksexchange.android.ui.tickets.fragment.TicketResources.Companion.STRING_WITHDRAWAL_PROBLEM
import com.stocksexchange.android.ui.utils.extensions.getCompatColor
import com.stocksexchange.android.ui.utils.extensions.getLayerDrawable
import com.stocksexchange.android.ui.utils.extensions.truncate
import com.stocksexchange.android.ui.views.DottedOptionTextView

class TicketViewHolder(
    itemView: View,
    resources: TicketResources?
) : BaseViewHolder<Ticket, TicketResources>(itemView, resources) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.ticket_item_layout

    }


    val mStatusTv: TextView = itemView.findViewById(R.id.statusTv)

    val mIdDotv: DottedOptionTextView = itemView.findViewById(R.id.idDotv)
    val mCategoryDotv: DottedOptionTextView = itemView.findViewById(R.id.categoryDotv)
    val mSubjectDotv: DottedOptionTextView = itemView.findViewById(R.id.subjectDotv)
    val mUpdatedAtDotv: DottedOptionTextView = itemView.findViewById(R.id.updatedAtDotv)
    val mCreatedAtDotv: DottedOptionTextView = itemView.findViewById(R.id.createdAtDotv)




    override fun bind(itemModel: Ticket, resources: TicketResources?) {
        val strings = resources!!.strings

        if(itemModel.statusId == CLOSED.id) {
            mStatusTv.setTextColor(itemView.context.getCompatColor(R.color.colorGreenAccent))
            mStatusTv.background = itemView.context.getLayerDrawable(
                R.drawable.secondary_button_background,
                R.color.colorGreenAccent,
                R.color.colorCardView
            )
        } else {
            mStatusTv.setTextColor(itemView.context.getCompatColor(R.color.colorBlueAccent))
            mStatusTv.background = itemView.context.getLayerDrawable(
                R.drawable.secondary_button_background,
                R.color.colorBlueAccent,
                R.color.colorCardView
            )
        }

        mStatusTv.text = when(itemModel.statusId) {
            AWAITING_CUSTOMER_RESPONSE.id -> strings[STRING_AWAITING_CUSTOMER_RESPONSE]
            AWAITING_SUPPORT_RESPONSE.id -> strings[STRING_AWAITING_SUPPORT_RESPONSE]

            else -> strings[STRING_CLOSED]
        }

        mIdDotv.setValueText(itemModel.id.toString())

        mCategoryDotv.setValueText(when(itemModel.categoryId) {
            ORDER_PROBLEM.id -> strings[STRING_ORDER_PROBLEM]
            DEPOSIT_PROBLEM.id -> strings[STRING_DEPOSIT_PROBLEM]
            WITHDRAWAL_PROBLEM.id -> strings[STRING_WITHDRAWAL_PROBLEM]
            DIVIDEND_PROBLEM.id -> strings[STRING_DIVIDEND_PROBLEM]

            else -> strings[STRING_OTHER]
        })

        mSubjectDotv.setValueText(itemModel.subject.truncate(25))
        mUpdatedAtDotv.setValueText(resources.timeFormatter.formatDate(itemModel.updatedAt))
        mCreatedAtDotv.setValueText(resources.timeFormatter.formatDate(itemModel.createdAt))
    }


}