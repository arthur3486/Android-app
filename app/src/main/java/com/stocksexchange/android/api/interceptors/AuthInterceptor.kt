package com.stocksexchange.android.api.interceptors

import com.stocksexchange.android.api.utils.generateSignature
import com.stocksexchange.android.utils.handlers.CredentialsHandler
import com.stocksexchange.android.model.HttpMethods
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Intercepts all the requests and applies the authentication if necessary.
 */
class AuthInterceptor(private val credentialsHandler: CredentialsHandler) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        return if(chain.request().method() == HttpMethods.POST.name) {
            val request = chain.request()
            val publicKey = credentialsHandler.getPublicKey().takeIf {
                it.isNotBlank()
            } ?: throw IllegalArgumentException("Empty public key for call: ${request.url()}.")
            val signature = credentialsHandler.getSecretKey().takeIf { it.isNotBlank() }?.let {
                generateSignature(it, request)
            } ?: throw IllegalArgumentException("Empty secret key for call: ${request.url()}.")

            val authenticatedRequest = request.newBuilder()
                .header("Key", publicKey)
                .header("Sign", signature)
                .build()

            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(chain.request())
        }
    }


}