package com.stocksexchange.android.ui.base.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.base.mvp.presenters.Presenter
import com.stocksexchange.android.ui.utils.KeyboardManager
import com.stocksexchange.android.ui.utils.extensions.*
import com.stocksexchange.android.ui.utils.listeners.QueryListener
import kotlinx.android.synthetic.main.search_toolbar_layout.*

/**
 * A base search activity to extend from.
 */
abstract class BaseSearchActivity<T : BaseFragment<*>, P : Presenter> : BaseFragmentActivity<T, P>() {


    companion object {

        private const val SAVED_STATE_IS_QUERY_INPUT_EMPTY = "is_query_input_empty"

        private const val KEYBOARD_SHOWING_DELAY = 150L

        private const val ANIMATION_DURATION = 100L

        private const val ANIMATION_TYPE_SHOW = 0
        private const val ANIMATION_TYPE_HIDE = 1

    }


    /**
     * A flag indicating whether the query edit text is empty or not.
     */
    private var mIsQueryInputEmpty: Boolean = true

    /**
     * A keyboard manager to control showing and hiding the keyboard
     * on the screen.
     */
    private lateinit var mKeyboardManager: KeyboardManager

    /**
     * A value animator to use for animating buttons on the search bar.
     */
    private var mValueAnimator: ValueAnimator? = null




    override fun preInit() {
        super.preInit()

        mKeyboardManager = KeyboardManager.newInstance(this)
    }


    override fun init() {
        super.init()

        initToolbar()
    }


    private fun initToolbar() {
        returnBackBtnIv.setOnClickListener { onBackPressed() }

        queryInputEt.hint = getInputHint()
        queryInputEt.inputType = getInputType()
        queryInputEt.setCursorDrawable(getColoredCompatDrawable(
            R.drawable.edit_text_cursor_drawable,
            R.color.colorPrimaryText
        ))
        queryInputEt.addTextChangedListener(QueryListener(queryListenerCallback))
        queryInputEt.setOnEditorActionListener(onEditorActionListener)

        clearInputBtnIv.visibility = (if (mIsQueryInputEmpty) View.GONE else View.VISIBLE)
        clearInputBtnIv.setOnClickListener(clearInputBtnClickListener)
    }


    private fun animateClearInputButton(animationType: Int) {
        mValueAnimator?.cancel()

        if(animationType == ANIMATION_TYPE_SHOW) {
            clearInputBtnIv.setScale(0f)
            clearInputBtnIv.makeVisible()

            mValueAnimator = ValueAnimator.ofFloat(0f, 1f)
        } else {
            clearInputBtnIv.setScale(1f)

            mValueAnimator = ValueAnimator.ofFloat(1f, 0f)
            mValueAnimator!!.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator?) {
                    clearInputBtnIv.makeGone()
                }

            })
        }

        with(mValueAnimator!!) {
            addUpdateListener { clearInputBtnIv.setScale(it.animatedValue as Float) }
            interpolator = LinearInterpolator()
            duration = ANIMATION_DURATION
            start()
        }
    }


    private fun showClearInputButton() {
        animateClearInputButton(ANIMATION_TYPE_SHOW)
    }


    private fun hideClearInputButton() {
        animateClearInputButton(ANIMATION_TYPE_HIDE)
    }


    private fun showKeyboard(shouldDelay: Boolean) {
        queryInputEt.requestFocus()
        queryInputEt.postDelayed(
            { mKeyboardManager.showKeyboard(queryInputEt) },
            (if(shouldDelay) KEYBOARD_SHOWING_DELAY else 0L)
        )
    }


    private fun hideKeyboard() {
        queryInputEt.clearFocus()
        mKeyboardManager.hideKeyboard(queryInputEt)
    }


    /**
     * Should perform a search on the passed in query.
     */
    abstract fun performSearch(query: String)


    /**
     * Should cancel the search.
     */
    abstract fun cancelSearch()


    private fun setSearchQuery(query: String) {
        queryInputEt.setText(query)
        queryInputEt.setSelection(query.length)
    }


    /**
     * Should return a hint for the EditText.
     */
    abstract fun getInputHint(): String


    /**
     * Should return an input type for the EditText.
     */
    abstract fun getInputType(): Int


    override fun onResume() {
        super.onResume()

        if(mIsQueryInputEmpty) {
            showKeyboard(true)
        } else {
            hideKeyboard()
        }
    }


    private val queryListenerCallback: QueryListener.Callback = object : QueryListener.Callback {

        override fun onQueryEntered(query: String) {
            if(mIsQueryInputEmpty) {
                showClearInputButton()
                mIsQueryInputEmpty = false
            }
        }

        override fun onQueryRemoved() {
            if(!mIsQueryInputEmpty) {
                hideClearInputButton()
                mIsQueryInputEmpty = true
            }
        }

    }


    private val onEditorActionListener: OnEditorActionListener = OnEditorActionListener { view, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard()

            val query = view.text.toString()

            if(query.isNotBlank()) {
                setSearchQuery(query)
                performSearch(query)
            }
        }

        true
    }


    private val clearInputBtnClickListener: OnClickListener = OnClickListener {
        cancelSearch()
        setSearchQuery("")
        showKeyboard(false)
    }


    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)

        if(savedState != null) {
            mIsQueryInputEmpty = savedState.getBoolean(SAVED_STATE_IS_QUERY_INPUT_EMPTY, true)
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.putBoolean(SAVED_STATE_IS_QUERY_INPUT_EMPTY, mIsQueryInputEmpty)
    }


    override fun onRecycle(isChangingConfigurations: Boolean) {
        super.onRecycle(isChangingConfigurations)

        mKeyboardManager.recycle()
        mValueAnimator?.end()
    }


}