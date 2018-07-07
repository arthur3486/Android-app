package com.stocksexchange.android.ui.help

import com.stocksexchange.android.model.HelpItem

interface HelpContract {


    interface View {

        fun showPopupMenu()

        fun hidePopupMenu()

        fun launchFeedbackActivity()

        fun launchAboutActivity()

        fun setItems(items: MutableList<HelpItem>)

        fun isDataSetEmpty(): Boolean

    }


    interface ActionListener {

        fun onActionButtonClicked()

        fun onFeedbackMenuItemClicked()

        fun onAboutMenuItemClicked()

    }


}