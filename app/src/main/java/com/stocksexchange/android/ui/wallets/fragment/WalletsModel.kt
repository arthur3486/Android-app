package com.stocksexchange.android.ui.wallets.fragment

import com.stocksexchange.android.api.model.Currency
import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.model.*
import com.stocksexchange.android.repositories.currencies.CurrenciesRepository
import com.stocksexchange.android.repositories.users.UsersRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseDataLoadingModel
import com.stocksexchange.android.ui.utils.extensions.getWithDefault
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.koin.standalone.inject
import timber.log.Timber

class WalletsModel : BaseDataLoadingModel<
    List<Wallet>,
    WalletParameters,
    WalletsModel.ActionListener
>() {


    private val mCurrenciesRepository: CurrenciesRepository by inject()
    private val mUsersRepository: UsersRepository by inject()




    override fun canLoadData(params: WalletParameters, dataType: DataTypes): Boolean {
        val walletMode = params.walletMode
        val searchQuery = params.searchQuery

        val isWalletSearch = (walletMode == WalletModes.SEARCH)
        val isNewData = (dataType == DataTypes.NEW_DATA)

        val isWalletSearchWithNoQuery = (isWalletSearch && searchQuery.isBlank())
        val isWalletSearchNewData = (isWalletSearch && isNewData)
        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied())

        return (!isWalletSearchWithNoQuery
                && !isWalletSearchNewData
                && !isNewDataWithIntervalNotApplied)
    }


    override fun refreshData() {
        mUsersRepository.refresh()
        mCurrenciesRepository.refresh()
    }


    override fun getDataAsync(params: WalletParameters): Job {
        return launch(UI) {
            val currenciesResult = when(params.walletMode) {
                WalletModes.STANDARD -> mCurrenciesRepository.getAll()
                WalletModes.SEARCH -> mCurrenciesRepository.search(params.searchQuery)
            }

            Timber.i("currenciesRepository.get(params: $params) = $currenciesResult")

            if(currenciesResult.isSuccessful()) {
                val userResult = mUsersRepository.getSignedInUser()

                Timber.i("usersRepository.getSignedInUser() = $userResult")

                if(userResult.isSuccessful()) {
                    val wallets = generateWallets(
                        currenciesResult.getSuccessfulResult().value,
                        userResult.getSuccessfulResult().value,
                        params.shouldShowEmptyWallets
                    )

                    handleSuccessfulResponse(wallets)
                } else {
                    handleUnsuccessfulResponse(userResult.getErroneousResult().exception)
                }
            } else {
                handleUnsuccessfulResponse(currenciesResult.getErroneousResult().exception)
            }
        }
    }


    override suspend fun getRepositoryResult(params: WalletParameters): RepositoryResult<List<Wallet>> {
        return RepositoryResult(null, null, null)
    }


    private fun generateWallets(currencies: List<Currency>, user: User,
                                shouldShowEmptyWallets: Boolean): List<Wallet> {
        val wallets = mutableListOf<Wallet>()
        var availableBalance: Double
        var balanceInOrders: Double

        // Filling out wallets with information
        for(currency in currencies) {
            availableBalance = user.funds.getWithDefault(currency.name, "0").toDouble()
            balanceInOrders = user.holdFunds.getWithDefault(currency.name, "0").toDouble()

            if(!shouldShowEmptyWallets && (availableBalance == 0.0) && (balanceInOrders == 0.0)) {
                continue
            }

            wallets.add(Wallet(currency, availableBalance, balanceInOrders))
        }

        // Sorting wallets
        return wallets.sortedWith(
            compareByDescending<Wallet> { it.availableBalance }
            .thenByDescending { it.balanceInOrders }
        )
    }


    interface ActionListener : BaseDataLoadingActionListener<List<Wallet>>


}