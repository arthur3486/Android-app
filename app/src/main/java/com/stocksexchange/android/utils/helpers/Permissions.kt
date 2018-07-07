package com.stocksexchange.android.utils.helpers

import android.content.pm.PackageManager

/**
 * Checks whether all permission requests have been granted or not.
 *
 * @param grantResults The integer array of permission grant results
 *
 * @return true if all permissions have been granted; false otherwise
 */
fun isPermissionSetGranted(grantResults: IntArray): Boolean {
    return grantResults.all { it == PackageManager.PERMISSION_GRANTED }
}