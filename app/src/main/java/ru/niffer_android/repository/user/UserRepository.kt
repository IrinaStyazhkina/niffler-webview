package ru.niffer_android.repository.user

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.Result
import ru.niffer_android.model.User

interface UserRepository {

    fun getCurrentUser(): Flow<Result<User>>

    fun getAllPeople(page: Int?, query: String?): Flow<Result<PagedModel<User>>>

    fun updateUser(user: User): Flow<Result<User>>

}