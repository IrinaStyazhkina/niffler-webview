package ru.niffer_android.ui.newSpending

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.niffer_android.R
import ru.niffer_android.adapter.currency.CurrencyDropdownAdapter
import ru.niffer_android.databinding.FragmentEditSpendingBinding
import ru.niffer_android.model.CurrencyDropdownItem
import ru.niffer_android.model.Result
import ru.niffer_android.model.Spend
import ru.niffer_android.utils.CurrencyUtils
import ru.niffer_android.utils.UiFormatter
import ru.niffer_android.utils.hideLoader
import ru.niffer_android.utils.showError
import ru.niffer_android.utils.showLoader
import ru.niffer_android.utils.showSuccess
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditSpendingFragment : Fragment() {

    private lateinit var binding: FragmentEditSpendingBinding
    private lateinit var datePicker: MaterialDatePicker<Long>
    private val editSpendingViewModel: EditSpendingViewModel by activityViewModels()
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditSpendingBinding.inflate(
            inflater,
            container,
            false,
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectData()
        setupDatePicker()
        setupListeners()
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { collectCurrencies() }
                launch { collectSpendingDetails() }
                launch { collectCategories() }
                launch { collectSpendingActionResult() }
            }
        }
    }

    private suspend fun collectSpendingDetails() {
        editSpendingViewModel.editSpending.collect { spending ->
            spending?.let { updateSpendingDetails(it) }
        }
    }

    private fun updateSpendingDetails(spending: Spend) {
        binding.apply {
            tvAddSpendingHeader.text = getString(R.string.edit_spending)
            buttonAdd.text = getString(R.string.save_changes)

            inputAmount.setText(spending.amount.toString())
            inputDescription.setText(spending.description)

            editSpendingViewModel.setSelectedCategory(spending.category.name)
            editSpendingViewModel.setSelectedCurrency(spending.currency)

            inputDate.setText(formatDate(spending.spendDate))
        }
    }

    private fun formatDate(dateString: String): String {
        return try {
            val offsetDateTime = OffsetDateTime.parse(dateString)
            dateFormatter.format(Date.from(offsetDateTime.toInstant()))
        } catch (e: Exception) {
            Log.e("EditSpendingFragment", "Invalid date format", e)
            ""
        }
    }

    private suspend fun collectCategories() {
        editSpendingViewModel.categories.collect { result ->
            when (result) {
                is Result.Loading -> Unit
                is Result.Success -> {
                    val categoryList = result.data.map { it.name }
                    val categoryAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        categoryList
                    )
                    binding.categorySelect.setAdapter(categoryAdapter)
                }

                is Result.Error -> showError("Categories are not loaded")
            }
        }
    }

    private suspend fun collectCurrencies() {
        editSpendingViewModel.currencies.collect { currencies ->
            when (currencies) {
                is Result.Loading -> Unit
                is Result.Success -> {
                    if (currencies.data.isNotEmpty()) {
                        val curList = currencies.data.map { CurrencyUtils.getCurrencyItem(it.currency) }
                        val currenciesAdapter = CurrencyDropdownAdapter(requireContext(), curList)
                        binding.currencySelect.setAdapter(currenciesAdapter)
                        val initialCurrency = curList.find { it.text == "RUB" } ?: curList.first()
                        UiFormatter.setFormatterCurrencyToInput(binding.currencySelect, initialCurrency)
                        editSpendingViewModel.setSelectedCurrency(initialCurrency.text)
                    }
                }
                is Result.Error -> showError("Currencies are not loaded")
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            currencySelect.setOnItemClickListener { parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position) as CurrencyDropdownItem
                editSpendingViewModel.setSelectedCurrency(selectedItem.text)
                UiFormatter.setFormatterCurrencyToInput(currencySelect, selectedItem)
            }

            categorySelect.setOnItemClickListener { parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position) as String
                editSpendingViewModel.setSelectedCategory(selectedItem)
            }

            buttonAdd.setOnClickListener { handleSubmitClick() }
            buttonCancel.setOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupDatePicker() {
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()

        binding.inputDateLayout.setEndIconOnClickListener {
            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = dateFormatter.format(Date(selection))
            editSpendingViewModel.setSelectedDate(selection)
            binding.inputDate.setText(selectedDate)
        }

        val listener = MaskedTextChangedListener("[00]{.}[00]{.}[0000]", binding.inputDate)
        binding.inputDate.addTextChangedListener(listener)
        binding.inputDate.onFocusChangeListener = listener

        binding.inputDate.doAfterTextChanged { text ->
            val dateText = text.toString()
            val parsedDate = try {
                dateFormatter.parse(dateText)?.time
            } catch (e: Exception) {
                null
            }

            if (parsedDate != null) {
                editSpendingViewModel.setSelectedDate(parsedDate)
            }
        }
        binding.inputDateLayout.errorIconDrawable = null

    }

    private fun handleSubmitClick() {
        val amountText = binding.inputAmount.text.toString()
        val description = binding.inputDescription.text.toString()

        val category = editSpendingViewModel.selectedCategory.value
        val date = editSpendingViewModel.selectedDate.value

        if (validateInputData(amount = amountText, category, date)) {
            return
        }
        if (editSpendingViewModel.editSpending.value == null) {
            editSpendingViewModel.addSpending(amountText.toDouble(), description)
        } else {
            editSpendingViewModel.editSpending(amountText.toDouble(), description)
        }
    }

    private fun validateInputData(
        amount: String,
        category: String,
        date: Long?,
        ): Boolean {
        var formHasErrors = false
        if (amount.isBlank()) {
            formHasErrors = true
            binding.inputAmountLayout.error = "Amount can not be blank"
        } else if (!(amount.toDouble() > 0)) {
            formHasErrors = true
            binding.inputAmountLayout.error = "Amount should be more than 0.01"
        } else {
            binding.inputAmountLayout.error = null
        }

        if (category.isBlank()) {
            formHasErrors = true
            binding.categorySelectLayout.error = "Category can not be blank"
        } else {
            binding.categorySelectLayout.error = null
        }

        if (date == null) {
            formHasErrors = true
            binding.inputDateLayout.error = "Date can not be blank"
        } else {
            val d = Date(date.toLong())
            if (d.after(Date())) {
                formHasErrors = true
                binding.inputDateLayout.error = "You can not choose future date"
            }
            else {
                binding.inputDateLayout.error = null
            }
        }

        return formHasErrors
    }

    private suspend fun collectSpendingActionResult(){
        editSpendingViewModel.spendingActionResult.collect { result ->
            when (result) {
                is Result.Loading -> showLoader()
                is Result.Success -> {
                    hideLoader()
                    showSuccess("Spending saved")
                    findNavController().navigate(R.id.action_global_toMainPage)
                }
                is Result.Error -> {
                    hideLoader()
                    val message = result.exception.detail ?: "Unknown error"
                    showError(message)
                }
            }
        }
    }
}