package ru.niffer_android.ui.newSpending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.niffer_android.model.Category
import ru.niffer_android.model.Currency
import ru.niffer_android.model.Result
import ru.niffer_android.model.Spend
import ru.niffer_android.model.SpendingToCreate
import ru.niffer_android.repository.category.CategoryRepository
import ru.niffer_android.repository.currency.CurrencyRepository
import ru.niffer_android.repository.spending.SpendingRepository
import javax.inject.Inject

@HiltViewModel
class EditSpendingViewModel @Inject constructor(
    private val spendingRepository: SpendingRepository,
    private val currencyRepository: CurrencyRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _currencies = MutableStateFlow<Result<List<Currency>>>(Result.Loading)
    val currencies: StateFlow<Result<List<Currency>>> = _currencies.asStateFlow()

    private val _categories =
        MutableStateFlow<Result<List<Category>>>(Result.Loading)
    val categories: StateFlow<Result<List<Category>>> = _categories.asStateFlow()

    private val _selectedCurrency = MutableStateFlow("")
    val selectedCurrency: StateFlow<String> = _selectedCurrency.asStateFlow()

    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate.asStateFlow()

    private val _editSpending = MutableStateFlow<Spend?>(null)
    val editSpending: StateFlow<Spend?> = _editSpending.asStateFlow()

    private val _spendingActionResult = MutableSharedFlow<Result<Spend>>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val spendingActionResult: SharedFlow<Result<Spend>> = _spendingActionResult

    init {
        loadCurrencies()
        loadCategories(excludeArchived = true)
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            currencyRepository.getCurrencies()
                .collect { curr ->
                    _currencies.value = curr
                }
        }
    }

    fun loadCategories(excludeArchived: Boolean) {
        viewModelScope.launch {
            categoryRepository.getCategories(excludeArchived)
                .collect { categoriesData ->
                    _categories.value = categoriesData
                }
        }
    }

    fun setSelectedCurrency(text: String) {
        _selectedCurrency.value = text
    }

    fun setSelectedCategory(text: String) {
        _selectedCategory.value = text
    }

    fun setSelectedDate(text: Long) {
        _selectedDate.value = text
    }

    fun addSpending(amount: Double, description: String) {
        val categories = _categories.value
        val category = if (_selectedCategory.value != "" && categories is Result.Success) {
            requireNotNull(categories.data.find { it.name == _selectedCategory.value })
        } else throw RuntimeException("Incorrect category name")


        val spendingToCreate = SpendingToCreate(
            amount = amount,
            description = description,
            category = category,
            currency = _selectedCurrency.value,
            spendDate = _selectedDate.value.toString(),
        )

        viewModelScope.launch {
            spendingRepository.createSpend(spendingToCreate).collect { result ->
                _spendingActionResult.emit(result)
            }
        }
    }

    fun editSpending(amount: Double, description: String) {
        val categories = _categories.value
        val category = if (_selectedCategory.value != "" && categories is Result.Success) {
            requireNotNull(categories.data.find { it.name == _selectedCategory.value })
        } else throw RuntimeException("Incorrect category name")

        val spendId = _editSpending.value?.id

        if (spendId != null) {
            val editedSpend = Spend(
                id = spendId,
                amount = amount,
                description = description,
                category = category,
                currency = _selectedCurrency.value,
                spendDate = _selectedDate.value.toString(),
            )


            viewModelScope.launch {
                spendingRepository.editSpend(editedSpend).collect { result ->
                    _spendingActionResult.emit(result)
                }
            }
        }
    }

    fun setEditSpending(spending: Spend?) {
        _editSpending.value = spending
    }
}