package ru.niffer_android.network.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.niffer_android.model.Invitation
import ru.niffer_android.model.User

interface InvitationApi {
    @POST("invitations/send")
    suspend fun sendInvitation(@Body invitation: Invitation): User

    @POST("invitations/accept")
    suspend fun acceptInvitation(@Body invitation: Invitation): User

    @POST("invitations/decline")
    suspend fun declineInvitation(@Body invitation: Invitation): User
}