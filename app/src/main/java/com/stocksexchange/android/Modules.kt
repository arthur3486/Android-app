package com.stocksexchange.android

import android.arch.persistence.room.Room
import com.google.gson.GsonBuilder
import com.stocksexchange.android.api.StocksExchangeService
import com.stocksexchange.android.api.interceptors.AuthInterceptor
import com.stocksexchange.android.database.StocksExchangeDatabase
import com.stocksexchange.android.database.migrations.*
import com.stocksexchange.android.datastores.chartsdata.ChartsDataDataStore
import com.stocksexchange.android.datastores.chartsdata.ChartsDataDatabaseDataStore
import com.stocksexchange.android.datastores.chartsdata.ChartsDataServerDataStore
import com.stocksexchange.android.datastores.currencies.CurrenciesDataStore
import com.stocksexchange.android.datastores.currencies.CurrenciesDatabaseDataStore
import com.stocksexchange.android.datastores.currencies.CurrenciesServerDataStore
import com.stocksexchange.android.datastores.currencymarkets.CurrencyMarketsDataStore
import com.stocksexchange.android.datastores.currencymarkets.CurrencyMarketsDatabaseDataStore
import com.stocksexchange.android.datastores.currencymarkets.CurrencyMarketsServerDataStore
import com.stocksexchange.android.datastores.currencymarketsummaries.CurrencyMarketSummariesDataStore
import com.stocksexchange.android.datastores.currencymarketsummaries.CurrencyMarketSummariesDatabaseDataStore
import com.stocksexchange.android.datastores.currencymarketsummaries.CurrencyMarketSummariesServerDataStore
import com.stocksexchange.android.datastores.deposits.DepositsDataStore
import com.stocksexchange.android.datastores.deposits.DepositsDatabaseDataStore
import com.stocksexchange.android.datastores.deposits.DepositsServerDataStore
import com.stocksexchange.android.datastores.favoritecurrencymarkets.FavoriteCurrencyMarketsDataStore
import com.stocksexchange.android.datastores.favoritecurrencymarkets.FavoriteCurrencyMarketsDatabaseDataStore
import com.stocksexchange.android.datastores.orders.OrdersDataStore
import com.stocksexchange.android.datastores.orders.OrdersDatabaseDataStore
import com.stocksexchange.android.datastores.orders.OrdersServerDataStore
import com.stocksexchange.android.datastores.settings.SettingsDataStore
import com.stocksexchange.android.datastores.settings.SettingsDatabaseDataStore
import com.stocksexchange.android.datastores.tickets.TicketsDataStore
import com.stocksexchange.android.datastores.tickets.TicketsDatabaseDataStore
import com.stocksexchange.android.datastores.tickets.TicketsServerDataStore
import com.stocksexchange.android.datastores.transactions.TransactionsDataStore
import com.stocksexchange.android.datastores.transactions.TransactionsDatabaseDataStore
import com.stocksexchange.android.datastores.transactions.TransactionsServerDataStore
import com.stocksexchange.android.datastores.users.UsersDataStore
import com.stocksexchange.android.datastores.users.UsersDatabaseDataStore
import com.stocksexchange.android.datastores.users.UsersServerDataStore
import com.stocksexchange.android.utils.providers.*
import com.stocksexchange.android.repositories.chartsdata.ChartsDataRepository
import com.stocksexchange.android.repositories.chartsdata.ChartsDataRepositoryImpl
import com.stocksexchange.android.repositories.currencies.CurrenciesRepository
import com.stocksexchange.android.repositories.currencies.CurrenciesRepositoryImpl
import com.stocksexchange.android.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.repositories.currencymarkets.CurrencyMarketsRepositoryImpl
import com.stocksexchange.android.repositories.currencymarketsummaries.CurrencyMarketSummariesRepository
import com.stocksexchange.android.repositories.currencymarketsummaries.CurrencyMarketSummariesRepositoryImpl
import com.stocksexchange.android.repositories.deposits.DepositsRepository
import com.stocksexchange.android.repositories.deposits.DepositsRepositoryImpl
import com.stocksexchange.android.repositories.orders.OrdersRepository
import com.stocksexchange.android.repositories.orders.OrdersRepositoryImpl
import com.stocksexchange.android.repositories.settings.SettingsRepository
import com.stocksexchange.android.repositories.settings.SettingsRepositoryImpl
import com.stocksexchange.android.repositories.tickets.TicketsRepository
import com.stocksexchange.android.repositories.tickets.TicketsRepositoryImpl
import com.stocksexchange.android.repositories.transactions.TransactionsRepository
import com.stocksexchange.android.repositories.transactions.TransactionsRepositoryImpl
import com.stocksexchange.android.repositories.users.UsersRepository
import com.stocksexchange.android.repositories.users.UsersRepositoryImpl
import com.stocksexchange.android.utils.handlers.*
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val GSON = "GSON"

private val providersModule = applicationContext {

    bean { StringProvider(get())}
    bean { ConnectionProvider(get()) }
    bean { CustomTabsProvider(get()) }
    bean { EmailProvider(get()) }
    bean { InfoViewIconProvider(get()) }
    bean { RingtoneProvider(get()) }
    bean { BooleanProvider(get()) }

}

private val handlersModule = applicationContext {

    bean { BrowserHandler(get()) }
    bean { ClipboardHandler(get()) }
    bean { CredentialsHandler(get()) }
    bean { PreferenceHandler(get()) }
    bean { QrCodeHandler() }
    bean { UserDataClearingHandler(get(), get(), get(), get(), get()) }

}

private val databaseModule = applicationContext {

    bean {
        Room.databaseBuilder(
            get(),
            StocksExchangeDatabase::class.java,
            StocksExchangeDatabase.NAME
        )
        .addMigrations(
            Migration_1_2,
            Migration_2_3,
            Migration_3_4,
            Migration_4_5,
            Migration_5_6
        )
        .build()
    }

    bean { get<StocksExchangeDatabase>().userDao }
    bean { get<StocksExchangeDatabase>().currencyMarketDao }
    bean { get<StocksExchangeDatabase>().favoriteCurrencyMarketDao }
    bean { get<StocksExchangeDatabase>().currencyMarketSummaryDao }
    bean { get<StocksExchangeDatabase>().chartDataDao }
    bean { get<StocksExchangeDatabase>().orderDao }
    bean { get<StocksExchangeDatabase>().currencyDao }
    bean { get<StocksExchangeDatabase>().transactionDao }
    bean { get<StocksExchangeDatabase>().depositDao }
    bean { get<StocksExchangeDatabase>().ticketDao }
    bean { get<StocksExchangeDatabase>().settingsDao }

}

private val apiModule = applicationContext {

    bean { get<Retrofit>().create(StocksExchangeService::class.java) }

    bean {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get(GSON)))
            .client(get())
            .build()
    }

    bean(GSON) { GsonBuilder().create() }

    bean { AuthInterceptor(get()) }

    bean {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    bean {
        OkHttpClient.Builder().apply {
            addInterceptor(get<AuthInterceptor>())
            connectTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            certificatePinner(CertificatePinner.Builder()
                .add(BuildConfig.STOCKS_EXCHANGE_HOSTNAME, BuildConfig.CERTIFICATE_PUBLIC_KEY_HASH_FIRST)
                .add(BuildConfig.STOCKS_EXCHANGE_HOSTNAME, BuildConfig.CERTIFICATE_PUBLIC_KEY_HASH_SECOND)
                .add(BuildConfig.STOCKS_EXCHANGE_HOSTNAME, BuildConfig.CERTIFICATE_PUBLIC_KEY_HASH_THIRD)
                .build()
            )

            if(BuildConfig.DEBUG) {
                addInterceptor(get<HttpLoggingInterceptor>())
            }
        }.build()
    }

}

private val dataStoresModule = applicationContext {

    bean { UsersDatabaseDataStore(get(), get()) }
    bean { UsersServerDataStore(get(), get()) as UsersDataStore }

    bean { CurrencyMarketsDatabaseDataStore(get()) }
    bean { CurrencyMarketsServerDataStore(get()) as CurrencyMarketsDataStore }

    bean { FavoriteCurrencyMarketsDatabaseDataStore(get()) as FavoriteCurrencyMarketsDataStore }

    bean { CurrencyMarketSummariesDatabaseDataStore(get()) }
    bean { CurrencyMarketSummariesServerDataStore(get()) as CurrencyMarketSummariesDataStore }

    bean { ChartsDataDatabaseDataStore(get()) }
    bean { ChartsDataServerDataStore(get()) as ChartsDataDataStore }

    bean { OrdersDatabaseDataStore(get()) }
    bean { OrdersServerDataStore(get(), get()) as OrdersDataStore }

    bean { CurrenciesDatabaseDataStore(get()) }
    bean { CurrenciesServerDataStore(get()) as CurrenciesDataStore }

    bean { TransactionsDatabaseDataStore(get()) }
    bean { TransactionsServerDataStore(get(), get()) as TransactionsDataStore }

    bean { DepositsDatabaseDataStore(get()) }
    bean { DepositsServerDataStore(get(), get()) as DepositsDataStore }

    bean { TicketsDatabaseDataStore(get()) }
    bean { TicketsServerDataStore(get(), get()) as TicketsDataStore }

    bean { SettingsDatabaseDataStore(get()) as SettingsDataStore }

}

private val repositoriesModule = applicationContext {

    bean { UsersRepositoryImpl(get(), get(), get(), get()) as UsersRepository }
    bean { CurrencyMarketsRepositoryImpl(get(), get(), get(), get()) as CurrencyMarketsRepository }
    bean { CurrencyMarketSummariesRepositoryImpl(get(), get(), get()) as CurrencyMarketSummariesRepository }
    bean { ChartsDataRepositoryImpl(get(), get(), get()) as ChartsDataRepository }
    bean { OrdersRepositoryImpl(get(), get(), get(), get()) as OrdersRepository }
    bean { CurrenciesRepositoryImpl(get(), get(), get()) as CurrenciesRepository }
    bean { TransactionsRepositoryImpl(get(), get(), get()) as TransactionsRepository }
    bean { DepositsRepositoryImpl(get(), get(), get()) as DepositsRepository }
    bean { TicketsRepositoryImpl(get(), get(), get()) as TicketsRepository }
    bean { SettingsRepositoryImpl(get()) as SettingsRepository }

}

val applicationModules = listOf(
    apiModule,
    databaseModule,
    handlersModule,
    providersModule,
    dataStoresModule,
    repositoriesModule
)