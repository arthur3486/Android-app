package com.stocksexchange.android.ui.transactions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.stocksexchange.android.R
import com.stocksexchange.android.model.TransactionTypes
import com.stocksexchange.android.ui.base.activities.BaseFragmentActivity
import com.stocksexchange.android.ui.transactions.fragment.TransactionsFragment
import com.stocksexchange.android.ui.transactions.search.TransactionsSearchActivity
import com.stocksexchange.android.ui.utils.extensions.makeVisible
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.jetbrains.anko.intentFor

class TransactionsActivity : BaseFragmentActivity<TransactionsFragment, TransactionsActivityPresenter>(),
    TransactionsActivityContract.View {


    companion object {

        private const val EXTRA_TRANSACTION_TYPE = "transaction_type"

        private const val SAVED_STATE_TRANSACTION_TYPE = "transaction_type"


        fun newDepositsInstance(context: Context) = newInstance(context, TransactionTypes.DEPOSITS)

        fun newWithdrawalsInstance(context: Context) = newInstance(context, TransactionTypes.WITHDRAWALS)

        fun newInstance(context: Context, transactionType: TransactionTypes): Intent {
            return context.intentFor<TransactionsActivity>(
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
            R.anim.horizontal_sliding_right_to_left_enter_animation,
            R.anim.default_window_exit_animation
        )
    }


    override fun initPresenter(): TransactionsActivityPresenter = TransactionsActivityPresenter(this)


    override fun init() {
        super.init()

        initToolbar()
    }


    private fun initToolbar() {
        toolbar.returnBackBtnIv.setOnClickListener { onBackPressed() }
        toolbar.titleTv.text = getString(when(mTransactionType) {
            TransactionTypes.DEPOSITS -> R.string.deposits
            TransactionTypes.WITHDRAWALS -> R.string.withdrawals
        })

        toolbar.actionBtnIv.setImageResource(R.drawable.ic_search)
        toolbar.actionBtnIv.setOnClickListener { mPresenter?.onActionButtonClicked() }
        toolbar.actionBtnIv.makeVisible()
    }


    override fun launchSearchActivity() {
        startActivity(TransactionsSearchActivity.newInstance(this, mTransactionType))
    }


    override fun getActivityFragment(): TransactionsFragment {
        return TransactionsFragment.newStandardInstance(mTransactionType)
    }


    override fun getContentLayoutResourceId(): Int = R.layout.transactions_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.default_window_enter_animation,
            R.anim.horizontal_sliding_right_to_left_exit_animation
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