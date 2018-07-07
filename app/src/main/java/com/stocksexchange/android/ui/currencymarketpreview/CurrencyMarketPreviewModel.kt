package com.stocksexchange.android.ui.currencymarketpreview

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.repositories.users.UsersRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import org.koin.standalone.inject

class CurrencyMarketPreviewModel : BaseModel() {


    private var mActionListener: ActionListener? = null

    private val mCurrencyMarketsRepository: CurrencyMarketsRepository by inject()
    private val mUsersRepository: UsersRepository by inject()




    fun handleFavoriteAction(currencyMarket: CurrencyMarket) {
        performAsync {
            if(mCurrencyMarketsRepository.isCurrencyMarketFavorite(currencyMarket)) {
                unfavoriteCurrencyMarket(currencyMarket)
            } else {
                favoriteCurrencyMarket(currencyMarket)
            }
        }
    }


    suspend fun unfavoriteCurrencyMarket(currencyMarket: CurrencyMarket) {
        mCurrencyMarketsRepository.unfavorite(currencyMarket)

        mActionListener?.onCurrencyMarketUnfavorited(currencyMarket)
    }


    suspend fun favoriteCurrencyMarket(currencyMarket: CurrencyMarket) {
        mCurrencyMarketsRepository.favorite(currencyMarket)

        mActionListener?.onCurrencyMarketFavorited(currencyMarket)
    }


    fun fetchUserAsync(onPresent: (User) -> Unit, onAbsent: () -> Unit) {
        performAsync {
            val result = mUsersRepository.getSignedInUser()

            if(result.isSuccessful()) {
                onPresent.invoke(result.getSuccessfulResult().value)
            } else {
                onAbsent.invoke()
            }
        }
    }


    fun setActionListener(listener: ActionListener) {
        mActionListener = listener
    }


    interface ActionListener {

        fun onCurrencyMarketFavorited(currencyMarket: CurrencyMarket)

        fun onCurrencyMarketUnfavorited(currencyMarket: CurrencyMarket)

    }


}