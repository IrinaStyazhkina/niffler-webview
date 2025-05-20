package ru.niffer_android.repository.invitation

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.Invitation
import ru.niffer_android.model.Result
import ru.niffer_android.model.User

interface InvitationRepository {

    fun sendInvitation(invitation: Invitation): Flow<Result<User>>

    fun acceptInvitation(invitation: Invitation): Flow<Result<User>>

    fun declineInvitation(invitation: Invitation): Flow<Result<User>>

}