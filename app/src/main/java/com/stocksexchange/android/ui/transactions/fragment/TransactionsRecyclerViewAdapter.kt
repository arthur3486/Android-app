package com.stocksexchange.android.ui.transactions.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.model.TransactionTypes
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseRecyclerViewAdapter

class TransactionsRecyclerViewAdapter(
    context: Context,
    items: MutableList<Transaction>
) : BaseRecyclerViewAdapter<Transaction, TransactionViewHolder, TransactionResources>(context, items) {


    /**
     * Resources for the adapter.
     */
    private val mResources: TransactionResources = TransactionResources.newInstance(context)

    /**
     * A listener used for notifying whenever a transaction address has been clicked.
     */
    var mOnTransactionAddressClickListener: ((View, Transaction, Int) -> Unit)? = null

    /**
     * A listener used for notifying whenever a transaction ID has been clicked.
     */
    var mOnTransactionIdClickListener: ((View, Transaction, Int) -> Unit)? = null




    override fun onCreateViewHolder(parent: ViewGroup, inflater: LayoutInflater,
                                    resources: TransactionResources?, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            inflater.inflate(TransactionViewHolder.MAIN_LAYOUT_ID, parent, false),
            resources
        )
    }


    override fun onBindViewHolder(viewHolder: TransactionViewHolder, itemModel: Transaction,
                                  resources: TransactionResources?, viewType: Int) {
        viewHolder.bind(itemModel, resources)
    }


    override fun assignListeners(viewHolder: TransactionViewHolder, itemModel: Transaction,
                                 position: Int, viewType: Int) {
        viewHolder.setOnTransactionAddressClickListener(mResources, position, itemModel, mOnTransactionAddressClickListener)
        viewHolder.setOnTransactionIdClickListener(mResources, position, itemModel, mOnTransactionIdClickListener)
    }


    fun setTransactionType(type: TransactionTypes) {
        mResources.transactionType = type
    }


    override fun getResources(): TransactionResources? {
        return mResources
    }


}