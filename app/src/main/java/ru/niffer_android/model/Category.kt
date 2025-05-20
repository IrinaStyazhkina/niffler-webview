package ru.niffer_android.model

data class Category(
    val id: String,
    val name: String,
    val archived: Boolean,
    val username: String,
)

data class CategoryToCreate(
    val name: String,
)
