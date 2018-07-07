package com.stocksexchange.android.database.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.stocksexchange.android.api.model.AssetsPortfolio
import com.stocksexchange.android.api.utils.fromJson

/**
 * A converter used for transforming [AssetsPortfolio]
 * object(-s) to a JSON string and vice versa.
 */
object AssetsPortfolioConverter {


    /**
     * Converts an [AssetsPortfolio] object to a JSON string.
     *
     * @param assetsPortfolio The [AssetsPortfolio] object
     * to convert to JSON
     *
     * @return The JSON string of the [AssetsPortfolio] object
     */
    @TypeConverter
    @JvmStatic
    fun fromAssetsPortfolio(assetsPortfolio: AssetsPortfolio?): String {
        return Gson().toJson(assetsPortfolio)
    }


    /**
     * Converts a JSON string into an [AssetsPortfolio] object.
     *
     * @param json The JSON string of the [AssetsPortfolio] object
     *
     * @return The [AssetsPortfolio] object constructed from
     * the JSON string
     */
    @TypeConverter
    @JvmStatic
    fun toAssetsPortfolio(json: String?): AssetsPortfolio? {
        return json?.let { Gson().fromJson(it) }
    }


}