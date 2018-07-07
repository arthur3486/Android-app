package com.stocksexchange.android.ui.trade.sell

import com.stocksexchange.android.ui.base.mvp.model.BaseTradeModel
import com.stocksexchange.android.ui.trade.sell.SellModel.ActionListener

class SellModel : BaseTradeModel<ActionListener>() {


    interface ActionListener: BaseTradeActionListener


}