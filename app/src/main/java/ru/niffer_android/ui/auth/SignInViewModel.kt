package ru.niffer_android.ui.auth

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import org.json.JSONException
import ru.niffer_android.model.AuthLoadingState
import ru.niffer_android.network.auth.AppAuth
import ru.niffer_android.network.auth.AuthConfig
import ru.niffer_android.network.auth.AuthorizationServiceManager
import ru.niffer_android.network.auth.SessionManager
import ru.niffer_android.repository.session.SessionRepository
import ru.niffer_android.storage.TokenStorage
import javax.inject.Inject
import androidx.core.content.edit

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val application: Application,
    private val authorizationServiceManager: AuthorizationServiceManager
) : AndroidViewModel(application) {

    private var authState = MutableLiveData(AuthState())
    private var authLoadingState =
        MutableLiveData(AuthLoadingState(isLoading = false, isSuccessful = false))

    val authLoadingLiveData: LiveData<AuthLoadingState> = authLoadingState
    val authStateLiveData: LiveData<AuthState> = authState

    fun setAuthState(response: AuthorizationResponse?, exception: AuthorizationException?) {
        this.authState.value = if (response == null && exception == null) {
            AuthState()
        } else {
            AuthState(response, exception)
        }
    }

    fun exchangeAuthorizationCode(authorizationResponse: AuthorizationResponse, context: Context) {
        val tokenExchangeRequest = authorizationResponse.createTokenExchangeRequest()
        viewModelScope.launch {
            runCatching {
                authLoadingState.postValue(AuthLoadingState(isLoading = true, isSuccessful = false))
                AppAuth.performTokenRequestSuspend(authorizationServiceManager.service, tokenExchangeRequest)
            }
                .onSuccess { res ->
                    TokenStorage.accessToken = res.accessToken
                    TokenStorage.refreshToken = res.refreshToken
                    TokenStorage.idToken = res.idToken
                    val session = sessionRepository.getCurrentSession()
                    if (session?.username != null) {
                        authState.value?.update(res, null)
                        authLoadingState.postValue(
                            AuthLoadingState(
                                isLoading = false,
                                isSuccessful = true
                            )
                        )
                    } else {
                        authState.postValue(AuthState())
                        TokenStorage.clearTokenStorage()
                        authLoadingState.postValue(
                            AuthLoadingState(
                                isLoading = false,
                                isSuccessful = false
                            )
                        )
                    }
                    persistState()
                }
                .onFailure {
                    authState.postValue(AuthState())
                    TokenStorage.clearTokenStorage()
                }
        }
    }

    fun startAuthorization(): Intent {
        val request = AppAuth.getAuthRequest()
        return authorizationServiceManager.service.getAuthorizationRequestIntent(request)
    }

    private fun persistState() {
        application.getSharedPreferences(AuthConfig.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(AuthConfig.AUTH_STATE, authState.value?.jsonSerializeString())
            }
    }

    fun restoreState() {
        //2025-05-27 10:09:47.262  ru.niffer_android.application.NifflerApp@e6d8e4f

        Log.d("APP DATA", application.applicationContext.toString())
        val jsonString = application
            .getSharedPreferences(AuthConfig.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getString(AuthConfig.AUTH_STATE, null)

        if (jsonString != null && !TextUtils.isEmpty(jsonString)) {
            try {
                authState.value = AuthState.jsonDeserialize(jsonString)

                if (!TextUtils.isEmpty(authState.value?.idToken)) {
                    TokenStorage.idToken = authState.value?.idToken

                    Log.d("RESTORE STATE FIRST", TokenStorage.idToken ?: "")

                    viewModelScope.launch {
                        val session = sessionRepository.getCurrentSession()
                        if (session?.username != null) {
                            authLoadingState.postValue(
                                AuthLoadingState(
                                    isLoading = false,
                                    isSuccessful = true
                                )
                            )
                        } else {
                            authLoadingState.postValue(
                                AuthLoadingState(
                                    isLoading = false,
                                    isSuccessful = false
                                )
                            )
                            authState.postValue(AuthState())
                            TokenStorage.clearTokenStorage()
                            persistState()
                        }
                    }
                }
            } catch (jsonException: JSONException) {
                jsonException.printStackTrace()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            SessionManager.logout(application, authorizationServiceManager.service) {
                authState.postValue(AuthState())
            }
        }
    }
}