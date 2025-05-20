package ru.niffer_android.repository.invitation

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.Invitation
import ru.niffer_android.model.Result
import ru.niffer_android.model.User
import ru.niffer_android.network.api.InvitationApi
import ru.niffer_android.utils.flowWithResult
import javax.inject.Inject

class InvitationRepositoryImpl @Inject constructor(private val invitationApi: InvitationApi) :InvitationRepository {
    override fun sendInvitation(invitation: Invitation): Flow<Result<User>>  = flowWithResult {
        invitationApi.sendInvitation(invitation)
    }

    override fun acceptInvitation(invitation: Invitation): Flow<Result<User>> = flowWithResult {
        invitationApi.acceptInvitation(invitation)
    }

    override fun declineInvitation(invitation: Invitation): Flow<Result<User>> = flowWithResult {
        invitationApi.declineInvitation(invitation)
    }
}