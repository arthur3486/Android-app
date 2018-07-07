package com.stocksexchange.android.ui.feedback

import com.stocksexchange.android.R
import com.stocksexchange.android.utils.providers.EmailProvider
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.ui.utils.listeners.QueryListener
import org.koin.standalone.inject

class FeedbackPresenter(
    model: StubModel,
    view: FeedbackContract.View
) : BasePresenter<StubModel, FeedbackContract.View>(model, view), FeedbackContract.ActionListener,
    QueryListener.Callback {


    private val mEmailProvider: EmailProvider by inject()
    private val mStringProvider: StringProvider by inject()




    constructor(view: FeedbackContract.View): this(StubModel(), view)


    override fun onQueryEntered(query: String) {
        mView.enableSendButton()
    }


    override fun onQueryRemoved() {
        mView.disableSendButton()
    }


    override fun onSendButtonClicked() {
        if(mEmailProvider.isMailClientPresenter()) {
            mView.sendFeedbackEmail()
        } else {
            mView.showToast(mStringProvider.getString(R.string.error_no_email_client))
        }
    }


    override fun onContentContainerClicked() {
        mView.showKeyboard()
    }


}