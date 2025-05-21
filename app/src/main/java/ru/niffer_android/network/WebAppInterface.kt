package ru.niffer_android.network

import android.util.Log
import android.webkit.JavascriptInterface
import ru.niffer_android.storage.TokenStorage

class WebAppInterface {
    @JavascriptInterface
    fun getToken(): String {
        Log.d("TOKEN", TokenStorage.idToken ?: "No token")
        return TokenStorage.idToken ?: ""
    }
}