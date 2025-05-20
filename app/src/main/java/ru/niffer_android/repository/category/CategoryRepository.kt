package ru.niffer_android.repository.category

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.Category
import ru.niffer_android.model.CategoryToCreate
import ru.niffer_android.model.Result

interface CategoryRepository {

    suspend fun getCategories(excludeArchived: Boolean): Flow<Result<List<Category>>>

    suspend fun updateCategory(category: Category): Flow<Result<Category>>

    suspend fun createCategory(category: CategoryToCreate): Flow<Result<Category>>
}