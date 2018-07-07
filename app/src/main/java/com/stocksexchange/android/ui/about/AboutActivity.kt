package com.stocksexchange.android.ui.about

import android.content.Context
import android.content.Intent
import android.support.v7.widget.PopupMenu
import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.R
import com.stocksexchange.android.utils.handlers.BrowserHandler
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.utils.extensions.getCompatDrawable
import com.stocksexchange.android.ui.utils.extensions.makeVisible
import com.stocksexchange.android.ui.utils.extensions.setLeftDrawable
import kotlinx.android.synthetic.main.about_activity_feature_layout.view.*
import kotlinx.android.synthetic.main.about_activity_header_layout.*
import kotlinx.android.synthetic.main.about_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.jetbrains.anko.intentFor
import org.koin.android.ext.android.get

class AboutActivity : BaseActivity<AboutPresenter>(), AboutContract.View {


    companion object {

        fun newInstance(context: Context): Intent {
            return context.intentFor<AboutActivity>()
        }

    }


    private var mPopupMenu: PopupMenu? = null




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.horizontal_sliding_right_to_left_enter_animation,
            R.anim.default_window_exit_animation
        )
    }


    override fun initPresenter(): AboutPresenter = AboutPresenter(this)


    override fun init() {
        initToolbar()
        initHeader()
        initFeatures()
        initVisitOurWebsiteButton()
    }


    private fun initToolbar() {
        toolbar.returnBackBtnIv.setOnClickListener { onBackPressed() }

        toolbar.titleTv.text = getString(R.string.about)

        toolbar.actionBtnIv.setImageResource(R.drawable.ic_dots_vertical)
        toolbar.actionBtnIv.setOnClickListener { mPresenter?.onActionButtonClicked() }
        toolbar.actionBtnIv.makeVisible()
    }


    private fun initHeader() {
        appVersionTv.text = getString(
            R.string.about_activity_app_version_template,
            BuildConfig.VERSION_NAME
        )
    }


    private fun initFeatures() {
        cryptoFeatureLl.iconIv.setImageResource(R.drawable.ic_bitcoin)
        cryptoFeatureLl.captionTv.text = getString(R.string.about_activity_feature_crypto_caption)

        auditFeatureLl.iconIv.setImageResource(R.drawable.ic_lock)
        auditFeatureLl.captionTv.text = getString(R.string.about_activity_feature_audit_caption)

        storageFeatureLl.iconIv.setImageResource(R.drawable.ic_cloud_upload)
        storageFeatureLl.captionTv.text = getString(R.string.about_activity_feature_storage_caption)
    }


    private fun initVisitOurWebsiteButton() {
        visitOurWebsiteBtn.setLeftDrawable(getCompatDrawable(R.drawable.ic_web))
        visitOurWebsiteBtn.setOnClickListener { mPresenter?.onVisitOurWebsiteButtonClicked() }
    }


    override fun launchBrowser(url: String) {
        get<BrowserHandler>().launchBrowser(this, url)
    }


    override fun showPopupMenu() {
        mPopupMenu = PopupMenu(this, toolbar.actionBtnIv)
        mPopupMenu?.inflate(R.menu.about_activity_overflow_menu)
        mPopupMenu?.setOnMenuItemClickListener {
            when(it.itemId) {

                R.id.termsOfUse -> {
                    mPresenter?.onTermsOfUseMenuItemClicked()
                    true
                }

                else -> false

            }
        }
        mPopupMenu?.show()
    }


    override fun hidePopupMenu() {
        mPopupMenu?.dismiss()
    }


    override fun getContentLayoutResourceId() = R.layout.about_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.default_window_enter_animation,
            R.anim.horizontal_sliding_right_to_left_exit_animation
        )
    }


}