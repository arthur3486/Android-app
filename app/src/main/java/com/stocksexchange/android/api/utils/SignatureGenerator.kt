package com.stocksexchange.android.api.utils

import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Generates a signature for the authenticated request.
 *
 * @param secretKey The secret key to sign the signature with
 *
 * @return The generated signature
 */
fun generateSignature(secretKey: String, request: Request): String {
    return hash(secretKey, getRequestBodyString(request))
}


/**
 * Generates a request body string representation for the request.
 * Applies only to POST requests.
 *
 * @param request The request to generate the request body string of
 *
 * @return The request body string in a key-value form
 * (e.g., key1=value1&key2=value2).
 */
fun getRequestBodyString(request: Request): String {
    return request.body()
        ?.takeIf { it is FormBody }
        ?.let { body: RequestBody ->
            body as FormBody

            val str = StringBuilder()

            for (i in 0 until body.size()) {
                if (i > 0) {
                    str.append("&")
                }

                str.append(body.encodedName(i))
                str.append("=")
                str.append(body.encodedValue(i))
            }

            str.toString()
        } ?: ""
}


/**
 * Generates a new nonce (random string).
 *
 * @return The nonce used for request signing
 */
fun generateNonce(): String {
    val randomizer = Random()
    val min = 1
    val max = 9
    val limit = (max - min + 1)
    var nonce = ""

    for(i in min..max) {
        nonce = "$nonce${randomizer.nextInt(limit) + min}"
    }

    return nonce
}


/**
 * Hashes the request body with HMAC-SHA512 hash with a secret key.
 *
 * @param secretKey The secret key to hash with
 * @param requestBody The body to hash
 *
 * @return The HMAC-SHA512 hash of the request body
 */
private fun hash(secretKey: String, requestBody: String): String {
    return try {
        val algorithm = "HmacSHA512"
        val charset = "UTF-8"

        val secretKeySpec = SecretKeySpec(
            secretKey.toByteArray(charset(charset)),
            algorithm
        )

        val mac = Mac.getInstance(algorithm)
        mac.init(secretKeySpec)

        val digest = mac.doFinal(requestBody.toByteArray(charset(charset)))

        return toHexString(digest)

    } catch(exception: Exception) {
        ""
    }
}


/**
 * Converts bytes to a hexadecimal string representation.
 *
 * @param bytes The bytes to convert
 *
 * @return The hexadecimal string of the bytes
 */
private fun toHexString(bytes: ByteArray): String {
    val formatter = Formatter()

    for(b in bytes) {
        formatter.format("%02x", b)
    }

    return formatter.toString()
}