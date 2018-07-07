package com.stocksexchange.android.ui.tickets.creation

interface TicketCreationContract {


    interface View {

        fun showToast(message: String)

        fun showProgressBar()

        fun hideProgressBar()

        fun showCategoryDialog()

        fun dismissCategoryDialog()

        fun showKeyboard()

        fun updateCategoryButtonText(text: String)

        fun updateSendButtonState()

        fun requestMessageEtFocus()

        fun finishActivity()

        fun getCategoryButtonText(): String

        fun getSubject(): String

        fun getMessage(): String

    }


    interface ActionListener {

        fun onActionButtonClicked()

        fun onSubjectEtTextChanged(text: String)

        fun onCategoryButtonClicked()

        fun onCategoryPicked(category: String)

        fun onScrollViewClicked()

        fun onMessageEtTextChanged(text: String)

    }


}