package com.stocksexchange.android.ui.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * A formatter used for formatting double values.
 */
class DoubleFormatter private constructor(locale: Locale) {


    companion object {

        private const val ONE_HUNDRED_MILLIONTH = 0.00000001
        private const val TEN = 10.0
        private const val ONE_HUNDRED = 100.0
        private const val ONE_THOUSAND = 1_000.0
        private const val TEN_THOUSAND = 10_000.0
        private const val ONE_HUNDRED_THOUSAND = 100_000.0
        private const val ONE_MILLION = 1_000_000.0
        private const val TEN_MILLION = 10_000_000.0
        private const val ONE_HUNDRED_MILLION = 100_000_000.0
        private const val ONE_BILLION = 1_000_000_000.0


        @Volatile
        private var INSTANCE : DoubleFormatter? = null


        fun getInstance(locale: Locale): DoubleFormatter {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildTimeFormatter(locale).also { INSTANCE = it }
            }
        }


        private fun buildTimeFormatter(locale: Locale): DoubleFormatter {
            return DoubleFormatter(locale)
        }

    }


    /**
     * A formatter for formatting double values. An instance of [DecimalFormat].
     */
    private val formatter: DecimalFormat = (NumberFormat.getInstance(locale) as DecimalFormat)




    init {
        formatter.isGroupingUsed = false
    }


    private fun format(payload: Payload): String {
        formatter.applyPattern(payload.pattern)
        return formatter.format(payload.value)
    }


    /**
     * Formats the market's volume real number to a string representation.
     *
     * @param value The value to format
     *
     * @return The market's volume in a string representation
     */
    fun formatVolume(value: Double): String = format(getVolumePayload(value))


    /**
     * Formats the price real number to a string representation.
     *
     * @param value The value to format
     *
     * @return The price in a string representation
     */
    fun formatPrice(value: Double): String = format(getPricePayload(value))


    /**
     * Formats the market's daily change real number to a string representation.
     *
     * @param value The value to format
     *
     * @return The market's daily change in a string representation
     */
    fun formatDailyChange(value: Double): String = format(getDailyChangePayload(value))


    /**
     * Formats the min order amount real number to a string representation.
     *
     * @param value The value to format
     *
     * @return The min order amount in a string representation
     */
    fun formatMinOrderAmount(value: Double): String = format(getMinOrderAmountPayload(value))


    /**
     * Formats the fee percent real number to a string representation.
     *
     * @param value The value to format
     *
     * @return The fee percent in a string representation
     */
    fun formatFeePercent(value: Double): String = format(getFeePercentPayload(value))


    /**
     * Formats the transaction fee real number to a string representation.
     *
     * @param value The value to format
     *
     * @return The transaction fee in a string representation
     */
    fun formatTransactionFee(value: Double): String = format(getTransactionFeePayload(value))


    /**
     * Formats the balance real number to a string representation.
     *
     * @param value The value to format
     *
     * @return The balance in a string representation
     */
    fun formatBalance(value: Double): String = format(getBalancePayload(value))


    /**
     * Formats the amount real number to a string representation.
     *
     * @param value The value to format
     *
     * @return The amount in a string representation
     */
    fun formatAmount(value: Double): String = format(getAmountPayload(value))


    /**
     * Sets the formatter's decimal separator.
     *
     * @param separator The new separator
     */
    fun setDecimalSeparator(separator: Char) {
        val symbols = formatter.decimalFormatSymbols
        symbols.decimalSeparator = separator

        formatter.decimalFormatSymbols = symbols
    }


    /**
     * Gets the formatter's decimal separator.
     *
     * @return The decimal separator
     */
    fun getDecimalSeparator(): Char {
        return formatter.decimalFormatSymbols.decimalSeparator
    }


    private fun getVolumePayload(value: Double): Payload {
        return when {
            (value < TEN) -> Payload("0.00000", value)
            (value < ONE_HUNDRED) -> Payload("00.0000", value)
            (value < ONE_THOUSAND) -> Payload("000.000", value)
            (value < TEN_THOUSAND) -> Payload("0000.00", value)
            (value < ONE_HUNDRED_THOUSAND) -> Payload("00000.0", value)
            (value < ONE_MILLION) -> Payload("000.00K", (value / ONE_THOUSAND))
            (value < TEN_MILLION) -> Payload("0.000M", (value / ONE_MILLION))
            (value < ONE_HUNDRED_MILLION) -> Payload("00.000M", (value / ONE_MILLION))
            (value < ONE_BILLION) -> Payload("000.00M", (value / ONE_MILLION))

            else -> Payload("0.000B", (value / ONE_BILLION))
        }
    }


    private fun getPricePayload(value: Double): Payload {
        return when {
            (value < TEN) -> Payload("0.00000000", value)
            (value < ONE_HUNDRED) -> Payload("00.0000000", value)
            (value < ONE_THOUSAND) -> Payload("000.000000", value)
            (value < TEN_THOUSAND) -> Payload("0000.00000", value)
            (value < ONE_HUNDRED_THOUSAND) -> Payload("00000.0000", value)
            (value < ONE_MILLION) -> Payload("000000.000", value)
            (value < TEN_MILLION) -> Payload("0000000.00", value)
            (value < ONE_HUNDRED_MILLION) -> Payload("00000000.0", value)

            else -> Payload("000000000", value)
        }
    }


    private fun getDailyChangePayload(value: Double): Payload {
        val sign = when {
            value > 0.0f -> "+"
            else -> ""
        }
        val absValue = Math.abs(value)

        return when {
            (absValue < TEN) -> Payload("${sign}0.0000'%'", value)
            (absValue < ONE_HUNDRED) -> Payload("${sign}00.000'%'", value)
            (absValue < ONE_THOUSAND) -> Payload("${sign}000.00'%'", value)
            (absValue < TEN_THOUSAND) -> Payload("${sign}0000.0'%'", value)
            (absValue < ONE_HUNDRED_THOUSAND) -> Payload("${sign}00000'%'", value)
            (absValue < ONE_MILLION) -> Payload("${sign}000.0K'%'", (value / ONE_THOUSAND))
            (absValue < TEN_MILLION) -> Payload("${sign}0.000M'%'", (value / ONE_MILLION))
            (absValue < ONE_HUNDRED_MILLION) -> Payload("${sign}00.00M'%'", (value / ONE_MILLION))
            (absValue < ONE_BILLION) -> Payload("${sign}000.0M'%'", (value / ONE_MILLION))

            else -> Payload("${sign}0.000B'%'", (value / ONE_BILLION))
        }
    }


    private fun getMinOrderAmountPayload(value: Double): Payload {
        return getPricePayload(value)
    }


    private fun getFeePercentPayload(value: Double): Payload {
        return when {
            (value < TEN) -> Payload("0.00'%'", value)
            (value < ONE_HUNDRED) -> Payload("00.00'%'", value)
            (value < ONE_THOUSAND) -> Payload("000.0'%'", value)

            else -> Payload("0000'%'", value)
        }
    }


    private fun getTransactionFeePayload(value: Double): Payload {
        return getAmountPayload(value)
    }


    private fun getBalancePayload(value: Double): Payload {
        return getPricePayload(value)
    }


    private fun getAmountPayload(value: Double): Payload {
        if(value <= 0) {
            return Payload("0.00000000", value)
        }

        return when {
            (value < ONE_HUNDRED_MILLIONTH) -> Payload("0.000E00", value)
            (value < TEN) -> Payload("0.00000000", value)
            (value < ONE_HUNDRED) -> Payload("00.0000000", value)
            (value < ONE_THOUSAND) -> Payload("000.000000", value)
            (value < TEN_THOUSAND) -> Payload("0000.00000", value)
            (value < ONE_HUNDRED_THOUSAND) -> Payload("00000.0000", value)
            (value < ONE_MILLION) -> Payload("000000.000", value)
            (value < TEN_MILLION) -> Payload("0000000.00", value)
            (value < ONE_HUNDRED_MILLION) -> Payload("00000000.0", value)

            else -> Payload("0.000E00", value)
        }
    }


    /**
     * A helper class used for holding the payload data of the formatting.
     */
    data class Payload(
        val pattern: String,
        val value: Any
    )


}