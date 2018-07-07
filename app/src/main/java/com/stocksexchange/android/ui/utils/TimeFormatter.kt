package com.stocksexchange.android.ui.utils

import android.content.Context
import com.stocksexchange.android.R
import com.stocksexchange.android.api.model.ChartDataIntervals
import com.stocksexchange.android.ui.utils.extensions.getLocale
import java.text.SimpleDateFormat
import java.util.*

/**
 * A singleton class containing all the goodies for formatting
 * time data. Requires a locale for proper formatting across
 * different countries/regions.
 */
class TimeFormatter private constructor(context: Context) {


    companion object {

        @Volatile
        private var INSTANCE : TimeFormatter? = null


        fun getInstance(context: Context): TimeFormatter {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildTimeFormatter(context).also { INSTANCE = it }
            }
        }


        private fun buildTimeFormatter(context: Context): TimeFormatter {
            return TimeFormatter(context)
        }

    }

    /**
     * Needed to make formatting locale-insensitive.
     */
    private val locale: Locale = context.getLocale()

    /**
     * An object of [SimpleDateFormat] containing the format of chart's X axis date values.
     */
    private val chartXAxisFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)

    /**
     * An object [SimpleDateFormat] containing the format for formatting dates.
     */
    private val dateFormatter = SimpleDateFormat(
        "yyyy-MM-dd ${if(isTwelveHourTimeZone(context)) "hh:mm:ss aa" else "HH:mm:ss"}",
        locale
    )

    /**
     * An instance of [Calendar] object.
     */
    private val calendar = Calendar.getInstance(locale)


    /**
     * Formats the date string according to the interval.
     *
     * @param context The context
     * @param date The date string to parse
     * @param interval The interval the date string's parsing should be based on
     *
     * @return The date formatted according to the interval
     */
    fun formatChartXAxis(context: Context, date: String, interval: String): String {
        val timeInMillis = chartXAxisFormatter.parse(date).time

        return when(interval) {
            ChartDataIntervals.ONE_DAY.intervalName -> formatOneDayInterval(context, timeInMillis)
            ChartDataIntervals.ONE_WEEK.intervalName -> formatOneWeekInterval(context, timeInMillis)
            ChartDataIntervals.ONE_MONTH.intervalName -> formatMonthsInterval(timeInMillis)
            ChartDataIntervals.THREE_MONTHS.intervalName -> formatMonthsInterval(timeInMillis)

            else -> formatOneYearInterval(context, timeInMillis)
        }
    }


    /**
     * Formats time according to one day interval.
     *
     * @param context The context
     * @param timeInMillis The time in milliseconds to format
     *
     * @return The formatted time
     */
    fun formatOneDayInterval(context: Context, timeInMillis: Long): String {
        return formatTime(
            context,
            getHourOfDay(timeInMillis),
            getMinute(timeInMillis),
            true
        )
    }


    /**
     * Formats time according to one week interval.
     *
     * @param context The context
     * @param timeInMillis The time in milliseconds to format
     *
     * @return The formatted time
     */
    fun formatOneWeekInterval(context: Context, timeInMillis: Long): String {
        return context.getString(when(getDayOfWeek(timeInMillis)) {
            Calendar.MONDAY -> R.string.monday_short
            Calendar.TUESDAY -> R.string.tuesday_short
            Calendar.WEDNESDAY -> R.string.wednesday_short
            Calendar.THURSDAY -> R.string.thursday_short
            Calendar.FRIDAY -> R.string.friday_short
            Calendar.SATURDAY -> R.string.saturday_short

            else -> R.string.sunday_short
        })
    }


    /**
     * Formats time according to months interval.
     *
     * @param timeInMillis The time in milliseconds to format
     *
     * @return The formatted time
     */
    fun formatMonthsInterval(timeInMillis: Long): String {
        val dayOfMonth = getDayOfMonth(timeInMillis)
        val month = (getMonth(timeInMillis) + 1)

        return if(locale == Locale.US) {
            "$month/$dayOfMonth"
        } else {
            "$dayOfMonth/$month"
        }
    }


    /**
     * Formats time according to one year interval.
     *
     * @param timeInMillis The time in milliseconds to format
     *
     * @return The formatted time
     */
    fun formatOneYearInterval(context: Context, timeInMillis: Long): String {
        return context.getString(when(getMonth(timeInMillis)) {
            Calendar.JANUARY -> R.string.january_short
            Calendar.FEBRUARY -> R.string.february_short
            Calendar.MARCH -> R.string.march_short
            Calendar.APRIL -> R.string.april_short
            Calendar.MAY -> R.string.may_short
            Calendar.JUNE -> R.string.june_short
            Calendar.JULY -> R.string.july_short
            Calendar.AUGUST -> R.string.august_short
            Calendar.SEPTEMBER -> R.string.september_short
            Calendar.OCTOBER -> R.string.october_short
            Calendar.NOVEMBER -> R.string.november_short

            else -> R.string.december_short
        })
    }


    /**
     * A generic method for formatting time in the following format: MM:SS AM/PM (in countries
     * that support AM/PM) or MM:SS (in countries that do support 24 hour format).
     *
     * @param context The context
     * @param hours The hours to format
     * @param minutes The minutes to format
     * @param appendDayPeriod Whether or not to append day period (AM / PM). Onlly applicable
     * to countries that support such format.
     *
     * @return The formatted time
     */
    fun formatTime(context: Context, hours: Int, minutes: Int, appendDayPeriod: Boolean): String {
        return if(isTwelveHourTimeZone(context)) {
            val adjustedHours: Int = if(hours >= 12) {
                (hours - 12)
            } else {
                hours
            }

            var result = when {
                (adjustedHours == 0) -> "12"
                else -> adjustedHours.toString()
            } + ":" + when {
                (minutes < 10) -> "0$minutes"
                else -> minutes.toString()
            }

            if(appendDayPeriod) {
                result += (if (isAm(context, hours)) " AM" else " PM")
            }

            return result
        } else {
            when {
                (hours < 10) -> "0$hours"
                else -> hours.toString()
            } + ":" + when {
                (minutes < 10) -> "0$minutes"
                else -> minutes.toString()
            }
        }
    }


    /**
     * Formats the passed in timestamp in the following format: 2018-04-19 14:06:32.
     * If the device's time zone is in 12 hour time zone, then the time will be
     * 2018-04-19 02:06:32 PM.
     *
     * @param timestamp The unix time to format
     *
     * @return The timestamp formatted in the aforementioned way
     */
    fun formatDate(timestamp: Long): String {
        return dateFormatter.format(Date(timestamp * 1000L))
    }


    /**
     * Fetches the year from the timestamp.
     *
     * @param timeInMillis The time in milliseconds
     *
     * @return The year from the timestamp
     */
    fun getYear(timeInMillis: Long): Int {
        calendar.timeInMillis = timeInMillis

        return calendar.get(Calendar.YEAR)
    }


    /**
     * Fetches the month from the timestamp.
     *
     * @param timeInMillis The time in milliseconds
     *
     * @return The month from the timestamp
     */
    fun getMonth(timeInMillis: Long): Int {
        calendar.timeInMillis = timeInMillis

        return calendar.get(Calendar.MONTH)
    }


    /**
     * Fetches the day of month from the timestamp.
     *
     * @param timeInMillis The time in milliseconds
     *
     * @return The day of month from the timestamp
     */
    fun getDayOfMonth(timeInMillis: Long): Int {
        calendar.timeInMillis = timeInMillis

        return calendar.get(Calendar.DAY_OF_MONTH)
    }


    /**
     * Fetches the day of week from the timestamp.
     *
     * @param timeInMillis The time in milliseconds
     *
     * @return The day of week from the timestamp
     */
    fun getDayOfWeek(timeInMillis: Long): Int {
        calendar.timeInMillis = timeInMillis

        return calendar.get(Calendar.DAY_OF_WEEK)
    }


    /**
     * Fetches the hour from the timestamp.
     *
     * @param timeInMillis The time in milliseconds
     *
     * @return The hour from the timestamp
     */
    fun getHour(context: Context, timeInMillis: Long): Int {
        calendar.timeInMillis = timeInMillis

        return calendar.get(if(isTwelveHourTimeZone(context)) {
            Calendar.HOUR
        } else {
            Calendar.HOUR_OF_DAY
        })
    }


    /**
     * Fetches the hour of day from the timestamp.
     *
     * @param timeInMillis The time in milliseconds
     *
     * @return The hour of day from the timestamp
     */
    fun getHourOfDay(timeInMillis: Long): Int {
        calendar.timeInMillis = timeInMillis

        return calendar.get(Calendar.HOUR_OF_DAY)
    }


    /**
     * Fetches the minute from the timestamp.
     *
     * @param timeInMillis The time in milliseconds
     *
     * @return The minute from the timestamp
     */
    fun getMinute(timeInMillis: Long): Int {
        calendar.timeInMillis = timeInMillis

        return calendar.get(Calendar.MINUTE)
    }


    /**
     * Fetches the second from the timestamp.
     *
     * @param timeInMillis The time in milliseconds
     *
     * @return The second from the timestamp
     */
    fun getSecond(timeInMillis: Long): Int {
        calendar.timeInMillis = timeInMillis

        return calendar.get(Calendar.SECOND)
    }


    /**
     * Fetches the millisecond from the timestamp.
     *
     * @param timeInMillis The time in milliseconds
     *
     * @return The millisecond from the timestamp
     */
    fun getMillisecond(timeInMillis: Long): Int {
        calendar.timeInMillis = timeInMillis

        return calendar.get(Calendar.MILLISECOND)
    }


    /**
     * Determines whether the device supports 12 hour time zone or not.
     *
     * @param context The context
     *
     * @return true if supports; false otherwise
     */
    fun isTwelveHourTimeZone(context: Context): Boolean {
        return !android.text.format.DateFormat.is24HourFormat(context)
    }


    /**
     * Determines whether the hour of day is in the AM range according
     * to the 12 hour time zone.
     *
     * @param context The context
     * @param hourOfDay The hour of day
     *
     * @return true if the hour of day resides in the AM range; false otherwise
     */
    fun isAm(context: Context, hourOfDay: Int): Boolean {
        return if(isTwelveHourTimeZone(context)) {
            (hourOfDay < 12)
        } else {
            false
        }
    }


    /**
     * Determines whether the hour of day is in the PM range according
     * to the 12 hour time zone.
     *
     * @param context The context
     * @param hourOfDay The hour of day
     *
     * @return true if the hour of day resides in the PM range; false otherwise
     */
    fun isPm(context: Context, hourOfDay: Int): Boolean {
        return if(isTwelveHourTimeZone(context)) {
            (hourOfDay >= 12)
        } else {
            false
        }
    }


}