package ru.niffer_android.network.auth

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.niffer_android.storage.TokenStorage
import ru.niffer_android.ui.auth.StartActivity
import net.openid.appauth.AuthorizationService
import ru.niffer_android.activity.MainActivity
import androidx.core.content.edit

object SessionManager {

    fun logout(
        context: Context,
        authService: AuthorizationService,
        postLogoutCallback: (() -> Unit)? = null
    ) {
        val idToken = TokenStorage.idToken

        if (idToken.isNullOrBlank()) {
            Log.w("SessionManager", "No ID token found; skipping end session request")
            return
        }

        val logoutIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, StartActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val errorIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE,
        )

        val endSessionRequest = AppAuth.getLogoutRequest(idToken)

        authService.performEndSessionRequest(
            endSessionRequest,
            requireNotNull(logoutIntent),
            requireNotNull(errorIntent)
        )

        clearSession(context.applicationContext)
        postLogoutCallback?.invoke()
    }

    @Synchronized
    fun handleUnauthorized(context: Context) {
        val idToken = TokenStorage.idToken

        if (idToken.isNullOrBlank()) {
            Log.w("SessionManager", "No ID token found; skipping end session request")
            return
        }

        clearSession(context.applicationContext)

        val intent = Intent(context, StartActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        context.startActivity(intent)
    }

    private fun clearSession(context: Context) {
        Log.d("CLEAR S", context.applicationContext.toString())

        context.applicationContext.getSharedPreferences(AuthConfig.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit {
                clear()
            }
        TokenStorage.clearTokenStorage()
    }
}