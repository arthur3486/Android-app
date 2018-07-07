package com.stocksexchange.android.ui.base.activities

import android.support.annotation.CallSuper
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.base.mvp.presenters.Presenter

/**
 * A base activity to extend from if the activity hosts the fragment.
 */
abstract class BaseFragmentActivity<T : BaseFragment<*>, P : Presenter> : BaseActivity<P>() {


    /**
     * A fragment that this activity holds.
     */
    protected lateinit var mFragment: T




    @CallSuper
    override fun init() {
        initFragment()
    }


    /**
     * Initializes and puts a fragment inside the fragment manager.
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun initFragment() {
        val foundFragment = supportFragmentManager.findFragmentById(R.id.fragmentHolderFl)

        mFragment = if(foundFragment == null) {
            getActivityFragment()
        } else {
            (foundFragment as T)
        }

        mFragment.onSelected()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentHolderFl, mFragment)
            .commit()
    }


    /**
     * Should return a fragment that this activity will hold.
     */
    abstract fun getActivityFragment(): T


    override fun onBackPressed() {
        super.onBackPressed()

        mFragment.onBackPressed()
    }


}