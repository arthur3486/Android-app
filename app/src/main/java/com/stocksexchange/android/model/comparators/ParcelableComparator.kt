package com.stocksexchange.android.model.comparators

import android.os.Parcelable

/**
 * A wrapper around standalone [Comparator] with [Parcelable] support.
 */
interface ParcelableComparator<T> : Comparator<T>, Parcelable