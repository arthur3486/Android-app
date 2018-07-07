package com.stocksexchange.android.ui.settings

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.stocksexchange.android.R
import com.stocksexchange.android.REQUEST_CODE_RINGTONE_ACTIVITY
import com.stocksexchange.android.REQUEST_CODE_RINGTONE_PERMISSION
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.utils.helpers.isPermissionSetGranted
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.utils.extensions.checkPermissions
import com.stocksexchange.android.ui.utils.extensions.getLocale
import com.stocksexchange.android.ui.utils.extensions.makeGone
import com.stocksexchange.android.ui.utils.extensions.makeVisible
import com.stocksexchange.android.ui.views.DeviceMetricsDialog
import kotlinx.android.synthetic.main.settings_activity_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.jetbrains.anko.configuration
import org.jetbrains.anko.ctx
import org.jetbrains.anko.intentFor
import java.io.Serializable
import java.util.*

class SettingsActivity : BaseActivity<SettingsPresenter>(), SettingsContract.View {


    companion object {

        private const val SAVED_STATE_ITEMS = "items"


        fun newInstance(context: Context): Intent {
            return context.intentFor<SettingsActivity>()
        }

    }


    /**
     * Items that this fragment's adapter contains.
     */
    private var mItems: MutableList<Any> = mutableListOf()

    /**
     * An adapter used by this fragment's recycler view.
     */
    private lateinit var mAdapter: SettingsRecyclerViewAdapter

    /**
     * A dialog used for asking a confirmation for signing out.
     */
    private var mSignOutDialog: MaterialDialog? = null

    /**
     * A dialog used for picking a specific decimal separator from the list.
     */
    private var mDecimalSeparatorsDialog: MaterialDialog? = null

    /**
     * A dialog used for asking a confirmation for settings restoration.
     */
    private var mRestoreDefaultsDialog: MaterialDialog? = null

    /**
     * A dialog used for showing a device specific metrics.
     */
    private var mDeviceMetricsDialog: DeviceMetricsDialog? = null




    override fun preInit() {
        super.preInit()

        overridePendingTransition(
            R.anim.horizontal_sliding_right_to_left_enter_animation,
            R.anim.default_window_exit_animation
        )
    }


    override fun initPresenter(): SettingsPresenter = SettingsPresenter(this)


    override fun init() {
        initToolbar()
        initRecyclerView()
    }


    private fun initToolbar() {
        toolbar.returnBackBtnIv.setOnClickListener { onBackPressed() }
        toolbar.titleTv.text = getString(R.string.settings)
    }


    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        mAdapter = SettingsRecyclerViewAdapter(this, mItems)
        mAdapter.mOnSettingItemClickListener = { _, item, _ ->
            mPresenter?.onSettingItemClicked(item)
        }
        mAdapter.mOnSwitchClickListener = { _, item, _, isChecked ->
            mPresenter?.onSettingSwitchClicked(item, isChecked)
        }

        recyclerView.adapter = mAdapter
    }


    override fun showProgressBar() {
        toolbar.progressBar.makeVisible()
    }


    override fun hideProgressBar() {
        toolbar.progressBar.makeGone()
    }


    override fun showSignOutConfirmationDialog() {
        val dialog = MaterialDialog.Builder(this)
            .title(R.string.settings_activity_sign_out_dialog_title_text)
            .titleColorRes(R.color.colorPrimaryText)
            .content(R.string.settings_activity_sign_out_dialog_text)
            .contentColorRes(R.color.colorPrimaryText)
            .positiveText(R.string.yes)
            .positiveColorRes(R.color.colorPrimaryText)
            .onPositive(signOutConfirmationDialogListener)
            .negativeText(R.string.no)
            .negativeColorRes(R.color.colorPrimaryText)
            .backgroundColorRes(R.color.colorPrimary)
            .build()

        mSignOutDialog = dialog
        mSignOutDialog?.show()
    }


    override fun hideSignOutConfirmationDialog() {
        mSignOutDialog?.dismiss()
    }


    override fun showDecimalSeparatorsDialog() {
        val dialog = MaterialDialog.Builder(this)
            .items(R.array.decimal_separators)
            .itemsColorRes(R.color.colorPrimaryText)
            .itemsCallback(decimalSeparatorsDialogListener)
            .backgroundColorRes(R.color.colorPrimary)
            .build()

        mDecimalSeparatorsDialog = dialog
        mDecimalSeparatorsDialog?.show()
    }


    override fun hideDecimalSeparatorsDialog() {
        mDecimalSeparatorsDialog?.dismiss()
    }


    override fun showRestoreDefaultsConfirmationDialog() {
        val dialog = MaterialDialog.Builder(this)
            .title(R.string.settings_activity_restore_defaults_dialog_title_text)
            .titleColorRes(R.color.colorPrimaryText)
            .content(R.string.settings_activity_restore_defaults_dialog_text)
            .contentColorRes(R.color.colorPrimaryText)
            .positiveText(R.string.yes)
            .positiveColorRes(R.color.colorPrimaryText)
            .onPositive(restoreDefaultsConfirmationDialogListener)
            .negativeText(R.string.no)
            .negativeColorRes(R.color.colorPrimaryText)
            .backgroundColorRes(R.color.colorPrimary)
            .build()

        mRestoreDefaultsDialog = dialog
        mRestoreDefaultsDialog?.show()
    }


    override fun hideRestoreDefaultsConfirmationDialog() {
        mRestoreDefaultsDialog?.dismiss()
    }


    override fun showDeviceMetricsDialog() {
        val dialog = DeviceMetricsDialog(this)
        val displayMetrics = resources.displayMetrics
        val density = displayMetrics.density

        dialog.setDeviceName("${Build.MODEL} (${Build.PRODUCT})")
        dialog.setDensity(density)
        dialog.setDensityInDp(displayMetrics.densityDpi)
        dialog.setScreenWidthInPx(displayMetrics.widthPixels)
        dialog.setScreenWidthInDp(displayMetrics.widthPixels / density)
        dialog.setScreenHeightInPx(displayMetrics.heightPixels)
        dialog.setScreenHeightInDp(displayMetrics.heightPixels / density)
        dialog.setSmallestWidthInDp(configuration.smallestScreenWidthDp)

        mDeviceMetricsDialog = dialog
        mDeviceMetricsDialog?.show()
    }


    override fun hideDeviceMetricsDialog() {
        mDeviceMetricsDialog?.dismiss()
    }


    override fun checkRingtonePermissions(): Boolean {
        return checkPermissions(
            REQUEST_CODE_RINGTONE_PERMISSION,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        )
    }


    override fun launchRingtonePickerActivity() {
        val intent = Intent()
        intent.action = RingtoneManager.ACTION_RINGTONE_PICKER
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.ringtones))
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)

        startActivityForResult(intent, REQUEST_CODE_RINGTONE_ACTIVITY)
    }


    override fun updateSettingWith(setting: Setting) {
        mAdapter.updateItemWith(setting, mAdapter.getSettingIndex(setting))
    }


    override fun finishActivity() {
        onBackPressed()
    }


    override fun setItems(items: MutableList<Any>) {
        mAdapter.setItems(items)
    }


    override fun isDataSetEmpty(): Boolean {
        return (mAdapter.itemCount == 0)
    }


    override fun getLocale(): Locale {
        return ctx.getLocale()
    }


    override fun getContentLayoutResourceId() = R.layout.settings_activity_layout


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(
            R.anim.default_window_enter_animation,
            R.anim.horizontal_sliding_right_to_left_exit_animation
        )
    }


    private val signOutConfirmationDialogListener = MaterialDialog.SingleButtonCallback { _, _ ->
        mPresenter?.onSignOutConfirmed()
    }


    private val decimalSeparatorsDialogListener = MaterialDialog.ListCallback { _, _, _, text ->
        mPresenter?.onDecimalSeparatorPicked(text.toString())
    }


    private val restoreDefaultsConfirmationDialogListener = MaterialDialog.SingleButtonCallback { _, _ ->
        mPresenter?.onRestoreDefaultsConfirmed()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(isPermissionSetGranted(grantResults)) {
            if(requestCode == REQUEST_CODE_RINGTONE_PERMISSION) {
                mPresenter?.onNotificationRingtoneItemClicked()
            }
        } else {
            showToast(getString(R.string.error_permissions_not_granted))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if((requestCode == REQUEST_CODE_RINGTONE_ACTIVITY) && (resultCode == Activity.RESULT_OK)) {
            val uri: Uri? = data!!.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)

            if(uri != null) {
                mPresenter?.onNotificationRingtonePicked(uri.toString())
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        if(savedState != null) {
            mItems = (savedState.getSerializable(SAVED_STATE_ITEMS) as MutableList<Any>)
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putSerializable(SAVED_STATE_ITEMS, (mItems as Serializable))
    }


}