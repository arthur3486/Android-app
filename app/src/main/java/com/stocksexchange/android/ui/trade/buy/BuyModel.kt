package com.stocksexchange.android.ui.trade.buy

import com.stocksexchange.android.ui.base.mvp.model.BaseTradeModel
import com.stocksexchange.android.ui.trade.buy.BuyModel.ActionListener

class BuyModel : BaseTradeModel<ActionListener>() {


    interface ActionListener : BaseTradeActionListener


}