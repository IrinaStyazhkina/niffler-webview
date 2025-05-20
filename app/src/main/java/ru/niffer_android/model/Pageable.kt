package ru.niffer_android.model

data class Pageable<T>(
    val content: List<T>,
    val number: Int,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
)
