package com.stocksexchange.android.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.stocksexchange.android.database.StocksExchangeDatabase.Companion.VERSION
import com.stocksexchange.android.database.converters.*
import com.stocksexchange.android.database.daos.*
import com.stocksexchange.android.database.model.*

/**
 * Main Room database this application uses to store data.
 */
@Database(entities = [
    DatabaseUser::class,
    DatabaseCurrencyMarket::class,
    DatabaseFavoriteCurrencyMarket::class,
    DatabaseCurrencyMarketSummary::class,
    DatabaseChartData::class,
    DatabaseOrder::class,
    DatabaseCurrency::class,
    DatabaseTransaction::class,
    DatabaseDeposit::class,
    DatabaseTicket::class,
    DatabaseSettings::class
], version = VERSION)
@TypeConverters(
    AssetsPortfolioConverter::class,
    UserSessionConverter::class,
    StringConverter::class,
    CandleStickConverter::class,
    TicketMessageConverter::class,
    TicketMessageDateConverter::class,
    DecimalSeparatorConverter::class,
    OrderAmountConverter::class
)
abstract class StocksExchangeDatabase : RoomDatabase() {


    companion object {

        const val NAME = "stocksexchange_database.db"
        const val VERSION = 6

    }


    abstract val userDao: UserDao
    abstract val currencyMarketDao: CurrencyMarketDao
    abstract val favoriteCurrencyMarketDao: FavoriteCurrencyMarketDao
    abstract val currencyMarketSummaryDao: CurrencyMarketSummaryDao
    abstract val chartDataDao: ChartDataDao
    abstract val orderDao: OrderDao
    abstract val currencyDao: CurrencyDao
    abstract val transactionDao: TransactionDao
    abstract val depositDao: DepositDao
    abstract val ticketDao: TicketDao
    abstract val settingsDao: SettingsDao


}

