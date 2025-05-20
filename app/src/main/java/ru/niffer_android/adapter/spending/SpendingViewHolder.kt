package ru.niffer_android.adapter.spending

import androidx.recyclerview.widget.RecyclerView
import ru.niffer_android.databinding.SpendingItemBinding
import ru.niffer_android.model.Spend
import ru.niffer_android.utils.CurrencyUtils
import ru.niffer_android.utils.DateUtils

class SpendingViewHolder(
    private val binding: SpendingItemBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(spending: Spend) {
        val spendSum = "${spending.amount} ${CurrencyUtils.getCurrencySign(spending.currency)}"
        binding.tvSpendingDescription.text = spending.description
        binding.tvSpendingPrice.text = spendSum
        binding.tvSpendingCategory.text = spending.category.name
        binding.tvSpendingDate.text = DateUtils.convertTimestampToUiDate(spending.spendDate)

        binding.spendingCheckbox.setOnClickListener {
            onInteractionListener.onCheckboxClick(spending)
        }

        binding.buttonEdit.setOnClickListener {
            onInteractionListener.onEditIconClick(spending)
        }
    }
}