package com.miramir.mahfaze.util

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
            chain.proceed(chain.request()
                    .newBuilder()
                    .header("Authorization", token)
                    .build())
}