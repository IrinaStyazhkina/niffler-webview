package ru.niffer_android.application

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import ru.niffer_android.network.auth.AuthorizationServiceManager
import javax.inject.Inject

@HiltAndroidApp
class NifflerApp: Application() , LifecycleObserver {

    @Inject
    lateinit var authServiceManager: AuthorizationServiceManager

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(authServiceManager)
    }
}