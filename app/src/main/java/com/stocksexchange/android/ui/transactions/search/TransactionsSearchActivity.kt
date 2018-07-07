package com.stocksexchange.android.ui.transactions.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import com.stocksexchange.android.R
import com.stocksexchange.android.model.TransactionTypes
import com.stocksexchange.android.ui.base.activities.BaseSearchActivity
import com.stocksexchange.android.ui.transactions.fragment.TransactionsFragment
import org.jetbrains.anko.intentFor

class TransactionsSearchActivity : BaseSearchActivity<TransactionsFragment, TransactionsSearchPresenter>(),
    TransactionsSearchContract.View {


    companion object {

        private const val EXTRA_TRANSACTION_TYPE = "transaction_type"

        private const val SAVED_STATE_TRANSACTION_TYPE = "transaction_type"


        fun newInstance(context: Context, transactionType: TransactionTypes): Intent {
            return context.intentFor<TransactionsSearchActivity>(
                EXTRA_TRANSACTION_TYPE to transactionType
            )
        }

    }


    /**
     * A transaction type to instantiate [TransactionsFragment] with.
     */
    private lateinit var mTransactionType: TransactionTypes




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.kitkat_scaling_window_b_enter_animation,
            R.anim.kitkat_scaling_window_a_exit_animation
        )
    }


    override fun initPresenter(): TransactionsSearchPresenter = TransactionsSearchPresenter(this)


    override fun performSearch(query: String) {
        mFragment.onPerformSearch(query)
    }


    override fun cancelSearch() {
        mFragment.onCancelSearch()
    }


    override fun getInputHint(): String {
        return getString(when(mTransactionType) {
            TransactionTypes.DEPOSITS -> R.string.search_deposits
            TransactionTypes.WITHDRAWALS -> R.string.search_withdrawals
        })
    }


    override fun getInputType(): Int {
        return (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS)
    }


    override fun getActivityFragment(): TransactionsFragment {
        return TransactionsFragment.newSearchInstance(mTransactionType)
    }


    override fun getContentLayoutResourceId(): Int = R.layout.transactions_search_activity


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.kitkat_scaling_window_a_enter_animation,
            R.anim.kitkat_scaling_window_b_exit_animation
        )
    }


    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        if(savedState != null) {
            mTransactionType = (savedState.getSerializable(SAVED_STATE_TRANSACTION_TYPE) as TransactionTypes)
        } else {
            mTransactionType = (intent.getSerializableExtra(EXTRA_TRANSACTION_TYPE) as? TransactionTypes) ?: TransactionTypes.DEPOSITS
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putSerializable(SAVED_STATE_TRANSACTION_TYPE, mTransactionType)
    }


}