package com.stocksexchange.android.ui.base.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.widget.Toast
import com.stocksexchange.android.receivers.NetworkStateReceiver
import com.stocksexchange.android.ui.base.mvp.presenters.Presenter
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.io.Serializable

/**
 * An activity wrapper class used for holding common
 * functionality among all activities.
 */
abstract class BaseActivity<P : Presenter> : AppCompatActivity(), NetworkStateReceiver.Listener {


    companion object {

        private const val SAVED_STATE_STATE_BUNDLE = "saved_state"
        private const val SAVED_STATE_PRESENTER = "presenter"

    }




    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }


    /**
     * A flag indicating whether the network state is registered or not.
     */
    private var mIsNetworkStateReceiverRegistered: Boolean = false

    /**
     * A flag indicating whether this activity has been initialized
     * (i.e. whether [onCreate] method has finished running).
     */
    private var mIsInitialized: Boolean = false

    /**
     * A network state receiver registered locally in this activity.
     */
    private var mNetworkStateReceiver: NetworkStateReceiver? = null

    /**
     * A presenter associated with this activity.
     */
    protected var mPresenter: P? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentLayoutResourceId())
        preInit()
        onRestoreState(savedInstanceState?.getBundle(SAVED_STATE_STATE_BUNDLE))
        init()
        postInit()

        mIsInitialized = true
    }


    /**
     * Called right after [setContentView] method is called.
     * Can be useful for performing some tasks before views
     * initialization.
     */
    @CallSuper
    protected open fun preInit() {
        mPresenter = initPresenter()
    }


    /**
     * An abstract method used for initialization of the presenter.
     * Gets invoked from within [preInit].
     */
    protected abstract fun initPresenter(): P


    /**
     * Called right after [onRestoreState] method is called. Typically,
     * all views initialization should go here.
     */
    protected open fun init() {
        // Stub
    }


    /**
     * Called right after [init] method is called. Can be useful
     * for performing some tasks after view initialization.
     */
    protected open fun postInit() {
        // Stub
    }


    /**
     * Shows a toast with a length of [Toast.LENGTH_SHORT] to the user.
     *
     * @param message The message for the toast
     */
    fun showToast(message: String) {
        toast(message)
    }


    /**
     * Shows a toast with a length of [Toast.LENGTH_LONG] to the user.
     *
     * @param message The message for the toast
     */
    fun showLongToast(message: String) {
        longToast(message)
    }


    /**
     * Registers the network state receiver if [canObserveNetworkStateChanges]
     * method returns true.
     */
    private fun registerNetworkStateReceiver() {
        if(!canObserveNetworkStateChanges() && ((mNetworkStateReceiver != null) && mIsNetworkStateReceiverRegistered)) {
            return
        }

        mNetworkStateReceiver = NetworkStateReceiver(this)
        registerReceiver(mNetworkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        mIsNetworkStateReceiverRegistered = true
    }


    /**
     * Unregisters the network state receiver if [canObserveNetworkStateChanges]
     * method returns true.
     */
    private fun unregisterNetworkStateReceiver() {
        if (!mIsNetworkStateReceiverRegistered || (mNetworkStateReceiver == null)) {
            return
        }

        unregisterReceiver(mNetworkStateReceiver)

        mIsNetworkStateReceiverRegistered = false
    }


    /**
     * Override this method if your want to get notified about
     * network state changes.
     */
    protected open fun canObserveNetworkStateChanges(): Boolean {
        return false
    }


    /**
     * Returns an ID of a layout that this activity is associated with.
     *
     * @return The ID of the layout
     */
    @LayoutRes
    protected abstract fun getContentLayoutResourceId(): Int


    /**
     * Checks whether this activity has been initialized
     * (i.e. whether [onCreate] method has finished running).
     */
    protected fun isInitialized(): Boolean {
        return mIsInitialized
    }


    override fun onResume() {
        super.onResume()

        mPresenter?.start()
        registerNetworkStateReceiver()
    }


    override fun onPause() {
        super.onPause()

        mPresenter?.stop()
        unregisterNetworkStateReceiver()
    }


    /**
     * Override this method if you want to get notified
     * that the device has been connected to the network.
     */
    override fun onConnected() {
        // Stub
    }


    /**
     * Override this method if you want to get notified
     * that the device has been disconnected from the network.
     */
    override fun onDisconnected() {
        // Stub
    }


    /**
     * Override this method if you need to restore state from activity.
     */
    @CallSuper
    @Suppress("UNCHECKED_CAST")
    protected open fun onRestoreState(savedState: Bundle?) {
        if(savedState != null) {
            mPresenter?.onRestoreState(savedState.getSerializable(SAVED_STATE_PRESENTER) as MutableMap<String, Any>)
        }
    }


    /**
     * Override this method if you need to save state in activity.
     */
    @CallSuper
    protected open fun onSaveState(savedState: Bundle) {
        val presenterState: MutableMap<String, Any> = mutableMapOf()

        mPresenter?.onSaveState(presenterState)

        savedState.putSerializable(SAVED_STATE_PRESENTER, (presenterState as Serializable))
    }


    override fun onSaveInstanceState(outState: Bundle) {
        val savedState = Bundle()

        onSaveState(savedState)

        outState.putBundle(SAVED_STATE_STATE_BUNDLE, savedState)

        super.onSaveInstanceState(outState)
    }


    @CallSuper
    override fun onDestroy() {
        super.onDestroy()

        onRecycle(isChangingConfigurations)
    }


    /**
     * Override this method if you need to recycle stuff.
     *
     * @param isChangingConfigurations Whether the activity
     * is destroyed only to be recreated thereafter because
     * of a configuration change or if it destroyed without
     * the intention to be recreated immediately thereafter
     */
    @CallSuper
    protected open fun onRecycle(isChangingConfigurations: Boolean) {
        // Stub
    }


}