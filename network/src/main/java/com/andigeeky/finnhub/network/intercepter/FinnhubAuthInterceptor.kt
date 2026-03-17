package com.andigeeky.finnhub.network.intercepter

import okhttp3.Interceptor

class FinnhubAuthInterceptor(
    private val authToken: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain) =
        chain.proceed(
            chain.request()
                .newBuilder()
                .header(AUTH_TOKEN_HEADER_KEY, authToken)
                .build()
        )

    companion object {
        const val AUTH_TOKEN_HEADER_KEY = "X-Finnhub-Token"
    }
}