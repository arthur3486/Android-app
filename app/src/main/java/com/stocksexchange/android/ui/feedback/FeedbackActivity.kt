package com.stocksexchange.android.ui.feedback

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.R
import com.stocksexchange.android.REQUEST_CODE_NEW_EMAIL
import com.stocksexchange.android.utils.helpers.composeFeedbackEmailSubject
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.utils.KeyboardManager
import com.stocksexchange.android.ui.utils.extensions.*
import com.stocksexchange.android.ui.utils.listeners.QueryListener
import kotlinx.android.synthetic.main.feedback_activity_footer_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.jetbrains.anko.intentFor

class FeedbackActivity : BaseActivity<FeedbackPresenter>(), FeedbackContract.View,
    QueryListener.Callback{


    companion object {

        fun newInstance(context: Context): Intent {
            return context.intentFor<FeedbackActivity>()
        }

    }


    /**
     * A keyboard manager for showing and hiding keyboard on the screen.
     */
    private lateinit var mKeyboardManager: KeyboardManager




    override fun preInit() {
        super.preInit()

        mKeyboardManager = KeyboardManager.newInstance(this)

        overridePendingTransition(
            R.anim.horizontal_sliding_right_to_left_enter_animation,
            R.anim.default_window_exit_animation
        )
    }


    override fun initPresenter(): FeedbackPresenter = FeedbackPresenter(this)


    override fun init() {
        initToolbar()
        initCardView()
    }


    private fun initToolbar() {
        returnBackBtnIv.setOnClickListener { onBackPressed() }

        titleTv.setText(R.string.feedback)
    }


    private fun initCardView() {
        initTopBar()
        initSeparator()
        initMainScrollView()
        initInputEditText()
    }


    private fun initTopBar() {
        sendBtnTv.background = getLayerDrawable(
            R.drawable.secondary_button_background,
            R.color.colorFeedbackFooterButton,
            R.color.colorCardView
        )
        sendBtnTv.setOnClickListener { mPresenter?.onSendButtonClicked() }
        disableSendButton()
    }


    private fun initSeparator() {
        separatorIv.setImageDrawable(getDottedLineDrawable(R.color.colorSecondaryText))
    }


    private fun initMainScrollView() {
        scrollView.mInterceptableClickListener = View.OnClickListener {
            mPresenter?.onContentContainerClicked()
        }
    }


    private fun initInputEditText() {
        feedbackTextEt.setCursorDrawable(getColoredCompatDrawable(
            R.drawable.edit_text_cursor_drawable,
            R.color.colorSecondaryText
        ))
        feedbackTextEt.addTextChangedListener(QueryListener(this))
    }


    override fun showKeyboard() {
        feedbackTextEt.requestFocus()
        mKeyboardManager.showKeyboard(feedbackTextEt)
    }


    override fun enableSendButton() {
        sendBtnTv.enable(true)
    }


    override fun disableSendButton() {
        sendBtnTv.disable(true)
    }


    override fun sendFeedbackEmail() {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", BuildConfig.FEEDBACK_EMAIL_ADDRESS, null)
        )

        intent.putExtra(Intent.EXTRA_SUBJECT, composeFeedbackEmailSubject(getString(R.string.app_name)))
        intent.putExtra(Intent.EXTRA_TEXT, feedbackTextEt.text.toString())

        startActivityForResult(intent, REQUEST_CODE_NEW_EMAIL)
    }


    override fun getContentLayoutResourceId() = R.layout.feedback_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.default_window_enter_animation,
            R.anim.horizontal_sliding_right_to_left_exit_animation
        )
    }


    override fun onQueryEntered(query: String) {
        mPresenter?.onQueryEntered(query)
    }


    override fun onQueryRemoved() {
        mPresenter?.onQueryRemoved()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if((requestCode == REQUEST_CODE_NEW_EMAIL) && (resultCode == Activity.RESULT_OK)) {
            finish()
        }
    }


    override fun onRecycle(isChangingConfigurations: Boolean) {
        super.onRecycle(isChangingConfigurations)

        mKeyboardManager.recycle()
    }


}