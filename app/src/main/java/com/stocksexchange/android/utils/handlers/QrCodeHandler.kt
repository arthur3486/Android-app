package com.stocksexchange.android.utils.handlers

/**
 * A handler used for parsing and verifying QR codes.
 */
class QrCodeHandler {


    companion object {

        private const val API_PUBLIC = "APIPublic"
        private const val API_SECRET = "APISecret"

    }




    /**
     * Parses a QR code.
     *
     * @param qrCode The QR code to parse
     *
     * @return The pair of string containing public and secret keys
     */
    fun parseQrCode(qrCode: String) : Pair<String, String>? {
        try {
            val hasPublic = qrCode.contains(API_PUBLIC)
            val hasSecret = qrCode.contains(API_SECRET)

            if(hasPublic && hasSecret) {
                val pairs = qrCode.split(", ")
                val public = pairs[0].split(":")[1].replace("\"", "")
                val secret = pairs[1].split(":")[1].replace("\"", "")

                return public to secret
            }
        } catch(exception: Exception) {
            // Do nothing
        }

        return null
    }


    /**
     * Checks whether a QR code is valid.
     *
     * @param qrCode The QR code to check
     *
     * @return true if valid; false otherwise
     */
    fun isQrCodeValid(qrCode: String) : Boolean {
        val keys = parseQrCode(qrCode)

        return if(keys != null) {
            val (public, secret) = keys

            public.isNotBlank() && secret.isNotBlank()
        } else {
            false
        }
    }


}