package com.stocksexchange.android.ui.base.mvp.presenters

import android.support.annotation.CallSuper
import com.stocksexchange.android.ui.base.mvp.model.Model
import org.greenrobot.eventbus.EventBus
import org.koin.standalone.KoinComponent

/**
 * A base presenter to build presenters on.
 */
abstract class BasePresenter<out M, out V>(
    protected val mModel: M,
    protected val mView: V
) : Presenter, KoinComponent where
        M : Model,
        V : Any {


    /**
     * Registers the [EventBus] if [canReceiveEvents] returns true.
     */
    protected fun registerEventBusIfNecessary() {
        if(canReceiveEvents()) {
            EventBus.getDefault().register(this)
        }
    }


    /**
     * Unregisters the [EventBus] if [canReceiveEvents] returns true.
     */
    protected fun unregisterEventBusIfNecessary() {
        if(canReceiveEvents()) {
            EventBus.getDefault().unregister(this)
        }
    }


    override fun start() {
        mModel.start()
        registerEventBusIfNecessary()
    }


    override fun stop() {
        mModel.stop()
        unregisterEventBusIfNecessary()
    }


    /**
     * Should return whether the presenter can
     * can receive [EventBus] events or not.
     */
    protected open fun canReceiveEvents(): Boolean {
        return false
    }


    @CallSuper
    override fun onRestoreState(savedState: MutableMap<String, Any>) {
        mModel.onRestoreState(savedState)
    }


    @CallSuper
    override fun onSaveState(savedState: MutableMap<String, Any>) {
        mModel.onSaveState(savedState)
    }


    override fun toString(): String {
        return "${javaClass.simpleName}_${mModel.javaClass.simpleName}_${mView.javaClass.simpleName}"
    }


}