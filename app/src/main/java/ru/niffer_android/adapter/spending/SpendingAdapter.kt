package ru.niffer_android.adapter.spending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.niffer_android.databinding.SpendingItemBinding
import ru.niffer_android.model.Spend

class SpendingAdapter (private val onInteractionListener: OnInteractionListener): ListAdapter<Spend, SpendingViewHolder>(
    SpendingDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpendingViewHolder {
        val binding = SpendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpendingViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: SpendingViewHolder, position: Int) {
        val spending = getItem(position) ?: return
        holder.bind(spending)
    }
}