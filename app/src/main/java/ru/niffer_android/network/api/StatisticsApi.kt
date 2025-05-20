package ru.niffer_android.network.api

import retrofit2.http.GET
import ru.niffer_android.model.Statistics

interface StatisticsApi {

    @GET("v2/stat/total")
    suspend fun getStatistics(): Statistics

}