package ru.niffer_android.repository.statistics

import kotlinx.coroutines.flow.Flow
import ru.niffer_android.model.Result
import ru.niffer_android.model.Statistics
import ru.niffer_android.network.api.StatisticsApi
import ru.niffer_android.utils.flowWithResult
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(private val statisticsApi: StatisticsApi) :
    StatisticsRepository {
    override suspend fun getStatistics(): Flow<Result<Statistics>> = flowWithResult {
        statisticsApi.getStatistics()
    }
}