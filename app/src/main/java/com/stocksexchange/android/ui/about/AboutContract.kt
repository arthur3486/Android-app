package com.stocksexchange.android.ui.about

interface AboutContract {


    interface View {

        fun showPopupMenu()

        fun hidePopupMenu()

        fun launchBrowser(url: String)

    }


    interface ActionListener {

        fun onActionButtonClicked()

        fun onTermsOfUseMenuItemClicked()

        fun onVisitOurWebsiteButtonClicked()

    }


}