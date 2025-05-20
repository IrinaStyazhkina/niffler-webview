package ru.niffer_android.adapter.category

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.niffer_android.R
import ru.niffer_android.databinding.CategoryItemBinding
import ru.niffer_android.model.Category

class CategoryViewHolder(
    private val binding: CategoryItemBinding,
    private val onInteractionListener: OnInteractionListener,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(category: Category) {

        val blueColor = ContextCompat.getColor(context, R.color.blue_100)
        val grayColor = ContextCompat.getColor(context, R.color.gray_300)
        binding.categoryName.let {
            it.text = category.name
            if (!category.archived) {
                it.chipBackgroundColor = ColorStateList.valueOf(blueColor)
                it.setTextColor(Color.WHITE)
            } else {
                it.chipBackgroundColor = ColorStateList.valueOf(grayColor)
                it.setTextColor(Color.BLACK)
            }
        }
        binding.buttonEdit.setOnClickListener { onInteractionListener.onEditClick(category) }
        binding.buttonArchive.setOnClickListener { onInteractionListener.onArchiveButtonClick(category) }
    }

}