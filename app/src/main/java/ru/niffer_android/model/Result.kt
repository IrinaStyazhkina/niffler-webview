package ru.niffer_android.model

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val exception: ApiError): Result<Nothing>()
    data object Loading: Result<Nothing>()
}