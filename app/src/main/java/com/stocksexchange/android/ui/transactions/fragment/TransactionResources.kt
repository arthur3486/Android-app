package com.stocksexchange.android.ui.transactions.fragment

import android.content.Context
import com.stocksexchange.android.R
import com.stocksexchange.android.model.TransactionTypes
import com.stocksexchange.android.ui.base.adapters.recyclerview.resources.ItemResources
import com.stocksexchange.android.ui.utils.DoubleFormatter
import com.stocksexchange.android.ui.utils.TimeFormatter
import com.stocksexchange.android.ui.utils.extensions.getCompatColor
import com.stocksexchange.android.ui.utils.extensions.getLocale

class TransactionResources(
    val strings: List<String>,
    val colors: List<Int>,
    val timeFormatter: TimeFormatter,
    val doubleFormatter: DoubleFormatter
) : ItemResources {


    companion object {

        const val STRING_FINISHED = 0
        const val STRING_AWAITING_CONFIRMATIONS = 1
        const val STRING_EMAIL_SENT = 2
        const val STRING_CANCELLED_BY_USER = 3
        const val STRING_AWAITING_APPROVAL = 4
        const val STRING_APPROVED = 5
        const val STRING_NOT_CONFIRMED = 6
        const val STRING_PROCESSING = 7
        const val STRING_WITHDRAWAL_ERROR = 8
        const val STRING_CANCELLED_BY_ADMIN = 9

        const val COLOR_LINK_NORMAL_BACKGROUND = 0
        const val COLOR_LINK_SELECTED_BACKGROUND = 1


        fun newInstance(context: Context): TransactionResources {
            val strings = listOf(
                context.getString(R.string.transaction_status_finished),
                context.getString(R.string.transaction_status_awaiting_confirmations),
                context.getString(R.string.transaction_status_email_sent),
                context.getString(R.string.transaction_status_cancelled_by_user),
                context.getString(R.string.transaction_status_awaiting_approval),
                context.getString(R.string.transaction_status_approved),
                context.getString(R.string.transaction_status_not_confirmed),
                context.getString(R.string.transaction_status_processing),
                context.getString(R.string.transaction_status_withdrawal_error),
                context.getString(R.string.transaction_status_cancelled_by_admin)
            )

            val colors = listOf(
                context.getCompatColor(R.color.colorLinkNormalBackground),
                context.getCompatColor(R.color.colorLinkSelectedBackground)
            )

            val timeFormatter = TimeFormatter.getInstance(context)
            val doubleFormatter = DoubleFormatter.getInstance(context.getLocale())

            return TransactionResources(
                strings,
                colors,
                timeFormatter,
                doubleFormatter
            )
        }

    }


    var transactionType: TransactionTypes = TransactionTypes.DEPOSITS


}