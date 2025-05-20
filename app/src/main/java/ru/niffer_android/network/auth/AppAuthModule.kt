package ru.niffer_android.network.auth

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.browser.BrowserAllowList
import net.openid.appauth.browser.VersionedBrowserMatcher
import ru.niffer_android.BuildConfig
import ru.niffer_android.utils.HttpConnectionBuilder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppAuthModule {

    @Provides
    @Singleton
    fun provideAppAuthConfiguration(): AppAuthConfiguration {
        val appAuthConfig = AppAuthConfiguration.Builder()
            .setBrowserMatcher(
                BrowserAllowList(VersionedBrowserMatcher.CHROME_CUSTOM_TAB)
            )

        if (BuildConfig.FLAVOR == "local") {
            appAuthConfig
                .setSkipIssuerHttpsCheck(true)
                .setConnectionBuilder(HttpConnectionBuilder.INSTANCE)
        }

        return appAuthConfig.build()
    }
}