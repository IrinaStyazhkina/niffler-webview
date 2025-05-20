package ru.niffer_android.repository.spending

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.Period
import ru.niffer_android.model.Result
import ru.niffer_android.model.Spend
import ru.niffer_android.model.SpendingToCreate

interface SpendingRepository {

    suspend fun getSpends(
        page: Int?,
        query: String?,
        period: Period?,
        currency: String?,
    ): Flow<Result<PagedModel<Spend>>>

    suspend fun createSpend(spendingToCreate: SpendingToCreate): Flow<Result<Spend>>

    suspend fun editSpend(spend: Spend): Flow<Result<Spend>>

    suspend fun deleteSpends(spendingIdsToDelete: List<String>): Flow<Result<Unit>>

}