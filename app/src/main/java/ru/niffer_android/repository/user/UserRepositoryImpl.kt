package ru.niffer_android.repository.user

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.Result
import ru.niffer_android.network.api.UserApi
import ru.niffer_android.model.User
import ru.niffer_android.utils.flowWithResult
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userApi: UserApi) : UserRepository {
    override fun getCurrentUser(): Flow<Result<User>> = flowWithResult {
        userApi.getCurrentUser()
    }

    override fun getAllPeople(page: Int?, query: String?): Flow<Result<PagedModel<User>>> = flowWithResult {
        userApi.getAllPeople(page = page, query = query)
    }

    override fun updateUser(user: User): Flow<Result<User>> = flowWithResult {
        userApi.updateProfile(user)
    }
}