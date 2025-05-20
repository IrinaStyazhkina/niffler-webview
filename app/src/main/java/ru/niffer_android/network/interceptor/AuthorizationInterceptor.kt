package ru.niffer_android.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.niffer_android.storage.TokenStorage

class AuthorizationInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .addTokenHeader()
            .let { chain.proceed(it) }
    }

    private fun Request.addTokenHeader(): Request {
        val authHeaderName = "Authorization"
        return newBuilder()
            .apply {
                val token = TokenStorage.idToken
                if (token.isNullOrBlank()) {
                    Log.w("AuthorizationInterceptor", "No token found")
                } else {
                    header(authHeaderName, token.withBearer())
                }
            }
            .build()
    }

    private fun String.withBearer() = "Bearer $this"
}