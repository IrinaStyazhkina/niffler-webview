package ru.niffer_android.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.User


interface UserApi {

    @GET("users/current")
    suspend fun getCurrentUser(): User

    @GET("v3/users/all?&size=20")
    suspend fun getAllPeople(
        @Query(value = "page") page: Int? = 0,
        @Query(value = "searchQuery") query: String?): PagedModel<User>

    @POST("users/update")
    suspend fun updateProfile(@Body user: User): User
}