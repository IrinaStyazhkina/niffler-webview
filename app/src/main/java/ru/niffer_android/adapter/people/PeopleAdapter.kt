package ru.niffer_android.adapter.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.niffer_android.databinding.PeopleItemBinding
import ru.niffer_android.model.User

class PeopleAdapter(private val onInteractionListener: OnInteractionListener): ListAdapter<User, PeopleViewHolder>(
    PeopleDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val binding = PeopleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PeopleViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val user = getItem(position) ?: return
        holder.bind(user)
    }
}