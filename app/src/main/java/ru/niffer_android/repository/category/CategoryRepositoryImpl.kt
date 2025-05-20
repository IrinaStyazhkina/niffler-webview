package ru.niffer_android.repository.category

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.network.api.CategoryApi
import ru.niffer_android.model.Category
import ru.niffer_android.model.CategoryToCreate
import ru.niffer_android.model.Result
import ru.niffer_android.utils.flowWithResult
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryApi: CategoryApi) :
    CategoryRepository {

    override suspend fun getCategories(excludeArchived: Boolean): Flow<Result<List<Category>>> =
        flowWithResult {
            categoryApi.getCategories(excludeArchived)
        }

    override suspend fun updateCategory(category: Category): Flow<Result<Category>> =
        flowWithResult {
            categoryApi.editCategory(category)
        }

    override suspend fun createCategory(category: CategoryToCreate): Flow<Result<Category>> =
        flowWithResult {
            categoryApi.createCategory(category)
        }
}