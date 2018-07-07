package com.stocksexchange.android.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.stocksexchange.android.api.model.AssetsPortfolio
import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.api.model.UserSession
import com.stocksexchange.android.database.model.DatabaseUser.Companion.TABLE_NAME

/**
 * A Room database model class of [User] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseUser(
    @PrimaryKey @ColumnInfo(name = USER_NAME) var userName: String,
    @ColumnInfo(name = EMAIL) var email: String,
    @ColumnInfo(name = HASH) var hash: String,
    @ColumnInfo(name = SESSIONS) var sessions: List<UserSession>,
    @ColumnInfo(name = FUNDS) var funds: Map<String, String>,
    @ColumnInfo(name = HOLD_FUNDS) var holdFunds: Map<String, String>,
    @ColumnInfo(name = WALLET_ADDRESSES) var walletAddresses: Map<String, String>,
    @ColumnInfo(name = WALLET_PUBLIC_KEYS) var walletPublicKeys: Map<String, String>,
    @ColumnInfo(name = ASSETS_PORTFOLIO) var assetsPortfolio: AssetsPortfolio?,
    @ColumnInfo(name = OPEN_ORDERS) var openOrders: Int
) {

    companion object {

        const val TABLE_NAME = "users"

        const val USER_NAME = "user_name"
        const val EMAIL = "email"
        const val HASH = "hash"
        const val SESSIONS = "sessions"
        const val FUNDS = "funds"
        const val HOLD_FUNDS = "hold_funds"
        const val WALLET_ADDRESSES = "wallet_addresses"
        const val WALLET_PUBLIC_KEYS = "wallet_public_keys"
        const val ASSETS_PORTFOLIO = "assets_portfolio"
        const val OPEN_ORDERS = "open_orders"

    }


    constructor() : this("", "", "", listOf(), mapOf(), mapOf(), mapOf(), mapOf(), null, -1)

}