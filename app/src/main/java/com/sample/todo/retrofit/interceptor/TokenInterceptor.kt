package com.sample.todo.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response

// Inject the authentication header.
private class TokenInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Token $token")
            .build()
        return chain.proceed(newRequest)
    }
}