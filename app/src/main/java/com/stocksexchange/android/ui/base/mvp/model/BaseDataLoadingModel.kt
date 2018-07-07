package com.stocksexchange.android.ui.base.mvp.model

import com.stocksexchange.android.MIN_DATA_REFRESHING_INTERVAL
import com.stocksexchange.android.model.DataLoadingStates
import com.stocksexchange.android.model.DataTypes
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.utils.helpers.tag
import com.stocksexchange.android.ui.base.mvp.model.BaseDataLoadingModel.BaseDataLoadingActionListener
import com.stocksexchange.android.ui.utils.extensions.getWithDefault
import com.stocksexchange.android.ui.utils.interfaces.DataLoadable
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * A base data loading model to build model classes on.
 */
abstract class BaseDataLoadingModel<
    Data : Any,
    in Parameters : Any,
    ActionListener: BaseDataLoadingActionListener<Data>
> : BaseModel() {


    companion object {

        private val CLASS = BaseDataLoadingModel::class.java

        private val SAVED_STATE_WAS_LAST_DATA_FETCHING_SUCCESSFUL = tag(CLASS, "was_last_data_fetching_successful")
        private val SAVED_STATE_WAS_LAST_DATA_FETCHING_INITIATED_BY_USER = tag(CLASS, "was_last_data_fetching_initiated_by_user")
        private val SAVED_STATE_LAST_OLD_DATA_FETCHING_TIME = tag(CLASS, "last_old_data_fetching_time")
        private val SAVED_STATE_LAST_NEW_DATA_FETCHING_TIME = tag(CLASS, "last_new_data_fetching_time")
        private val SAVED_STATE_LAST_DATA_TYPE = tag(CLASS, "last_data_type")

    }


    /**
     * A flag indicating whether the last data loading has been
     * successfully performed or not.
     */
    var mWasLastDataFetchingSuccessful: Boolean = false
        private set

    /**
     * A flag indicating whether the last data loading has been
     * initiated by the user or not. Helpful in determining what
     * progress bar to hide (standard or refresh one), etc.
     */
    var mWasLastDataFetchingInitiatedByTheUser: Boolean = false
        private set

    /**
     * A flag indicating whether the data is currently being
     * loaded or not.
     */
    var mIsDataLoading: Boolean = false
        private set

    /**
     * A flag indicating whether the data loading has been
     * cancelled or not.
     */
    var mIsDataLoadingCancelled: Boolean = false
        private set

    /**
     * A timestamp holding the time in milliseconds of the last
     * loading of old data.
     */
    protected var mLastOldDataFetchingTime: Long = 0L

    /**
     * A timestamp holding the time in milliseconds of the last
     * loading of new data.
     */
    protected var mLastNewDataFetchingTime: Long = 0L

    /**
     * Contains the data type the last data loading was based on.
     */
    protected var mLastDataType: DataTypes = DataTypes.INVALID

    /**
     * A job responsible for loading the data. Needed in case
     * the data loading is cancelled.
     */
    protected var mDataLoadingJob: Job? = null

    /**
     * A listener to report events to a presenter.
     */
    protected var mActionListener: ActionListener? = null




    override fun stop() {
        super.stop()

        cancelDataLoading()
    }


    /**
     * Cancels the data loading (if one is being performed).
     */
    open fun cancelDataLoading() {
        if(mDataLoadingJob?.cancel() == true) {
            mIsDataLoadingCancelled = true

            // Ending the data loading to avoid a corrupt view state
            onDataLoadingEnded()
        }
    }


    /**
     * Updates the right timestamp based on the last data type.
     */
    protected open fun updateDataFetchingTimestamp() {
        if((mLastOldDataFetchingTime == 0L) && (mLastNewDataFetchingTime == 0L)) {
            val timestamp = System.currentTimeMillis()

            mLastOldDataFetchingTime = timestamp
            mLastNewDataFetchingTime = timestamp

            return
        }

        if(mLastDataType == DataTypes.OLD_DATA) {
            mLastOldDataFetchingTime = System.currentTimeMillis()
        } else {
            mLastNewDataFetchingTime = System.currentTimeMillis()
        }
    }


    /**
     * Resets necessary parameters for data reloading.
     */
    open fun resetParameters() {
        mWasLastDataFetchingSuccessful = false
        mWasLastDataFetchingInitiatedByTheUser = false
        mLastOldDataFetchingTime = 0L
        mLastNewDataFetchingTime = 0L
    }


    /**
     * Gets called in case the data loading has been successful.
     */
    open fun handleSuccessfulResponse(data: Data) {
        mWasLastDataFetchingSuccessful = true
        updateDataFetchingTimestamp()

        mActionListener?.onDataLoadingSucceeded(data)
        onDataLoadingEnded()
    }


    /**
     * Gets called in case the data loading has been unsuccessful.
     */
    open fun handleUnsuccessfulResponse(error: Throwable) {
        updateDataFetchingTimestamp()

        onDataLoadingEnded()
        mActionListener?.onDataLoadingFailed(error)
    }


    protected open fun onDataLoadingStarted() {
        mIsDataLoading = true

        mActionListener?.onDataLoadingStarted()
        mActionListener?.onDataLoadingStateChanged(DataLoadingStates.ACTIVE)
    }


    protected open fun onDataLoadingEnded() {
        mIsDataLoading = false

        mActionListener?.onDataLoadingEnded()
        mActionListener?.onDataLoadingStateChanged(DataLoadingStates.IDLE)
    }


    /**
     * Sets an action listener to speak with a presenter.
     *
     * @param actionListener The listener to set
     */
    open fun setActionListener(actionListener: ActionListener) {
        mActionListener = actionListener
    }


    /**
     * Checks whether the data loading interval is applied for
     * the last data type.
     */
    fun isDataLoadingIntervalApplied(): Boolean {
        val lastFetchingTime = (if(mLastDataType == DataTypes.OLD_DATA) mLastOldDataFetchingTime else mLastNewDataFetchingTime)
        return ((System.currentTimeMillis() - (lastFetchingTime)) > MIN_DATA_REFRESHING_INTERVAL)
    }


    /**
     * Gets the data by changing the particular flags and returns
     * immediately if the data loading cannot be performed for some
     * reason (specifically, if [canLoadData] returns false).
     */
    fun getData(params: Parameters, dataType: DataTypes, wasInitiatedByTheUser: Boolean) {
        // Altering the states
        mWasLastDataFetchingInitiatedByTheUser = wasInitiatedByTheUser
        mIsDataLoadingCancelled = false
        mLastDataType = dataType

        if(!canLoadData(params, dataType)) {
            onDataLoadingEnded()
            return
        }

        if(dataType == DataTypes.NEW_DATA) {
            refreshData()
        }

        mDataLoadingJob = getDataAsync(params)
        onDataLoadingStarted()
    }


    /**
     * Override this method to check for specific conditions
     * under which the data loading cannot be performed.
     */
    open fun canLoadData(params: Parameters, dataType: DataTypes): Boolean {
        val isNewDataWithIntervalNotApplied = ((dataType == DataTypes.NEW_DATA) && !isDataLoadingIntervalApplied())
        return !isNewDataWithIntervalNotApplied
    }


    /**
     * Gets called whenever data needs to refreshed.
     */
    abstract fun refreshData()


    /**
     * Should return a coroutine job where the data loading
     * itself is performed.
     */
    protected open fun getDataAsync(params: Parameters): Job {
        return launch(UI) {
            val result = getRepositoryResult(params)

            if(result.isSuccessful()) {
                handleSuccessfulResponse(result.getSuccessfulResult().value)
            } else {
                handleUnsuccessfulResponse(result.getErroneousResult().exception)
            }
        }
    }


    /**
     * Should return a repository result by fetching from
     * a specific repository. Gets called inside [getDataAsync].
     */
    abstract suspend fun getRepositoryResult(params: Parameters): RepositoryResult<Data>


    override fun onRestoreState(savedState: MutableMap<String, Any>) {
        with(savedState) {
            mWasLastDataFetchingSuccessful = (getWithDefault(SAVED_STATE_WAS_LAST_DATA_FETCHING_SUCCESSFUL, false) as Boolean)
            mWasLastDataFetchingInitiatedByTheUser = (getWithDefault(SAVED_STATE_WAS_LAST_DATA_FETCHING_INITIATED_BY_USER, false) as Boolean)
            mLastOldDataFetchingTime = (getWithDefault(SAVED_STATE_LAST_OLD_DATA_FETCHING_TIME, 0L) as Long)
            mLastNewDataFetchingTime = (getWithDefault(SAVED_STATE_LAST_NEW_DATA_FETCHING_TIME, 0L) as Long)
            mLastDataType = (getWithDefault(SAVED_STATE_LAST_DATA_TYPE, DataTypes.INVALID) as DataTypes)
        }
    }


    override fun onSaveState(savedState: MutableMap<String, Any>) {
        savedState[SAVED_STATE_WAS_LAST_DATA_FETCHING_SUCCESSFUL] = mWasLastDataFetchingSuccessful
        savedState[SAVED_STATE_WAS_LAST_DATA_FETCHING_INITIATED_BY_USER] = mWasLastDataFetchingInitiatedByTheUser
        savedState[SAVED_STATE_LAST_OLD_DATA_FETCHING_TIME] = mLastOldDataFetchingTime
        savedState[SAVED_STATE_LAST_NEW_DATA_FETCHING_TIME] = mLastNewDataFetchingTime
        savedState[SAVED_STATE_LAST_DATA_TYPE] = mLastDataType
    }


    /**
     * A base listener to speak with presenter for data loading
     * related stuff.
     */
    interface BaseDataLoadingActionListener<in Data> : DataLoadable<Data>


}