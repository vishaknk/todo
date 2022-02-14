package com.sample.todo.retrofit.interceptor

import android.content.Context
import android.net.ConnectivityManager
import com.sample.todo.utils.ApiException
import com.sample.todo.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class NetworkConnectionInterceptor(context: Context) : Interceptor {

    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)

        if (!isInternetAvailable())
            throw NoInternetException("No connection available")

        if (response.code() == 500)
            throw ApiException("Server error")

        return response
    }

    fun isInternetAvailable(): Boolean {

        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }

}