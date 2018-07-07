package com.stocksexchange.android.utils

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode

/**
 * A factory for producing QR codes.
 */
class BarcodeTrackerFactory(private val detectedListener: (Barcode) -> Unit) : MultiProcessor.Factory<Barcode> {


    override fun create(p0: Barcode): Tracker<Barcode> {
        return object : Tracker<Barcode>() {

            override fun onNewItem(id: Int, barcode: Barcode) {
                detectedListener(barcode)
            }

        }
    }


}