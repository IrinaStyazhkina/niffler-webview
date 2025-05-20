package ru.niffer_android.network.auth

import ru.niffer_android.BuildConfig


object AuthConfig {
        const val AUTH_STATE = "AUTH_STATE"
        private const val AUTH_URL = BuildConfig.AUTH_URL

        const val SHARED_PREFERENCES_NAME = "niffler_preferences"
        const val AUTH_URI = "$AUTH_URL/oauth2/authorize"
        const val TOKEN_URI = "$AUTH_URL/oauth2/token"
        const val END_SESSION_URI = "$AUTH_URL/connect/logout"
        const val SCOPE = "openid"
        const val CLIENT_ID = "mobile-client"
        const val CALLBACK_URL = "app://ru.niffler_android/callback"
        const val LOGOUT_CALLBACK_URL = "app://ru.niffler_android/logout_callback"
}