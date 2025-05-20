package ru.niffer_android.model

data class Spend (
    val id: String,
    val amount: Double,
    val category: Category,
    val currency: String,
    val description: String,
    val spendDate: String
)

data class SpendingToCreate (
    val amount: Double,
    val category: Category,
    val currency: String,
    val description: String,
    val spendDate: String
)