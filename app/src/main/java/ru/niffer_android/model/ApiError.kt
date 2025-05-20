package ru.niffer_android.model

import com.google.gson.Gson
import retrofit2.HttpException

data class ApiError (
    val type: String? = null,
    val title: String? = "Something went wrong",
    val status: Int? = null,
    val detail: String? = "Unknown error occurred",
    val instance: String? = null
)

inline fun <reified T> parseErrorBody(exception: HttpException): T? {
    return try {
        val gson = Gson()
        val json = exception.response()?.errorBody()?.string()
        gson.fromJson(json, T::class.java)
    } catch (e: Exception) {
        null
    }
}