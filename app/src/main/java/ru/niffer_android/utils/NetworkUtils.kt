package ru.niffer_android.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import ru.niffer_android.model.ApiError
import ru.niffer_android.model.Result
import ru.niffer_android.model.parseErrorBody

inline fun <T> flowWithResult(crossinline block: suspend () -> T): Flow<Result<T>> =
    flow {
        emit(Result.Loading)
        emit(Result.Success(block()))
    }
        .catch { e ->
            val error = if (e is HttpException) {
                parseErrorBody<ApiError>(e)
            } else {
                null
            }
            emit(Result.Error(error ?: ApiError()))
        }.flowOn(Dispatchers.IO)