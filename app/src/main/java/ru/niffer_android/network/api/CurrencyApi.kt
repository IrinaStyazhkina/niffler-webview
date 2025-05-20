package ru.niffer_android.network.api

import retrofit2.http.GET
import ru.niffer_android.model.Currency

interface CurrencyApi {
    @GET("currencies/all")
    suspend fun getCurrencies(): List<Currency>
}