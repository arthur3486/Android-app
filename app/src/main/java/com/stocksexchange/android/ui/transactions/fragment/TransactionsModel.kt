package com.stocksexchange.android.ui.transactions.fragment

import com.stocksexchange.android.api.model.Currency
import com.stocksexchange.android.api.model.Transaction
import com.stocksexchange.android.database.mappings.mapToNameMarketMap
import com.stocksexchange.android.model.DataTypes
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.TransactionModes
import com.stocksexchange.android.model.TransactionParameters
import com.stocksexchange.android.repositories.currencies.CurrenciesRepository
import com.stocksexchange.android.repositories.transactions.TransactionsRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseDataLoadingModel
import com.stocksexchange.android.ui.transactions.fragment.TransactionsModel.ActionListener
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.koin.standalone.inject
import timber.log.Timber

class TransactionsModel : BaseDataLoadingModel<
    List<Transaction>,
    TransactionParameters,
    ActionListener
>() {


    private val mTransactionsRepository: TransactionsRepository by inject()
    private val mCurrenciesRepository: CurrenciesRepository by inject()




    override fun canLoadData(params: TransactionParameters, dataType: DataTypes): Boolean {
        val transactionMode = params.mode
        val searchQuery = params.searchQuery

        val isTransactionSearch = (transactionMode == TransactionModes.SEARCH)
        val isNewData = (dataType == DataTypes.NEW_DATA)

        val isTransactionSearchWithNoQuery = (isTransactionSearch && searchQuery.isBlank())
        val isTransactionSearchNewData = (isTransactionSearch && isNewData)
        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied())

        return (!isTransactionSearchWithNoQuery
                && !isTransactionSearchNewData
                && !isNewDataWithIntervalNotApplied)
    }


    override fun refreshData() {
        mTransactionsRepository.refresh()
    }


    override fun getDataAsync(params: TransactionParameters): Job {
        return launch(UI) {
            val result = getRepositoryResult(params)

            if(result.isSuccessful()) {
                val currenciesResult = mCurrenciesRepository.getAll()

                Timber.i("currenciesRepository.getAll() = $currenciesResult")

                if(currenciesResult.isSuccessful()) {
                    handleSuccessfulResponse(adjustTransactions(
                        result.getSuccessfulResult().value,
                        currenciesResult.getSuccessfulResult().value.mapToNameMarketMap()
                    ))
                } else {
                    handleUnsuccessfulResponse(currenciesResult.getErroneousResult().exception)
                }
            } else {
                handleUnsuccessfulResponse(result.getErroneousResult().exception)
            }
        }
    }


    override suspend fun getRepositoryResult(params: TransactionParameters): RepositoryResult<List<Transaction>> {
        val result = when(params.mode) {
            TransactionModes.STANDARD -> mTransactionsRepository.get(params)
            TransactionModes.SEARCH -> mTransactionsRepository.search(params)
        }

        Timber.i("transactionsRepository.get(params: $params) = $result")

        return result
    }


    private fun adjustTransactions(transactions: List<Transaction>,
                                   currencyMap: Map<String, Currency>): List<Transaction> {
        for(transaction in transactions) {
            transaction.blockExplorerUrl = currencyMap[transaction.currency]?.blockExplorerUrl ?: ""
        }

        return transactions
    }


    interface ActionListener: BaseDataLoadingActionListener<List<Transaction>>


}