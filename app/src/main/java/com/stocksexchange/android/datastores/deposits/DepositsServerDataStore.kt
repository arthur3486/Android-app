package com.stocksexchange.android.datastores.deposits

import com.stocksexchange.android.api.StocksExchangeService
import com.stocksexchange.android.api.exceptions.InvalidCredentialsException
import com.stocksexchange.android.api.model.ApiErrors
import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.api.model.PrivateApiMethods
import com.stocksexchange.android.api.utils.generateNonce
import com.stocksexchange.android.api.exceptions.ApiException
import com.stocksexchange.android.api.utils.extractDataDirectly
import com.stocksexchange.android.api.exceptions.CurrencyDisabledException
import com.stocksexchange.android.api.exceptions.InvalidCurrencyCodeException
import com.stocksexchange.android.api.exceptions.NoWalletAddressException
import com.stocksexchange.android.utils.handlers.CredentialsHandler
import com.stocksexchange.android.model.Result
import ru.gildor.coroutines.retrofit.awaitResult

class DepositsServerDataStore(
    private val stocksExchangeService: StocksExchangeService,
    private val credentialsHandler: CredentialsHandler
) : DepositsDataStore {


    override suspend fun save(deposit: Deposit) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun generateWalletAddress(currency: String): Result<Deposit> {
        return if(credentialsHandler.hasValidCredentials()) {
            stocksExchangeService.generateWalletAddress(
                PrivateApiMethods.GENERATE_WALLET.methodName,
                generateNonce(), currency
            ).awaitResult().extractDataDirectly {
                when(it.error) {
                    ApiErrors.CURRENCY_DISABLED.message -> CurrencyDisabledException()

                    else -> ApiException(it.toString())
                }
            }
        } else {
            Result.Failure(InvalidCredentialsException())
        }
    }


    override suspend fun get(currency: String): Result<Deposit> {
        return if(credentialsHandler.hasValidCredentials()) {
            stocksExchangeService.getDepositInfo(
                PrivateApiMethods.DEPOSIT.methodName,
                generateNonce(), currency
            ).awaitResult().extractDataDirectly {
                when(it.error) {
                    ApiErrors.NO_WALLET.message, ApiErrors.NO_WALLET_ADDRESS.message -> NoWalletAddressException()
                    ApiErrors.INVALID_CURRENCY_CODE.message -> InvalidCurrencyCodeException()

                    else -> ApiException(it.toString())
                }
            }
        } else {
            Result.Failure(InvalidCredentialsException())
        }
    }


}