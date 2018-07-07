package com.stocksexchange.android.database.converters

import android.arch.persistence.room.TypeConverter
import com.stocksexchange.android.model.DecimalSeparators

/**
 * A converter used for transforming [DecimalSeparators]
 * object(-s) to a JSON string and vice versa.
 */
object DecimalSeparatorConverter {


    /**
     * Converts a [DecimalSeparators] object to a string.
     *
     * @param decimalSeparator The [DecimalSeparators] object
     * to convert to a string
     *
     * @return The string representation of the [DecimalSeparators]
     * object
     */
    @TypeConverter
    @JvmStatic
    fun fromDecimalSeparator(decimalSeparator: DecimalSeparators): String {
        return decimalSeparator.name
    }


    /**
     * Converts a string into an [DecimalSeparators] object.
     *
     * @param name The name string of the [DecimalSeparators] object
     *
     * @return The [DecimalSeparators] object constructed from
     * the string
     */
    @TypeConverter
    @JvmStatic
    fun toDecimalSeparator(name: String): DecimalSeparators {
        return DecimalSeparators.valueOf(name)
    }


}