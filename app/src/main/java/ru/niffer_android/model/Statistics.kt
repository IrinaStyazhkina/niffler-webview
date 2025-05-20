package ru.niffer_android.model

data class Statistics(
    val total: Double,
    val currency: String,
    val statByCategories: List<CategoryStatistic>,
)

data class CategoryStatistic(
    val categoryName: String,
    val currency: String,
    val sum: Double,
    val firstSpendDate: String,
    val lastSpendDate: String,
)