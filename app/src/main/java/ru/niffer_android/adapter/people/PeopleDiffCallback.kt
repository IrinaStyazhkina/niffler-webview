package ru.niffer_android.adapter.people

import androidx.recyclerview.widget.DiffUtil
import ru.niffer_android.model.User

class PeopleDiffCallback: DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}