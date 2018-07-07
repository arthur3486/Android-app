package com.stocksexchange.android.repositories.currencymarkets

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.repositories.Repository

interface CurrencyMarketsRepository : Repository {

    suspend fun save(currencyMarket: CurrencyMarket)

    suspend fun save(currencyMarkets: List<CurrencyMarket>)

    suspend fun favorite(currencyMarket: CurrencyMarket)

    suspend fun unfavorite(currencyMarket: CurrencyMarket)

    suspend fun deleteAll()

    suspend fun search(query: String): RepositoryResult<List<CurrencyMarket>>

    suspend fun isCurrencyMarketFavorite(currencyMarket: CurrencyMarket): Boolean

    suspend fun get(ids: List<Long>): RepositoryResult<List<CurrencyMarket>>

    suspend fun getBitcoinMarkets(): RepositoryResult<List<CurrencyMarket>>

    suspend fun getTetherMarkets(): RepositoryResult<List<CurrencyMarket>>

    suspend fun getNxtMarkets(): RepositoryResult<List<CurrencyMarket>>

    suspend fun getLitecoinMarkets(): RepositoryResult<List<CurrencyMarket>>

    suspend fun getEthereumMarkets(): RepositoryResult<List<CurrencyMarket>>

    suspend fun getTrueUsdMarkets(): RepositoryResult<List<CurrencyMarket>>

    suspend fun getCurrencyMarkets(marketName: String): RepositoryResult<List<CurrencyMarket>>

    suspend fun getFavoriteMarkets(): RepositoryResult<List<CurrencyMarket>>

    suspend fun getAll(): RepositoryResult<List<CurrencyMarket>>

}