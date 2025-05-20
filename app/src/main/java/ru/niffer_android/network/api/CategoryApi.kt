package ru.niffer_android.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import ru.niffer_android.model.Category
import ru.niffer_android.model.CategoryToCreate

interface CategoryApi {

    @GET("categories/all")
    suspend fun getCategories(@Query(value = "excludeArchived") excludeArchived: Boolean): List<Category>

    @PATCH("categories/update")
    suspend fun editCategory(@Body category: Category): Category

    @POST("categories/add")
    suspend fun createCategory(@Body categoryToCreate: CategoryToCreate): Category
}