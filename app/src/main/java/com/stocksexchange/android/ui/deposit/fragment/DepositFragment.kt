package com.stocksexchange.android.ui.deposit.fragment

import android.Manifest
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import com.google.zxing.EncodeHintType
import com.stocksexchange.android.R
import com.stocksexchange.android.REQUEST_CODE_STORAGE_PERMISSION
import com.stocksexchange.android.api.model.Deposit
import com.stocksexchange.android.model.Wallet
import com.stocksexchange.android.utils.providers.ConnectionProvider
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.android.utils.helpers.*
import com.stocksexchange.android.ui.base.fragments.BaseDataLoadingFragment
import com.stocksexchange.android.ui.utils.DoubleFormatter
import com.stocksexchange.android.ui.utils.extensions.*
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.ui.views.QrDialog
import kotlinx.android.synthetic.main.deposit_fragment_layout.view.*
import net.glxn.qrgen.android.QRCode
import net.glxn.qrgen.core.image.ImageType
import org.jetbrains.anko.dimen
import org.jetbrains.anko.support.v4.ctx
import org.koin.android.ext.android.get
import java.io.File

class DepositFragment : BaseDataLoadingFragment<DepositPresenter, Deposit>(), DepositContract.View {


    companion object {

        private const val SAVED_STATE_WALLET = "wallet"
        private const val SAVED_STATE_DEPOSIT = "deposit"


        fun newInstance(wallet: Wallet): DepositFragment {
            val fragment = DepositFragment()

            fragment.mWallet = wallet

            return fragment
        }

    }


    /**
     * A wallet holding the additional deposit information.
     */
    private lateinit var mWallet: Wallet

    /**
     * Main deposit information.
     */
    private var mDeposit: Deposit? = null

    /**
     * A QR code dialog.
     */
    private var mQrDialog: QrDialog? = null




    override fun initPresenter(): DepositPresenter = DepositPresenter(this)


    override fun init() {
        super.init()

        initCurrencyTextView()
        initMainContainer()
        initDetails()
    }


    private fun initCurrencyTextView() {
        mRootView.currencyTv.text = mWallet.currency.name
    }


    private fun initMainContainer() {
        if(!isDataSourceEmpty()) {
            addData(mDeposit!!)
        }
    }


    private fun initDetails() {
        val formatter = DoubleFormatter.getInstance(ctx.getLocale())

        val depositFeeArg = "${formatter.formatTransactionFee(mWallet.currency.depositFee)} ${mWallet.currency.depositFeeCurrency}"
        mRootView.depositFeeTv.text = getString(R.string.deposit_fragment_deposit_fee_template, depositFeeArg)

        val minDepositAmountArg = "${formatter.formatAmount(mWallet.currency.minimumDepositAmount)} ${mWallet.currency.name}"
        mRootView.minDepositAmountTv.text = getString(R.string.deposit_fragment_min_amount_template, minDepositAmountArg)

        mRootView.warningTv.text = getString(R.string.deposit_fragment_warning_template, mWallet.currency.name)
    }


    override fun adjustView(view: View) {
        when(view.id) {

            R.id.infoView -> {
                mRootView.infoView.setPaddingTop(0)
                mRootView.infoView.setPaddingBottom(ctx.dimen(R.dimen.deposit_fragment_info_view_vertical_padding))
            }

        }
    }


    override fun addData(data: Deposit) {
        mDeposit = data

        if(data.hasAddress()) {
            val address = data.address

            mRootView.depositAddressOb.setSubtitleText(address.truncate(25))
            mRootView.depositAddressOb.setOnClickListener {
                mPresenter?.onDepositAddressClicked(address)
            }
            mRootView.depositAddressOb.setOnLongClickListener {
                mPresenter?.onDepositAddressLongClicked(address)
                true
            }
        } else {
            mRootView.depositAddressOb.setSubtitleText(getString(R.string.error_not_available))
        }

        if(data.hasPublicKey()) {
            val publicKey = data.publicKey

            mRootView.publicKeyOb.setSubtitleText(publicKey.truncate(25))
            mRootView.publicKeyOb.setOnClickListener {
                mPresenter?.onPublicKeyClicked(publicKey)
            }
            mRootView.publicKeyOb.setOnLongClickListener {
                mPresenter?.onPublicKeyLongClicked(publicKey)
                true
            }
        } else {
            mRootView.publicKeyOb.setSubtitleText(getString(R.string.error_not_available))
        }

        if(data.hasPaymentId()) {
            val paymentId = data.paymentId

            mRootView.paymentIdOb.setSubtitleText(paymentId.truncate(25))
            mRootView.paymentIdOb.setOnClickListener {
                mPresenter?.onPaymentIdClicked(paymentId)
            }
            mRootView.paymentIdOb.setOnLongClickListener {
                mPresenter?.onPaymentIdLongClicked(paymentId)
                true
            }
        } else {
            mRootView.paymentIdOb.setSubtitleText(getString(R.string.error_not_available))
        }
    }


    override fun showQrDialog(hash: String, titleResId: Int) {
        val dialog = QrDialog(ctx)
        val qrCodeSize = ctx.dimen(R.dimen.qr_dialog_qr_code_iv_size)

        dialog.setTitle(String.format("%s %s", mWallet.currency.name, getString(titleResId)))
        dialog.setHash(hash)
        dialog.setQrImage(QRCode.from(hash).withSize(qrCodeSize, qrCodeSize).withHint(EncodeHintType.MARGIN, 0).bitmap())
        dialog.setCopyHashButtonClickListener { mPresenter?.onCopyHashButtonClicked(hash) }
        dialog.setSaveImageButtonClickListener { onSaveQrImageButtonClicked() }

        mQrDialog = dialog
        mQrDialog!!.show()
    }


    override fun dismissQrDialog() {
        mQrDialog?.dismiss()
    }


    override fun downloadQrCodeImage() {
        val title = mQrDialog!!.getTitle().replace(" ", "_").toLowerCase()
        val hash = mQrDialog!!.getHash()

        val outputFile = File(
            createParentDirectory("${Environment.DIRECTORY_PICTURES}/${getString(R.string.app_name)}"),
            composeFileName(
                FILE_NAME_PREFIX_IMAGE,
                "${title}_qr_code",
                System.currentTimeMillis().toString(),
                FILE_NAME_EXTENSION_JPG
            )
        )

        QRCode.from(hash)
            .withSize(512, 512)
            .withHint(EncodeHintType.MARGIN, 0)
            .to(ImageType.JPG)
            .writeTo(outputFile.outputStream())

        super.showToast(getString(R.string.qr_code_image_downloaded))
    }


    override fun getCurrency(): String {
        return mWallet.currency.name
    }


    override fun isDataSourceEmpty(): Boolean {
        return (mDeposit == null)
    }


    override fun getInfoViewIcon(iconProvider: InfoViewIconProvider): Drawable? {
        return iconProvider.getDepositIcon()
    }


    override fun getEmptyViewCaption(): String {
        return getString(R.string.error_no_data_available)
    }


    override fun getMainView(): View = mRootView.optionButtonsContainerLl


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getContentLayoutResourceId(): Int = R.layout.deposit_fragment_layout


    override fun getDeposit(): Deposit? {
        return mDeposit
    }


    private fun onSaveQrImageButtonClicked() {
        if(!get<ConnectionProvider>().isNetworkAvailable()) {
            showToast(getString(R.string.error_check_network_connection))
            return
        }

        if(!checkPermissions(REQUEST_CODE_STORAGE_PERMISSION, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            return
        }

        mPresenter?.onSaveQrCodeImageButtonClicked()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if(isPermissionSetGranted(grantResults)) {
            if(requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
                onSaveQrImageButtonClicked()
            }
        } else {
            showToast(getString(R.string.error_permissions_not_granted))
        }
    }


    override fun onRestoreState(savedState: Bundle?) {
        if(savedState != null) {
            mWallet = (savedState.getSerializable(SAVED_STATE_WALLET) as Wallet)
            mDeposit = savedState.getParcelable(SAVED_STATE_DEPOSIT)
        }

        super.onRestoreState(savedState)
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putSerializable(SAVED_STATE_WALLET, mWallet)
        savedState.putParcelable(SAVED_STATE_DEPOSIT, mDeposit)
    }


}