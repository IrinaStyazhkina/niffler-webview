package ru.niffer_android.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.niffer_android.model.Category
import ru.niffer_android.model.CategoryToCreate
import ru.niffer_android.model.Result
import ru.niffer_android.repository.category.CategoryRepository
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _categories =
        MutableStateFlow<Result<List<Category>>>(Result.Loading)
    val categories: StateFlow<Result<List<Category>>> = _categories.asStateFlow()

    init {
        loadCategories(true)
    }

    fun loadCategories(excludeArchived: Boolean) {
        viewModelScope.launch {
            categoryRepository.getCategories(excludeArchived)
                .collect { categoriesData ->
                    _categories.value = categoriesData
                }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.updateCategory(category)
                .collect { response ->
                    if (response is Result.Success) {
                        val updatedCategory = response.data

                        _categories.update { currentState ->
                            when (currentState) {
                                is Result.Success -> {
                                    val updatedList = currentState.data.map { existingCategory ->
                                        if (existingCategory.id == updatedCategory.id) updatedCategory else existingCategory
                                    }
                                    Result.Success(updatedList)
                                }

                                else -> currentState
                            }
                        }
                    }
                }
        }
    }


    fun createCategory(name: String) {
        if (name != "") {
            viewModelScope.launch {
                categoryRepository.createCategory(CategoryToCreate(name = name))
                    .collect { categoryData ->
                        loadCategories(true)
                    }
            }
        }
    }
}