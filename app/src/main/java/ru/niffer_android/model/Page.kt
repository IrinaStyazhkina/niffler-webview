package ru.niffer_android.model

data class PagedModel<T>(
    val page: Page,
    val content: List<T>
)

data class Page(
    val size: Int,
    val number: Int,
    val totalElements: Int,
    val totalPages: Int,
)