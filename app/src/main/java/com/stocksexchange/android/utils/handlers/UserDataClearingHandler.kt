package com.stocksexchange.android.utils.handlers

import com.stocksexchange.android.repositories.deposits.DepositsRepository
import com.stocksexchange.android.repositories.orders.OrdersRepository
import com.stocksexchange.android.repositories.transactions.TransactionsRepository
import com.stocksexchange.android.repositories.users.UsersRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * A helper class used for providing clearing user's data functionality.
 */
class UserDataClearingHandler(
    private val usersRepository: UsersRepository,
    private val depositsRepository: DepositsRepository,
    private val ordersRepository: OrdersRepository,
    private val transactionsRepository: TransactionsRepository,
    private val credentialsHandler: CredentialsHandler
) {


    /**
     * Clears all user related data stored inside the database
     * as well as in preferences.
     *
     * @param onFinish The callback to invoke when the clearing
     * is done
     */
    fun clearUserData(onFinish: (() -> Unit)? = null) {
        launch(UI) {
            usersRepository.deleteSignedInUser()
            depositsRepository.deleteAll()
            ordersRepository.deleteUserOrders()
            transactionsRepository.deleteAll()
            credentialsHandler.clearKeys()

            onFinish?.invoke()
        }
    }


}