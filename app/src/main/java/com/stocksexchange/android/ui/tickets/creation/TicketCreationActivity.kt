package com.stocksexchange.android.ui.tickets.creation

import android.content.Context
import android.content.Intent
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.utils.KeyboardManager
import com.stocksexchange.android.ui.utils.extensions.*
import com.stocksexchange.android.ui.utils.listeners.adapters.TextWatcherAdapter
import kotlinx.android.synthetic.main.ticket_creation_actvitity_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.jetbrains.anko.intentFor

class TicketCreationActivity : BaseActivity<TicketCreationPresenter>(), TicketCreationContract.View {


    companion object {

        fun newInstance(context: Context): Intent {
            return context.intentFor<TicketCreationActivity>()
        }

    }


    /**
     * A keyboard manager for showing and hiding keyboard on the screen.
     */
    private lateinit var mKeyboardManager: KeyboardManager

    /**
     * A dialog for picking a category for a ticket.
     */
    private var mCategoryDialog: MaterialDialog? = null




    override fun preInit() {
        super.preInit()

        mKeyboardManager = KeyboardManager.newInstance(this)

        overridePendingTransition(
            R.anim.vertical_sliding_bottom_to_top_window_b_enter_animation,
            R.anim.vertical_sliding_bottom_to_top_window_a_exit_animation
        )
    }


    override fun initPresenter(): TicketCreationPresenter = TicketCreationPresenter(this)


    override fun init() {
        initToolbar()
        initScrollView()
        initCardView()
    }


    private fun initToolbar() {
        toolbar.returnBackBtnIv.setOnClickListener { onBackPressed() }

        toolbar.titleTv.setText(R.string.ticket_creation_toolbar_title)
        toolbar.progressBar.setColor(R.color.colorProgressBar)

        toolbar.actionBtnIv.setImageDrawable(getColoredCompatDrawable(
            R.drawable.ic_send,
            R.color.colorAccent
        ))
        toolbar.actionBtnIv.setOnClickListener { mPresenter?.onActionButtonClicked() }
        toolbar.actionBtnIv.makeVisible()

        updateSendButtonState()
    }


    private fun initScrollView() {
        scrollView.mInterceptableClickListener = View.OnClickListener {
            mPresenter?.onScrollViewClicked()
        }
    }


    private fun initCardView() {
        initTopBar()
        initSeparator()
        initMessageEt()
    }


    private fun initTopBar() {
        subjectEt.setCursorDrawable(getColoredCompatDrawable(
            R.drawable.edit_text_cursor_drawable,
            R.color.colorSecondaryText
        ))
        subjectEt.addTextChangedListener(object : TextWatcherAdapter {

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                mPresenter?.onSubjectEtTextChanged(text.toString())
            }

        })

        categoryBtnTv.background = getLayerDrawable(
            R.drawable.secondary_button_background,
            R.color.colorYellowAccent,
            R.color.colorCardView
        )
        categoryBtnTv.setOnClickListener { mPresenter?.onCategoryButtonClicked() }
    }


    private fun initSeparator() {
        separatorIv.setImageDrawable(getDottedLineDrawable(R.color.colorSecondaryText))
    }


    private fun initMessageEt() {
        messageEt.setCursorDrawable(getColoredCompatDrawable(
            R.drawable.edit_text_cursor_drawable,
            R.color.colorSecondaryText
        ))
        messageEt.addTextChangedListener(object : TextWatcherAdapter {

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                mPresenter?.onMessageEtTextChanged(text.toString())
            }

        })
    }


    override fun showProgressBar() {
        toolbar.actionBtnIv.makeGone()
        toolbar.progressBar.makeVisible()
    }


    override fun hideProgressBar() {
        toolbar.actionBtnIv.makeVisible()
        toolbar.progressBar.makeGone()
    }


    override fun showCategoryDialog() {
        val dialog = MaterialDialog.Builder(this)
            .items(R.array.ticket_categories)
            .itemsCallback { _, _, _, text ->  mPresenter?.onCategoryPicked(text.toString()) }
            .backgroundColorRes(R.color.colorPrimary)
            .contentColorRes(R.color.colorPrimaryText)
            .build()

        mCategoryDialog = dialog
        mCategoryDialog?.show()
    }


    override fun dismissCategoryDialog() {
        mCategoryDialog?.dismiss()
    }


    override fun showKeyboard() {
        mKeyboardManager.showKeyboard(messageEt)
    }


    override fun updateCategoryButtonText(text: String) {
        categoryBtnTv.text = text
    }


    override fun updateSendButtonState() {
        val isFormFilledOut = !(
            subjectEt.isEmpty() ||
            getCategoryButtonText() == getString(R.string.category) ||
            messageEt.isEmpty()
        )

        if(isFormFilledOut) {
            toolbar.actionBtnIv.enable(true)
        } else {
            toolbar.actionBtnIv.disable(true)
        }
    }


    override fun requestMessageEtFocus() {
        messageEt.requestFocus()
    }


    override fun finishActivity() {
        onBackPressed()
    }


    override fun getCategoryButtonText(): String {
        return categoryBtnTv.text.toString()
    }


    override fun getSubject(): String {
        return subjectEt.getContent()
    }


    override fun getMessage(): String {
        return messageEt.getContent()
    }


    override fun getContentLayoutResourceId(): Int = R.layout.ticket_creation_actvitity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.vertical_sliding_bottom_to_top_window_a_enter_animation,
            R.anim.vertical_sliding_bottom_to_top_window_b_exit_animation
        )
    }


    override fun onRecycle(isChangingConfigurations: Boolean) {
        super.onRecycle(isChangingConfigurations)

        mKeyboardManager.recycle()
    }


}