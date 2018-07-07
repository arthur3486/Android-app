package com.stocksexchange.android.ui.tickets.fragment

import android.content.Context
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.adapters.recyclerview.resources.ItemResources
import com.stocksexchange.android.ui.utils.TimeFormatter

class TicketResources(
    val strings: List<String>,
    val timeFormatter: TimeFormatter
) : ItemResources {


    companion object {

        const val STRING_AWAITING_SUPPORT_RESPONSE = 0
        const val STRING_AWAITING_CUSTOMER_RESPONSE = 1
        const val STRING_CLOSED = 2

        const val STRING_ORDER_PROBLEM = 3
        const val STRING_DEPOSIT_PROBLEM = 4
        const val STRING_WITHDRAWAL_PROBLEM = 5
        const val STRING_DIVIDEND_PROBLEM = 6
        const val STRING_OTHER = 7


        fun newInstance(context: Context): TicketResources {
            val strings = listOf(
                context.getString(R.string.ticket_status_awaiting_support_response),
                context.getString(R.string.ticket_status_awaiting_customer_response),
                context.getString(R.string.ticket_status_closed),
                context.getString(R.string.ticket_category_order_problem),
                context.getString(R.string.ticket_category_deposit_problem),
                context.getString(R.string.ticket_category_withdrawal_problem),
                context.getString(R.string.ticket_category_dividend_problem),
                context.getString(R.string.ticket_category_other)
            )
            val timeFormatter = TimeFormatter.getInstance(context)

            return TicketResources(strings, timeFormatter)
        }

    }


}