package ru.niffer_android.repository.friend

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.Result
import ru.niffer_android.model.User

interface FriendRepository {

    fun removeFriend(username: String): Flow<Result<Unit>>

    fun getAllFriends(page: Int?, query: String?): Flow<Result<PagedModel<User>>>

}