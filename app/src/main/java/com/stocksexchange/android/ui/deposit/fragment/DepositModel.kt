package com.stocksexchange.android.ui.deposit.fragment

import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.api.exceptions.NoWalletAddressException
import com.stocksexchange.android.model.DepositParameters
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.repositories.deposits.DepositsRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseDataLoadingModel
import com.stocksexchange.android.ui.deposit.fragment.DepositModel.ActionListener
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.koin.standalone.inject
import timber.log.Timber

class DepositModel : BaseDataLoadingModel<
    Deposit,
    DepositParameters,
    ActionListener
>() {


    private val mDepositsRepository: DepositsRepository by inject()




    override fun refreshData() {
        mDepositsRepository.refresh()
    }


    override fun getDataAsync(params: DepositParameters): Job {
        val currency = params.currency

        return launch(UI) {
            val result = getRepositoryResult(params)

            if(result.isSuccessful()) {
                handleSuccessfulResponse(result.getSuccessfulResult().value)
            } else {
                when(result.getErroneousResult().exception) {

                    is NoWalletAddressException -> {
                        val walletGenerationResult = mDepositsRepository.generateWalletAddress(currency)

                        Timber.i("depositsRepository.generateWalletAddress(currency: $currency) = $walletGenerationResult")

                        if(walletGenerationResult.isSuccessful()) {
                            handleSuccessfulResponse(walletGenerationResult.getSuccessfulResult().value)
                        } else {
                            handleUnsuccessfulResponse(walletGenerationResult.getErroneousResult().exception)
                        }
                    }

                    else -> handleUnsuccessfulResponse(result.getErroneousResult().exception)
                }
            }
        }
    }


    override suspend fun getRepositoryResult(params: DepositParameters): RepositoryResult<Deposit> {
        val result = mDepositsRepository.get(params.currency)

        Timber.i("depositsRepository.get(currency: ${params.currency}) = $result")

        return result
    }


    interface ActionListener : BaseDataLoadingActionListener<Deposit>


}