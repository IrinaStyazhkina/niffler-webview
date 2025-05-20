package ru.niffer_android.network.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.Spend
import ru.niffer_android.model.SpendingToCreate

interface SpendingApi {

    @GET("v3/spends/all")
    suspend fun getSpends(
        @Query(value = "page") page: Int? = 0,
        @Query(value = "searchQuery") query: String? = "",
        @Query(value = "filterPeriod") period: String? = "",
        @Query(value = "filterCurrency") currency: String? = "",
    ): PagedModel<Spend>

    @POST("spends/add")
    suspend fun createSpend(@Body spendingToCreate: SpendingToCreate): Spend

    @PATCH("spends/edit")
    suspend fun editSpend(@Body spend: Spend): Spend

    @DELETE("spends/remove")
    suspend fun deleteSpends(@Query("ids") spendIds: List<String>)
}