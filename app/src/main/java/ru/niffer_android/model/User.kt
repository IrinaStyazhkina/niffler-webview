package ru.niffer_android.model

data class User (
    val id: String,
    val username: String,
    val fullname: String?,
    val photo: String?,
    val photoSmall: String?,
    val friendshipStatus: FriendshipStatus?
)
