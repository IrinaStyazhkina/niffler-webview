package ru.niffer_android.network.auth

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizationServiceManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val config: AppAuthConfiguration
) : DefaultLifecycleObserver {

    private var _service: AuthorizationService? = null
    val service: AuthorizationService
        get() {
            if (_service == null) {
                _service = AuthorizationService(context, config)
            }
            return _service!!
        }

    override fun onDestroy(owner: LifecycleOwner) {
        _service?.dispose()
        _service = null

    }
}