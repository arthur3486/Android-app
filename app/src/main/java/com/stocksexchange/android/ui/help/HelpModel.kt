package com.stocksexchange.android.ui.help

import com.stocksexchange.android.R
import com.stocksexchange.android.model.HelpItem
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import org.koin.standalone.inject

class HelpModel : BaseModel() {


    private val mStringProvider: StringProvider by inject()




    fun getHelpItems(): MutableList<HelpItem> {
        val items = mutableListOf<HelpItem>()

/*        items.add(HelpItem(
            stringProvider.getString(R.string.help_item_too_many_requests_question),
            stringProvider.getString(R.string.help_item_too_many_requests_answer)
        ))*/
        items.add(HelpItem(
            mStringProvider.getString(R.string.help_item_server_unresponsive_question),
            mStringProvider.getString(R.string.help_item_server_unresponsive_answer)
        ))
        items.add(HelpItem(
            mStringProvider.getString(R.string.help_item_something_went_wrong_question),
            mStringProvider.getString(R.string.help_item_something_went_wrong_answer)
        ))
        items.add(HelpItem(
            mStringProvider.getString(R.string.help_item_no_withdrawals_question),
            mStringProvider.getString(R.string.help_item_no_withdrawals_answer)
        ))
        items.add(HelpItem(
            mStringProvider.getString(R.string.help_item_exchange_disabled_question),
            mStringProvider.getString(R.string.help_item_exchange_disabled_answer)
        ))
/*        items.add(HelpItem(
            mStringProvider.getString(R.string.help_item_tickets_vs_feedback_question),
            mStringProvider.getString(R.string.help_item_tickets_vs_feedback_answer)
        ))todo*/
        items.add(HelpItem(
            mStringProvider.getString(R.string.help_item_fiat_money_recharge_question),
            mStringProvider.getString(R.string.help_item_fiat_money_recharge_answer)
        ))
        items.add(HelpItem(
            mStringProvider.getString(R.string.help_item_assets_dividends_question),
            mStringProvider.getString(R.string.help_item_assets_dividends_answer)
        ))
        items.add(HelpItem(
            mStringProvider.getString(R.string.help_item_stex_question),
            mStringProvider.getString(R.string.help_item_stex_answer)
        ))

        return items
    }


}