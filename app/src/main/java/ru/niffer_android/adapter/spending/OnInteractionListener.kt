package ru.niffer_android.adapter.spending

import ru.niffer_android.model.Spend

interface OnInteractionListener {
    fun onCheckboxClick(spending: Spend)

    fun onEditIconClick(spending: Spend)

}