package com.stocksexchange.android.receivers

import android.content.BroadcastReceiver
import org.koin.standalone.KoinComponent

/**
 * A base class for receivers with the support for dependency injection.
 */
abstract class BaseBroadcastReceiver : BroadcastReceiver(), KoinComponent