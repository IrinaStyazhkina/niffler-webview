package ru.niffer_android.network.api

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.User

interface FriendApi {

    @DELETE("friends/remove")
    suspend fun deleteFriend(@Query(value = "username") username: String)

    @GET("v3/friends/all")
    suspend fun getFriends(
        @Query(value = "page") page: Int? = 0,
        @Query(value = "searchQuery") query: String?): PagedModel<User>
}