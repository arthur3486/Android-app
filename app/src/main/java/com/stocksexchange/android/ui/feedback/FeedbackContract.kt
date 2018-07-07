package com.stocksexchange.android.ui.feedback

interface FeedbackContract {


    interface View {

        fun showToast(message: String)

        fun showKeyboard()

        fun enableSendButton()

        fun disableSendButton()

        fun sendFeedbackEmail()

    }


    interface ActionListener {

        fun onSendButtonClicked()

        fun onContentContainerClicked()

    }


}