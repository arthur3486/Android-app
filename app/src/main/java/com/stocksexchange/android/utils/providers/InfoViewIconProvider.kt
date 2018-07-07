package com.stocksexchange.android.utils.providers

import android.content.Context
import android.graphics.drawable.Drawable
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.OrderTypes
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.utils.extensions.getColoredCompatDrawable
import com.stocksexchange.android.ui.utils.extensions.getCompatDrawable

/**
 * A helper class used for providing icons for info views.
 */
class InfoViewIconProvider(context: Context) {


    private val context: Context = context.applicationContext




    /**
     * Retrieves a drawable for the specified market.
     *
     * @param marketType The market type to get the drawable for
     *
     * @return The drawable according to the market type
     */
    fun getIconForMarketType(marketType: CurrencyMarketTypes): Drawable? {
        return when(marketType) {

            CurrencyMarketTypes.BTC -> context.getCompatDrawable(R.drawable.ic_btc)
            CurrencyMarketTypes.USDT -> context.getCompatDrawable(R.drawable.ic_usdt)
            CurrencyMarketTypes.NXT -> context.getCompatDrawable(R.drawable.ic_nxt)
            CurrencyMarketTypes.LTC -> context.getCompatDrawable(R.drawable.ic_ltc)
            CurrencyMarketTypes.ETH -> context.getCompatDrawable(R.drawable.ic_eth)
            CurrencyMarketTypes.TUSD -> context.getCompatDrawable(R.drawable.ic_coin)
            CurrencyMarketTypes.FAVORITES -> context.getColoredCompatDrawable(R.drawable.ic_star, R.color.colorInfoView)
            CurrencyMarketTypes.SEARCH -> context.getColoredCompatDrawable(R.drawable.ic_search, R.color.colorInfoView)

        }
    }


    /**
     * Retrieves a drawable for the specified order type.
     *
     * @param orderType The order type to get the drawable for
     *
     * @return The drawable according to the order type
     */
    fun getIconForOrderType(orderType: OrderTypes): Drawable? {
        return context.getColoredCompatDrawable(R.drawable.ic_swap_horizontal, R.color.colorInfoView)
    }


    /**
     * Retrieves a drawable for displaying on the chart when
     * there is no data or an error occurred.
     *
     * @return The drawable for the chart
     */
    fun getChartIcon(): Drawable? {
        return context.getColoredCompatDrawable(R.drawable.ic_finance, R.color.colorInfoView)
    }


    /**
     * Retrieves a summary icon to display whenever no summary
     * data is available.
     *
     * @return The drawable for the summary
     */
    fun getSummaryIcon(): Drawable? {
        return context.getColoredCompatDrawable(R.drawable.ic_information_outline, R.color.colorInfoView)
    }


    /**
     * Retrieves an icon for the specified wallet mode.
     *
     * @param walletMode The mode to the icon for
     *
     * @return The drawable according to the wallet mode
     */
    fun getWalletsIconForMode(walletMode: WalletModes): Drawable? {
        return when(walletMode) {
            WalletModes.STANDARD -> context.getColoredCompatDrawable(R.drawable.ic_wallet, R.color.colorInfoView)
            WalletModes.SEARCH -> context.getColoredCompatDrawable(R.drawable.ic_search, R.color.colorInfoView)
        }
    }


    /**
     * Retrieves a drawable for the specified transaction mode and type.
     *
     * @param mode The transaction mode to get the drawable for
     * @param type The transaction type to get the drawable for
     *
     * @return The drawable according to the transaction type and mode
     */
    fun getTransactionsIconForType(mode: TransactionModes, type: TransactionTypes): Drawable? {
        return when(mode) {

            TransactionModes.STANDARD -> {
                when(type) {
                    TransactionTypes.DEPOSITS -> context.getColoredCompatDrawable(R.drawable.ic_deposit, R.color.colorInfoView)
                    TransactionTypes.WITHDRAWALS -> context.getColoredCompatDrawable(R.drawable.ic_withdrawal, R.color.colorInfoView)
                }
            }

            TransactionModes.SEARCH -> {
                context.getColoredCompatDrawable(R.drawable.ic_search, R.color.colorInfoView)
            }

        }
    }


    /**
     * Retrieves a deposit drawable.
     *
     * @return The deposit drawable
     */
    fun getDepositIcon(): Drawable? {
        return context.getColoredCompatDrawable(R.drawable.ic_deposit, R.color.colorInfoView)
    }


    /**
     * Retrieves a tickets icon for the specified ticket mode.
     *
     * @param mode The mode to get the drawable for
     *
     * @return The tickets icon
     */
    fun getTicketsIcon(mode: TicketModes): Drawable? {
        return when(mode) {
            TicketModes.STANDARD -> context.getColoredCompatDrawable(R.drawable.ic_bookmark, R.color.colorInfoView)
            TicketModes.SEARCH -> context.getColoredCompatDrawable(R.drawable.ic_search, R.color.colorInfoView)
        }
    }


}