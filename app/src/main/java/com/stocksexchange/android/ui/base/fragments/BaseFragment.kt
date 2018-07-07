package com.stocksexchange.android.ui.base.fragments

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.stocksexchange.android.ui.base.mvp.presenters.Presenter
import com.stocksexchange.android.ui.utils.interfaces.Selectable
import com.stocksexchange.android.ui.utils.listeners.OnBackPressListener
import org.jetbrains.anko.longToast
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.toast
import java.io.Serializable

/**
 * A fragment wrapper class used for holding common
 * functionality among all fragments.
 */
abstract class BaseFragment<P : Presenter> : Fragment(), Selectable, OnBackPressListener {


    companion object {

        private const val SAVED_STATE_STATE_BUNDLE = "saved_state"
        private const val SAVED_STATE_PRESENTER = "presenter"
        private const val SAVED_STATE_IS_SELECTED = "is_selected"

    }


    /**
     * A flag indicating whether this fragment has been initialized,
     * i.e. whether [onCreateView] method has finished running.
     */
    private var mIsInitialized: Boolean = false

    /**
     * A flag indicating whether this fragment is currently
     * selected (visible to the user) or not.
     */
    private var mIsSelected: Boolean = false

    /**
     * A presenter associated with this activity.
     */
    protected var mPresenter: P? = null

    /**
     * A view representing the fragment's UI.
     */
    protected lateinit var mRootView: View




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(getContentLayoutResourceId(), container, false)

        preInit()
        onRestoreState(savedInstanceState?.getBundle(SAVED_STATE_STATE_BUNDLE))
        init()
        postInit()

        mIsInitialized = true

        return mRootView
    }


    /**
     * Called right after [LayoutInflater.inflate] method is called.
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
        ctx.toast(message)
    }


    /**
     * Shows a toast with a length of [Toast.LENGTH_LONG] to the user.
     *
     * @param message The message for the toast
     */
    fun showLongToast(message: String) {
        ctx.longToast(message)
    }


    /**
     * Checks whether this fragment has been initialized,
     * i.e. whether [onCreateView] has finished running.
     *
     * @return true if initialization is done; false otherwise
     */
    protected fun isInitialized(): Boolean {
        return mIsInitialized
    }


    /**
     * Checks whether this fragment is currently selected
     * (visible to the user) or not.
     *
     * @return true if visible; false otherwise
     */
    protected fun isSelected(): Boolean = mIsSelected


    /**
     * Returns an ID of a layout that this fragment is associated with.
     *
     * @return The ID of the layout
     */
    @LayoutRes
    protected abstract fun getContentLayoutResourceId(): Int


    override fun onResume() {
        super.onResume()

        mPresenter?.start()
    }


    override fun onPause() {
        super.onPause()

        mPresenter?.stop()
    }


    @CallSuper
    override fun onSelected() {
        mIsSelected = true
    }


    @CallSuper
    override fun onUnselected() {
        mIsSelected = false
    }


    override fun onBackPressed() {
        // Stub
    }


    /**
     * Override this method if you need to restore state from fragment.
     */
    @CallSuper
    @Suppress("UNCHECKED_CAST")
    protected open fun onRestoreState(savedState: Bundle?) {
        if(savedState != null) {
            mPresenter?.onRestoreState(savedState.getSerializable(SAVED_STATE_PRESENTER) as MutableMap<String, Any>)

            mIsSelected = savedState.getBoolean(SAVED_STATE_IS_SELECTED, false)
        }
    }


    /**
     * Override this method if you need to save state in fragment.
     */
    @CallSuper
    protected open fun onSaveState(savedState: Bundle) {
        val presenterState: MutableMap<String, Any> = mutableMapOf()

        mPresenter?.onSaveState(presenterState)

        savedState.putSerializable(SAVED_STATE_PRESENTER, (presenterState as Serializable))
        savedState.putBoolean(SAVED_STATE_IS_SELECTED, mIsSelected)
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

        onRecycle(act.isChangingConfigurations)
    }


    /**
     * Override this method if you need to recycle stuff.
     *
     * @param isChangingConfigurations Whether the fragment
     * is destroyed only to be recreated thereafter because
     * of a configuration change or if it destroyed without
     * the intention to be recreated immediately thereafter
     */
    @CallSuper
    protected open fun onRecycle(isChangingConfigurations: Boolean) {
        // Stub
    }


}