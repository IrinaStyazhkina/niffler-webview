package ru.niffer_android.repository.friend

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.Result
import ru.niffer_android.model.User
import ru.niffer_android.network.api.FriendApi
import ru.niffer_android.utils.flowWithResult
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(private val friendApi: FriendApi) :
    FriendRepository {
    override fun removeFriend(username: String): Flow<Result<Unit>> = flowWithResult {
        friendApi.deleteFriend(username)
    }

    override fun getAllFriends(page: Int?, query: String?): Flow<Result<PagedModel<User>>> = flowWithResult {
        friendApi.getFriends(page = page, query = query)
    }
}