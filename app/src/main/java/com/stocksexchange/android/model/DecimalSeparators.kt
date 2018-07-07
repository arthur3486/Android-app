package com.stocksexchange.android.model

/**
 * An enumeration of all possible decimal separators.
 */
enum class DecimalSeparators(val separator: Char) {


    PERIOD('.'),
    COMMA(',');




    companion object {

        /**
         * Returns a decimal separator enumeration for a separator character.
         *
         * @param separator The character separator
         *
         * @return The decimal separator enumeration
         */
        fun getEnumForSeparator(separator: String): DecimalSeparators {
            return when(separator) {
                "." -> PERIOD
                else -> COMMA
            }
        }

    }


}