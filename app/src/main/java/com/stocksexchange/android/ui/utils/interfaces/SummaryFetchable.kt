package com.stocksexchange.android.ui.utils.interfaces

import com.stocksexchange.android.api.model.CurrencyMarketSummary

interface SummaryFetchable {

    fun getSummary(): CurrencyMarketSummary?

}