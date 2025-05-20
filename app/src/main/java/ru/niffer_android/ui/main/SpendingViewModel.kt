package ru.niffer_android.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.niffer_android.model.Currency
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.Period
import ru.niffer_android.model.Result
import ru.niffer_android.model.Spend
import ru.niffer_android.repository.currency.CurrencyRepository
import ru.niffer_android.repository.spending.SpendingRepository
import javax.inject.Inject

@HiltViewModel
class SpendingViewModel @Inject constructor(
    private val spendingRepository: SpendingRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _spendings =
        MutableStateFlow<Result<PagedModel<Spend>>>(Result.Loading)
    val spendings: StateFlow<Result<PagedModel<Spend>>> = _spendings.asStateFlow()

    private val _currencies = MutableStateFlow<Result<List<Currency>>>(Result.Loading)
    val currencies: StateFlow<Result<List<Currency>>> = _currencies.asStateFlow()

    private val _selectedCurrency = MutableStateFlow("")
    val selectedCurrency: StateFlow<String> = _selectedCurrency.asStateFlow()

    private val _spendIdsToDelete = MutableStateFlow<List<String>>(emptyList())
    val spendIdsToDelete: StateFlow<List<String>> = _spendIdsToDelete.asStateFlow()

    private val _spendsSearchQuery = MutableStateFlow("")
    val spendsSearchQuery: StateFlow<String> = _spendsSearchQuery.asStateFlow()

    val periods: StateFlow<List<String>> =
        MutableStateFlow(Period.entries.map { it.uiName }).asStateFlow()

    private val _selectedPeriod = MutableStateFlow(Period.ALL_TIME)
    val selectedPeriod: StateFlow<Period> = _selectedPeriod.asStateFlow()

    private var currentPage = 0
    private var isLoadingMore = false


    init {
        viewModelScope.launch {
            currencyRepository.getCurrencies()
                .collect { curr ->
                    _currencies.value = curr
                }
        }
    }

    fun loadSpends() {
        currentPage = 0
        viewModelScope.launch {
            spendingRepository.getSpends(
                page = currentPage,
                query = _spendsSearchQuery.value,
                period = _selectedPeriod.value,
                currency = _selectedCurrency.value
            )
                .collect { spends ->
                    _spendings.value = spends
                }
        }
    }

    fun deleteSpends() {
        val spendingIds = _spendIdsToDelete.value
        if (spendingIds.isNotEmpty()) {
            viewModelScope.launch {
                spendingRepository.deleteSpends(spendingIds).catch { e ->
                    Log.e("SpendingViewModel", "Ошибка удаления трат")
                }.collect { res ->
                    if (res is Result.Success) {
                        loadSpends()
                    }
                }
            }
        }
    }

    fun updateSpendIdsToDelete(spendId: String) {
        val spend = _spendIdsToDelete.value.find { it == spendId }
        if (spend != null) {
            _spendIdsToDelete.value = _spendIdsToDelete.value.filter { it != spendId }
        } else {
            val spends = _spendIdsToDelete.value.toMutableList()
            spends.add(spendId)
            _spendIdsToDelete.value = spends
        }
    }

    fun updateSpendsSearchQuery(query: String) {
        _spendsSearchQuery.value = query
        loadSpends()
    }


    fun setSelectedCurrency(text: String) {
        _selectedCurrency.value = text
        loadSpends()
    }

    fun setSelectedPeriod(period: Period) {
        _selectedPeriod.value = period
        loadSpends()
    }

    fun loadNextSpendings() {
        if (isLoadingMore) return
        isLoadingMore = true
        viewModelScope.launch {
            val spends = _spendings.value
            if (spends is Result.Success && currentPage == spends.data.page.totalPages - 1) {
                isLoadingMore = false
                return@launch
            }
            currentPage++
            spendingRepository.getSpends(
                page = currentPage,
                query = _spendsSearchQuery.value,
                period = _selectedPeriod.value,
                currency = _selectedCurrency.value
            )
                .collect { result ->
                    if (result is Result.Success) {
                        _spendings.update { current ->
                            when (current) {
                                is Result.Success -> {
                                    isLoadingMore = false
                                    Result.Success(
                                        PagedModel(
                                            content = current.data.content + result.data.content,
                                            page = result.data.page
                                        )
                                    )
                                }

                                else -> {
                                    isLoadingMore = false
                                    result
                                }
                            }
                        }
                    }
                }
        }
    }
}