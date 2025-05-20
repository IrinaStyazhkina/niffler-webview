package ru.niffer_android.network.interceptor

import android.content.Context
import android.os.Handler
import android.os.Looper
import okhttp3.Interceptor
import okhttp3.Response
import ru.niffer_android.network.auth.SessionManager

class EndSessionInterceptor(
    private val context: Context,
    ): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code == 401) {
            Handler(Looper.getMainLooper()).post {
                SessionManager.handleUnauthorized(context)
            }
        }

        return response
    }
}