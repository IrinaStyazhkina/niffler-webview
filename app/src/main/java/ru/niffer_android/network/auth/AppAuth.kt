package ru.niffer_android.network.auth

import android.net.Uri
import androidx.core.net.toUri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenResponse
import kotlin.coroutines.suspendCoroutine


object AppAuth {

    private val authServiceConfig: AuthorizationServiceConfiguration =
        AuthorizationServiceConfiguration(
            Uri.parse(AuthConfig.AUTH_URI),
            Uri.parse(AuthConfig.TOKEN_URI),
            null,
            Uri.parse(AuthConfig.END_SESSION_URI)
        )

    fun getAuthRequest(): AuthorizationRequest {
        return AuthorizationRequest.Builder(
            authServiceConfig,
            AuthConfig.CLIENT_ID,
            ResponseTypeValues.CODE,
            AuthConfig.CALLBACK_URL.toUri()
        )
            .setScopes(
                AuthConfig.SCOPE
            )
            .setPrompt("login")
            .build()
    }

    fun getLogoutRequest(idToken: String?): EndSessionRequest{
        return EndSessionRequest.Builder(authServiceConfig)
            .setIdTokenHint(idToken)
            .setPostLogoutRedirectUri(AuthConfig.LOGOUT_CALLBACK_URL.toUri())
            .build()
    }

    suspend fun performTokenRequestSuspend(
        authorizationService: AuthorizationService,
        tokenExchangeRequest: TokenRequest,
    ): TokenResponse {
        return suspendCoroutine { continuation ->
            authorizationService.performTokenRequest(tokenExchangeRequest) { response, exception ->
                when {
                    response != null -> {
                        continuation.resumeWith(Result.success(response))
                    }

                    exception != null -> {
                        continuation.resumeWith(Result.failure(exception))
                    }

                    else -> error("unreachable")
                }

            }
        }
    }
}