package com.stocksexchange.android.ui.transactions.fragment

import android.text.SpannableString
import android.view.View
import android.widget.TextView
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.api.model.TransactionStatuses.*
import com.stocksexchange.android.model.TransactionTypes
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseViewHolder
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.COLOR_LINK_NORMAL_BACKGROUND
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.COLOR_LINK_SELECTED_BACKGROUND
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.STRING_APPROVED
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.STRING_AWAITING_APPROVAL
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.STRING_AWAITING_CONFIRMATIONS
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.STRING_CANCELLED_BY_ADMIN
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.STRING_CANCELLED_BY_USER
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.STRING_EMAIL_SENT
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.STRING_FINISHED
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.STRING_NOT_CONFIRMED
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.STRING_PROCESSING
import com.stocksexchange.android.ui.transactions.fragment.TransactionResources.Companion.STRING_WITHDRAWAL_ERROR
import com.stocksexchange.android.ui.utils.extensions.*
import com.stocksexchange.android.ui.utils.text.CustomLinkMovementMethod
import com.stocksexchange.android.ui.utils.text.SelectorSpan
import com.stocksexchange.android.ui.views.DottedOptionTextView

class TransactionViewHolder(
    itemView: View,
    resources: TransactionResources?
) : BaseViewHolder<Transaction, TransactionResources>(itemView, resources) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.transaction_item_layout

    }


    private enum class Styles {

        GREEN,
        RED,
        BLUE

    }


    val mStatusTv: TextView = itemView.findViewById(R.id.statusTv)
    val mCurrencyTv: TextView = itemView.findViewById(R.id.currencyTv)

    val mAmountDotv: DottedOptionTextView = itemView.findViewById(R.id.amountDotv)
    val mFeeDotv: DottedOptionTextView = itemView.findViewById(R.id.feeDotv)
    val mAddressDotv: DottedOptionTextView = itemView.findViewById(R.id.addressDotv)
    val mTransactionIdDotv: DottedOptionTextView = itemView.findViewById(R.id.transactionIdDotv)
    val mDateDotv: DottedOptionTextView = itemView.findViewById(R.id.dateDotv)




    override fun bind(itemModel: Transaction, resources: TransactionResources?) {
        val strings = resources!!.strings
        val status = itemModel.status.toLowerCase()

        when {
            status.startsWith(FINISHED.value) -> fillOutStatusTv(strings[STRING_FINISHED], Styles.GREEN)
            status.startsWith(AWAITING_CONFIRMATIONS.value) -> fillOutStatusTv(strings[STRING_AWAITING_CONFIRMATIONS], Styles.BLUE)
            status.startsWith(EMAIL_SENT.value) -> fillOutStatusTv(strings[STRING_EMAIL_SENT], Styles.BLUE)
            status.startsWith(CANCELLED_BY_USER.value) -> fillOutStatusTv(strings[STRING_CANCELLED_BY_USER], Styles.RED)
            status.startsWith(AWAITING_APPROVAL.value) -> fillOutStatusTv(strings[STRING_AWAITING_APPROVAL], Styles.BLUE)
            status.startsWith(APPROVED.value) -> fillOutStatusTv(strings[STRING_APPROVED], Styles.BLUE)
            status.startsWith(NOT_CONFIRMED.value) -> fillOutStatusTv(strings[STRING_NOT_CONFIRMED], Styles.BLUE)
            status.startsWith(PROCESSING.value) -> fillOutStatusTv(strings[STRING_PROCESSING], Styles.BLUE)
            status.startsWith(WITHDRAWAL_ERROR.value) -> fillOutStatusTv(strings[STRING_WITHDRAWAL_ERROR], Styles.RED)
            status.startsWith(CANCELLED_BY_ADMIN.value) -> fillOutStatusTv(strings[STRING_CANCELLED_BY_ADMIN], Styles.RED)
        }

        mCurrencyTv.text = itemModel.currency

        mAmountDotv.setValueText(resources.doubleFormatter.formatAmount(itemModel.amount))

        fillOutFeeDotv(itemModel, resources)

        mDateDotv.setValueText(resources.timeFormatter.formatDate(itemModel.timestamp))
    }


    private fun fillOutStatusTv(text: String, style: Styles) {
        when(style) {

            Styles.GREEN -> {
                mStatusTv.background = itemView.context.getLayerDrawable(
                    R.drawable.secondary_button_background,
                    R.color.colorGreenAccent,
                    R.color.colorCardView
                )
                mStatusTv.setTextColor(itemView.context.getCompatColor(R.color.colorGreenAccent))
            }

            Styles.RED -> {
                mStatusTv.background = itemView.context.getLayerDrawable(
                    R.drawable.secondary_button_background,
                    R.color.colorRedAccent,
                    R.color.colorCardView
                )
                mStatusTv.setTextColor(itemView.context.getCompatColor(R.color.colorRedAccent))
            }

            Styles.BLUE -> {
                mStatusTv.background = itemView.context.getLayerDrawable(
                    R.drawable.secondary_button_background,
                    R.color.colorBlueAccent,
                    R.color.colorCardView
                )
                mStatusTv.setTextColor(itemView.context.getCompatColor(R.color.colorBlueAccent))
            }

        }

        mStatusTv.text = text
    }


    private fun fillOutFeeDotv(itemModel: Transaction, resources: TransactionResources?) {
        val lastDigitIndex = itemModel.fee.lastDigitIndex()

        val fee = itemModel.fee.substring(0, (lastDigitIndex + 1)).toDoubleOrNull()
        val feeCurrency = itemModel.fee.substring((lastDigitIndex + 1), itemModel.fee.length)

        if(fee != null) {
            mFeeDotv.setValueText("${resources!!.doubleFormatter.formatTransactionFee(fee)} $feeCurrency")
        }
    }


    fun setOnTransactionAddressClickListener(resources: TransactionResources?, position: Int,
                                             itemModel: Transaction, listener: ((View, Transaction, Int) -> Unit)?) {
        if(resources!!.transactionType == TransactionTypes.WITHDRAWALS) {
            val spannableString = SpannableString(itemModel.address.truncate(15))

            spannableString.setSpan(
                getClickableSpanForEntry(resources, itemModel, position, listener),
                0, spannableString.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            mAddressDotv.setValueMovementMethod(CustomLinkMovementMethod())
            mAddressDotv.setValueText(spannableString)
            mAddressDotv.makeVisible()
        } else {
            mAddressDotv.makeGone()
        }
    }


    fun setOnTransactionIdClickListener(resources: TransactionResources?, position: Int,
                                        itemModel: Transaction, listener: ((View, Transaction, Int) -> Unit)?) {
        if(itemModel.hasTransactionId()) {
            val spannableString = SpannableString(itemModel.transactionId!!.truncate(15))

            spannableString.setSpan(
                getClickableSpanForEntry(resources, itemModel, position, listener),
                0, spannableString.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            mTransactionIdDotv.setValueMovementMethod(CustomLinkMovementMethod())
            mTransactionIdDotv.setValueText(spannableString)
            mTransactionIdDotv.makeVisible()
        } else {
            mTransactionIdDotv.makeGone()
        }
    }


    private fun getClickableSpanForEntry(resources: TransactionResources?, itemModel: Transaction, position: Int,
                                         listener: ((View, Transaction, Int) -> Unit)?): SelectorSpan {
        return object : SelectorSpan(
            resources!!.colors[COLOR_LINK_NORMAL_BACKGROUND],
            resources.colors[COLOR_LINK_SELECTED_BACKGROUND]
        ) {

            override fun onClick(widget: View) {
                listener?.invoke(widget, itemModel, position)
            }

        }
    }


}