package com.stocksexchange.android.ui.currencymarkets.fragment

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.model.CurrencyMarketTypes
import com.stocksexchange.android.model.CurrencyMarketsParameters
import com.stocksexchange.android.model.DataTypes
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseDataLoadingModel
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketsModel.ActionListener
import org.koin.standalone.inject
import timber.log.Timber

class CurrencyMarketsModel : BaseDataLoadingModel<
    List<CurrencyMarket>,
    CurrencyMarketsParameters,
    ActionListener
>() {


    private val mCurrencyMarketsRepository: CurrencyMarketsRepository by inject()




    override fun canLoadData(params: CurrencyMarketsParameters, dataType: DataTypes): Boolean {
        val marketType = params.currencyMarketType
        val searchQuery = params.searchQuery

        val isMarketSearch = (marketType == CurrencyMarketTypes.SEARCH)
        val isNewData = (dataType == DataTypes.NEW_DATA)

        val isMarketSearchWithNoQuery = (isMarketSearch && searchQuery.isBlank())
        val isMarketSearchNewData = (isMarketSearch && isNewData)
        val isFavoriteMarketsNewData = ((marketType == CurrencyMarketTypes.FAVORITES) && isNewData)
        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied())

        return (!isMarketSearchWithNoQuery
                && !isMarketSearchNewData
                && !isFavoriteMarketsNewData
                && !isNewDataWithIntervalNotApplied)
    }


    override fun refreshData() {
        mCurrencyMarketsRepository.refresh()
    }


    override suspend fun getRepositoryResult(params: CurrencyMarketsParameters): RepositoryResult<List<CurrencyMarket>> {
        val result = when(params.currencyMarketType) {
            CurrencyMarketTypes.BTC -> mCurrencyMarketsRepository.getBitcoinMarkets()
            CurrencyMarketTypes.USDT -> mCurrencyMarketsRepository.getTetherMarkets()
            CurrencyMarketTypes.NXT -> mCurrencyMarketsRepository.getNxtMarkets()
            CurrencyMarketTypes.LTC -> mCurrencyMarketsRepository.getLitecoinMarkets()
            CurrencyMarketTypes.ETH -> mCurrencyMarketsRepository.getEthereumMarkets()
            CurrencyMarketTypes.TUSD -> mCurrencyMarketsRepository.getTrueUsdMarkets()
            CurrencyMarketTypes.FAVORITES -> mCurrencyMarketsRepository.getFavoriteMarkets()
            CurrencyMarketTypes.SEARCH -> mCurrencyMarketsRepository.search(params.searchQuery)
        }

        Timber.i("currencyMarketsRepository.get(params: $params) = $result")

        return result
    }


    fun updateCurrencyMarket(currencyMarket: CurrencyMarket) {
        performAsync {
            mCurrencyMarketsRepository.save(currencyMarket)
        }
    }


    interface ActionListener : BaseDataLoadingActionListener<List<CurrencyMarket>>


}