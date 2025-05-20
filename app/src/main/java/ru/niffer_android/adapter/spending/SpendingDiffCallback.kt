package ru.niffer_android.adapter.spending

import androidx.recyclerview.widget.DiffUtil
import ru.niffer_android.model.Spend


class SpendingDiffCallback: DiffUtil.ItemCallback<Spend>() {
    override fun areItemsTheSame(oldItem: Spend, newItem: Spend): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Spend, newItem: Spend): Boolean {
        return oldItem == newItem
    }
}