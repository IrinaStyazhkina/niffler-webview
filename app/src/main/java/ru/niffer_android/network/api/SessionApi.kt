package ru.niffer_android.network.api

import retrofit2.Response
import retrofit2.http.GET
import ru.niffer_android.model.Session

interface SessionApi {

    @GET("session/current")
    suspend fun getCurrentSession(): Response<Session>
}