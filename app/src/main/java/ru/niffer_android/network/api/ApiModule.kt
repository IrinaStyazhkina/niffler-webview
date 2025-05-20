package ru.niffer_android.network.api

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.niffer_android.BuildConfig
import ru.niffer_android.network.interceptor.AuthorizationInterceptor
import ru.niffer_android.network.interceptor.EndSessionInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if(BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        @ApplicationContext context: Context,
        logging: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(AuthorizationInterceptor())
        .addInterceptor(EndSessionInterceptor(context))
        .addInterceptor(logging)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSessionApiService(retrofit: Retrofit): SessionApi = retrofit.create(
        SessionApi::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideCategoryApiService(retrofit: Retrofit): CategoryApi =
        retrofit.create(CategoryApi::class.java)

    @Provides
    @Singleton
    fun provideSpendingApiService(retrofit: Retrofit): SpendingApi =
        retrofit.create(SpendingApi::class.java)

    @Provides
    @Singleton
    fun provideStatisticsApiService(retrofit: Retrofit): StatisticsApi =
        retrofit.create(StatisticsApi::class.java)

    @Provides
    @Singleton
    fun provideCurrencyApiService(retrofit: Retrofit): CurrencyApi =
        retrofit.create(CurrencyApi::class.java)

    @Provides
    @Singleton
    fun provideInvitationApiService(retrofit: Retrofit): InvitationApi =
        retrofit.create(InvitationApi::class.java)

    @Provides
    @Singleton
    fun provideFriendApiService(retrofit: Retrofit): FriendApi =
        retrofit.create(FriendApi::class.java)

}