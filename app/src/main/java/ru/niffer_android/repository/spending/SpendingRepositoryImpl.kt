package ru.niffer_android.repository.spending

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.PagedModel
import ru.niffer_android.model.Period
import ru.niffer_android.model.Result
import ru.niffer_android.model.Spend
import ru.niffer_android.model.SpendingToCreate
import ru.niffer_android.network.api.SpendingApi
import ru.niffer_android.utils.flowWithResult
import javax.inject.Inject

class SpendingRepositoryImpl @Inject constructor(private val spendingApi: SpendingApi):
    SpendingRepository {
    override suspend fun getSpends(page: Int?, query: String?, period: Period?, currency: String?): Flow<Result<PagedModel<Spend>>> = flowWithResult {
        spendingApi.getSpends(
            page = page,
            query = query,
            period = period?.apiValue,
            currency = currency
        )
    }

    override suspend fun createSpend(spendingToCreate: SpendingToCreate): Flow<Result<Spend>> = flowWithResult {
        spendingApi.createSpend(spendingToCreate)
    }

    override suspend fun editSpend(spend: Spend): Flow<Result<Spend>> = flowWithResult {
        spendingApi.editSpend(spend)
    }

    override suspend fun deleteSpends(spendingIdsToDelete: List<String>): Flow<Result<Unit>> = flowWithResult {
        spendingApi.deleteSpends(spendingIdsToDelete)
    }
}