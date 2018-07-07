package com.stocksexchange.android.repositories.currencymarkets

import com.stocksexchange.android.api.model.CurrencyMarket
import com.stocksexchange.android.datastores.currencymarkets.CurrencyMarketsDataStore
import com.stocksexchange.android.datastores.currencymarkets.CurrencyMarketsDatabaseDataStore
import com.stocksexchange.android.datastores.favoritecurrencymarkets.FavoriteCurrencyMarketsDataStore
import com.stocksexchange.android.datastores.exceptions.FavoriteMarketsNotFoundException
import com.stocksexchange.android.model.CurrencyMarketTypes
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.Result
import com.stocksexchange.android.utils.providers.ConnectionProvider
import com.stocksexchange.android.repositories.BaseRepository
import com.stocksexchange.android.utils.helpers.handleRepositoryResult

class CurrencyMarketsRepositoryImpl(
    private val serverDataStore: CurrencyMarketsDataStore,
    private val databaseDataStore: CurrencyMarketsDatabaseDataStore,
    private val favoritesDatabaseDataStore: FavoriteCurrencyMarketsDataStore,
    private val connectionProvider: ConnectionProvider
) : BaseRepository<CurrencyMarketsCache>(), CurrencyMarketsRepository {


    override val cache: CurrencyMarketsCache = CurrencyMarketsCache()




    override suspend fun save(currencyMarket: CurrencyMarket) {
        databaseDataStore.save(currencyMarket)
        saveMarketToCache(currencyMarket)
    }


    override suspend fun save(currencyMarkets: List<CurrencyMarket>) {
        databaseDataStore.save(currencyMarkets)
        saveMarketsToCache(currencyMarkets)
    }


    override suspend fun favorite(currencyMarket: CurrencyMarket) {
        favoritesDatabaseDataStore.favorite(currencyMarket)

        val favoriteMarkets = if(cache.hasFavoriteCurrencyMarkets()) {
            cache.getFavoriteCurrencyMarkets().toMutableList().apply {
                add(currencyMarket)
            }
        } else {
            listOf(currencyMarket)
        }

        saveFavoritesToCache(favoriteMarkets)
    }


    override suspend fun unfavorite(currencyMarket: CurrencyMarket) {
        favoritesDatabaseDataStore.unfavorite(currencyMarket)

        if(cache.hasFavoriteCurrencyMarkets()) {
            val favoriteMarkets = cache.getFavoriteCurrencyMarkets().toMutableList()
            favoriteMarkets.remove(currencyMarket)
            saveFavoritesToCache(favoriteMarkets)
        }
    }


    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
    }


    override suspend fun search(query: String): RepositoryResult<List<CurrencyMarket>> {
        val result = getAll()

        // Making sure that the data is present since the search is performed
        // solely on database records
        return if(result.isSuccessful()) {
            RepositoryResult(databaseResult = databaseDataStore.search(query))
        } else {
            result
        }
    }


    override suspend fun isCurrencyMarketFavorite(currencyMarket: CurrencyMarket): Boolean {
        val favoriteMarkets: List<CurrencyMarket> = if(cache.hasFavoriteCurrencyMarkets()) {
            cache.getFavoriteCurrencyMarkets()
        } else {
            val result = getFavoriteMarkets()

            if(result.isSuccessful()) {
                result.getSuccessfulResult().value
            } else {
                return true
            }
        }

        favoriteMarkets.forEach {
            if(currencyMarket.id == it.id) {
                return@isCurrencyMarketFavorite true
            }
        }

        return false
    }


    override suspend fun get(ids: List<Long>): RepositoryResult<List<CurrencyMarket>> {
        return RepositoryResult(databaseResult = databaseDataStore.get(ids))
    }


    override suspend fun getBitcoinMarkets(): RepositoryResult<List<CurrencyMarket>> {
        return getCurrencyMarkets(CurrencyMarketTypes.BTC.name)
    }


    override suspend fun getTetherMarkets(): RepositoryResult<List<CurrencyMarket>> {
        return getCurrencyMarkets(CurrencyMarketTypes.USDT.name)
    }


    override suspend fun getNxtMarkets(): RepositoryResult<List<CurrencyMarket>> {
        return getCurrencyMarkets(CurrencyMarketTypes.NXT.name)
    }


    override suspend fun getLitecoinMarkets(): RepositoryResult<List<CurrencyMarket>> {
        return getCurrencyMarkets(CurrencyMarketTypes.LTC.name)
    }


    override suspend fun getEthereumMarkets(): RepositoryResult<List<CurrencyMarket>> {
        return getCurrencyMarkets(CurrencyMarketTypes.ETH.name)
    }


    override suspend fun getTrueUsdMarkets(): RepositoryResult<List<CurrencyMarket>> {
        return getCurrencyMarkets(CurrencyMarketTypes.TUSD.name)
    }


    override suspend fun getCurrencyMarkets(marketName: String): RepositoryResult<List<CurrencyMarket>> {
        val result = getAll()

        return if(result.isSuccessful()) {
            val marketsResult = Result.Success(result.getSuccessfulResult().value.filter { it.market == marketName })

            when {
                result.isCacheResultSuccessful() -> result.cacheResult = marketsResult
                result.isServerResultSuccessful() -> result.serverResult = marketsResult
                else -> result.databaseResult = marketsResult
            }

            result
        } else {
            result
        }
    }


    override suspend fun getFavoriteMarkets(): RepositoryResult<List<CurrencyMarket>> {
        if(cache.hasFavoriteCurrencyMarkets()) {
            return RepositoryResult(cacheResult = Result.Success(cache.getFavoriteCurrencyMarkets()))
        }

        val currencyMarketsResult = getAll()

        return if(currencyMarketsResult.isSuccessful()) {
            val favoriteMarketNamesResult = favoritesDatabaseDataStore.getAll()

            when(favoriteMarketNamesResult) {
                is Result.Success -> {
                    val favoriteMarketsResult = get(favoriteMarketNamesResult.value)

                    if(favoriteMarketsResult.isSuccessful()) {
                        saveFavoritesToCache(favoriteMarketsResult.getSuccessfulResult().value)
                    }

                    favoriteMarketsResult
                }
                is Result.Failure -> {
                    RepositoryResult<List<CurrencyMarket>>(databaseResult = Result.Failure(FavoriteMarketsNotFoundException()))
                }
            }
        } else {
            currencyMarketsResult
        }
    }


    override suspend fun getAll(): RepositoryResult<List<CurrencyMarket>> {
        val result = RepositoryResult<List<CurrencyMarket>>()

        if(cache.isEmpty() || cache.isInvalidated) {
            var onSuccess: suspend ((Result.Success<List<CurrencyMarket>>) -> Unit) = {}

            if(connectionProvider.isNetworkAvailable()) {
                result.serverResult = serverDataStore.getAll()
                onSuccess = {
                    // Getting rid of old ones since quite a few could be no
                    // longer active and it would be a mistake to show them
                    // to the user in the future database loads (e.g., when
                    // the network is not available)
                    deleteAll()
                    save(it.value)
                }
            }

            if(result.isServerResultErroneous(true)) {
                result.databaseResult = databaseDataStore.getAll()
                onSuccess = { saveMarketsToCache(it.value) }
            }

            if(!handleRepositoryResult(result, onSuccess)) {
                return result
            }

            if(cache.isInvalidated) {
                cache.validate()
            }
        } else {
            result.cacheResult = Result.Success(cache.getCurrencyMarkets())
        }

        return result
    }


    private fun saveMarketToCache(currencyMarket: CurrencyMarket) {
        val currencyMarkets = if(cache.hasCurrencyMarkets()) {
            cache.getCurrencyMarkets().toMutableList().apply {
                add(currencyMarket)
            }
        } else {
            listOf(currencyMarket)
        }

        cache.saveCurrencyMarkets(currencyMarkets)
    }


    private fun saveMarketsToCache(currencyMarkets: List<CurrencyMarket>) {
        cache.saveCurrencyMarkets(currencyMarkets)
    }


    private fun saveFavoritesToCache(markets: List<CurrencyMarket>) {
        cache.saveFavoriteCurrencyMarkets(markets)
    }



}