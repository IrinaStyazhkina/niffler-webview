package ru.niffer_android.adapter.category

import ru.niffer_android.model.Category

interface OnInteractionListener {

    fun onArchiveButtonClick(category: Category)

    fun onEditClick(category: Category)
}